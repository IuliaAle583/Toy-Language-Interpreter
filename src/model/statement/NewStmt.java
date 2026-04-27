package model.statement;

import model.ADT.IDict;
import model.ADT.IHeap;
import model.exceptions.ADTException;
import model.exceptions.ExpressionException;
import model.exceptions.StatementException;
import model.exceptions.MyException;
import model.state.ProgramState;
import model.type.RefType;
import model.value.RefValue;
import model.value.IValue;
import model.type.IType;
import model.expression.IExpr;

public class NewStmt implements IStmt {
    private final String varName;
    private final IExpr exp;

    public NewStmt(String varName, IExpr exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        IDict<String, IValue> sym = state.getSymTable();
        IHeap<Integer, IValue> heap = state.getHeap();

        try {
            if (!sym.isDefined(varName)) {
                throw new StatementException("new: variable " + varName + " not defined");
            }

            IValue varVal = sym.lookup(varName);
            if (!(varVal.getType() instanceof RefType refType)) {
                throw new StatementException("new: variable " + varName + " is not Ref type");
            }

            IValue evalVal = exp.eval(sym, heap);
            if (!evalVal.getType().equals(refType.getInner())) {
                throw new StatementException("new: type mismatch: " +
                        evalVal.getType() + " vs " + refType.getInner());
            }

            int addr = heap.allocate(evalVal);
            sym.update(varName, new RefValue(addr, refType.getInner()));
            return state;

        } catch (ADTException e) {
            throw new StatementException("new (symTable/heap): " + e.getMessage());
        } catch (ExpressionException e) {
            throw new StatementException("new (exp): " + e.getMessage());
        }
    }

    @Override
    public IStmt deepCopy() {
        return new NewStmt(varName, exp.deepCopy());
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws MyException {
        IType typevar = typeEnv.lookup(varName);
        IType typexp = exp.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp)))
            return typeEnv;
        else
            throw new MyException("NEW stmt: right hand side and left hand side have different types");
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + exp + ")";
    }
}
