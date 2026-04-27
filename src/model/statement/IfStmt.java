package model.statement;

import model.ADT.IStack;
import model.ADT.IDict;
import model.exceptions.ExpressionException;
import model.exceptions.MyException;
import model.exceptions.StatementException;
import model.expression.IExpr;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.IType;
import model.value.BoolValue;
import model.value.IValue;

public class IfStmt implements IStmt {
    private final IExpr condition;
    private final IStmt thenS;
    private final IStmt elseS;

    public IfStmt(IExpr condition, IStmt thenS, IStmt elseS) {
        this.condition = condition;
        this.thenS = thenS;
        this.elseS = elseS;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        try {
            IValue condVal = condition.eval(state.getSymTable(), state.getHeap());

            if (!condVal.getType().equals(new BoolType())) {
                throw new StatementException("If: condition is not boolean");
            }

            boolean b = ((BoolValue) condVal).getValue();
            IStack<IStmt> stack = state.getExeStack();
            stack.push(b ? thenS : elseS);
            return state;

        } catch (ExpressionException e) {
            throw new StatementException("If: " + e.getMessage());
        }
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws MyException {
        IType typexp = condition.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            thenS.typecheck(typeEnv.deepCopy());
            elseS.typecheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new MyException("The condition of IF has not the type bool");
    }

    @Override
    public IStmt deepCopy() {
        return new IfStmt(condition.deepCopy(), thenS.deepCopy(), elseS.deepCopy());
    }

    @Override
    public String toString() {
        return "If(" + condition.toString() + ") Then(" + thenS.toString() + ") Else(" + elseS.toString() + ")";
    }
}