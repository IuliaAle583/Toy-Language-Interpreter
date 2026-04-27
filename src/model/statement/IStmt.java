package model.statement;

import model.ADT.IDict;
import model.exceptions.MyException;
import model.exceptions.StatementException;
import model.state.ProgramState;
import model.type.IType;

public interface IStmt {
    ProgramState execute(ProgramState state) throws StatementException;
    IStmt deepCopy();
    IDict<String, IType> typecheck(IDict<String,IType> typeEnv) throws MyException;
}