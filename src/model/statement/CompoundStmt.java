package model.statement;

import model.ADT.IStack;
import model.ADT.IDict;
import model.exceptions.MyException;
import model.exceptions.StatementException;
import model.state.ProgramState;
import model.type.IType;

public class CompoundStmt implements IStmt {
    private final IStmt first;
    private final IStmt second;

    public CompoundStmt(IStmt first, IStmt second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        IStack<IStmt> stack = state.getExeStack();
        stack.push(second);
        stack.push(first);
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new CompoundStmt(first.deepCopy(), second.deepCopy());
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws MyException {
        return second.typecheck(first.typecheck(typeEnv));
    }

    @Override
    public String toString() {
        return first.toString() + "; " + second.toString();
    }
}