package model.statement;

import model.ADT.IDict;
import model.ADT.IStack;
import model.exceptions.ExpressionException;
import model.exceptions.MyException;
import model.exceptions.StatementException;
import model.expression.IExpr;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.IType;
import model.value.BoolValue;
import model.value.IValue;

public class WhileStmt implements IStmt {
    private final IExpr condition;
    private final IStmt body;

    public WhileStmt(IExpr condition, IStmt body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        IStack<IStmt> stack = state.getExeStack();

        try {
            IValue condVal = condition.eval(state.getSymTable(), state.getHeap());

            if (!condVal.getType().equals(new BoolType())) {
                throw new StatementException("While condition is not boolean");
            }

            BoolValue boolCond = (BoolValue) condVal;
            if (boolCond.getValue()) {
                stack.push(new WhileStmt(condition, body));
                stack.push(body);
            }
            return state;

        } catch (ExpressionException e) {
            throw new StatementException("While: " + e.getMessage());
        }
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws MyException {
        IType condType = condition.typecheck(typeEnv);
        if (!condType.equals(new BoolType()))
            throw new MyException("While: condition is not of type bool");
        body.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(condition.deepCopy(), body.deepCopy());
    }

    @Override
    public String toString() {
        return "while(" + condition.toString() + ") " + body.toString();
    }
}