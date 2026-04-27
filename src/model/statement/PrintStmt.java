package model.statement;

import model.ADT.IList;
import model.ADT.IDict;
import model.exceptions.ExpressionException;
import model.exceptions.StatementException;
import model.exceptions.MyException;
import model.state.ProgramState;
import model.value.IValue;
import model.type.IType;
import model.expression.IExpr;

public class PrintStmt implements IStmt {
    private final IExpr exp;

    public PrintStmt(IExpr exp) {
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        IList<IValue> out = state.getOut();

        try {
            IValue val = exp.eval(state.getSymTable(), state.getHeap());
            out.add(val);
            return state;
        } catch (ExpressionException e) {
            throw new StatementException("Print failed: " + e.getMessage());
        }
    }

    @Override
    public IStmt deepCopy() {
        return new PrintStmt(exp.deepCopy());
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws MyException {
        exp.typecheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return "print(" + exp.toString() + ")";
    }
}
