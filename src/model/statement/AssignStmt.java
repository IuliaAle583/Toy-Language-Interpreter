package model.statement;

import model.ADT.IDict;
import model.ADT.IHeap;
import model.exceptions.ADTException;
import model.exceptions.ExpressionException;
import model.exceptions.StatementException;
import model.expression.IExpr;
import model.state.ProgramState;
import model.type.IType;
import model.value.IValue;
import model.ADT.IDict;
import model.exceptions.MyException;
import model.type.IType;

public class AssignStmt implements IStmt {
    private final String id;
    private final IExpr exp;

    public AssignStmt(String id, IExpr exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        IDict<String, IValue> sym = state.getSymTable();
        IHeap<Integer, IValue> heap = state.getHeap();

        try {
            if (!sym.isDefined(id)) {
                throw new StatementException("Assign: variable " + id + " not declared");
            }

            IValue currentVal = sym.lookup(id);
            IType varType = currentVal.getType();

            IValue val = exp.eval(sym, heap);

            if (!val.getType().equals(varType)) {
                throw new StatementException("Assign: type mismatch for variable '" + id + "'");
            }

            sym.update(id, val);
            return state;

        } catch (ADTException e) {
            throw new StatementException("Assign (symTable): " + e.getMessage());
        } catch (ExpressionException e) {
            throw new StatementException("Assign (expression): " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return id.toString() + " = " + exp.toString();
    }

    @Override
    public IStmt deepCopy() {
        return new AssignStmt(this.id, this.exp.deepCopy());
    }


    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws MyException {
        IType typevar = typeEnv.lookup(id);
        IType typexp = exp.typecheck(typeEnv);
        if (typevar.equals(typexp))
            return typeEnv;
        else
            throw new MyException("Assignment: right hand side and left hand side have different types");
    }
}