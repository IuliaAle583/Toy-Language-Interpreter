package model.statement;

import model.ADT.IDict;
import model.exceptions.MyException;
import model.exceptions.StatementException;
import model.state.ProgramState;
import model.type.IType;

public class NoOpStmt implements IStmt {
    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new NoOpStmt();
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "NoOp";
    }
}