package model.statement;

import model.ADT.IDict;
import model.ADT.IHeap;
import model.exceptions.ADTException;
import model.exceptions.ExpressionException;
import model.exceptions.StatementException;
import model.exceptions.MyException;
import model.expression.IExpr;
import model.state.ProgramState;
import model.type.RefType;
import model.type.IType;
import model.value.RefValue;
import model.value.IValue;

public class WriteHeapStmt implements IStmt {
    private final String varName;
    private final IExpr exp;

    public WriteHeapStmt(String varName, IExpr exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        IDict<String, IValue> sym = state.getSymTable();
        IHeap<Integer, IValue> heap = state.getHeap();

        try {
            if (!sym.isDefined(varName)) {
                throw new StatementException("wH: variable " + varName + " not defined");
            }

            IValue varVal = sym.lookup(varName);
            if (!(varVal instanceof RefValue refVal)) {
                throw new StatementException("wH: variable " + varName + " is not Ref");
            }

            if (!(refVal.getType() instanceof RefType refType)) {
                throw new StatementException("wH: internal type error for " + varName);
            }

            int addr = refVal.getAddress();
            if (!heap.isDefined(addr)) {
                throw new StatementException("wH: address " + addr + " not in heap");
            }

            IValue evalVal = exp.eval(sym, heap);
            if (!evalVal.getType().equals(refType.getInner())) {
                throw new StatementException("wH: type mismatch between heap location and expression");
            }

            heap.update(addr, evalVal);
            return state;

        } catch (ADTException e) {
            throw new StatementException("wH (heap/sym): " + e.getMessage());
        } catch (ExpressionException e) {
            throw new StatementException("wH (exp): " + e.getMessage());
        }
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws MyException {
        IType varType = typeEnv.lookup(varName);
        IType expType = exp.typecheck(typeEnv);
        if (!(varType instanceof RefType refType))
            throw new MyException("wH: variable " + varName + " is not RefType");
        if (!expType.equals(refType.getInner()))
            throw new MyException("wH: type mismatch between heap location and expression");
        return typeEnv;
    }

    @Override
    public IStmt deepCopy() {
        return new WriteHeapStmt(this.varName, this.exp.deepCopy());
    }

    @Override
    public String toString() {
        return "wH(" + varName.toString() + ", " + exp.toString() + ")";
    }
}