// java
package model.statement;

import model.ADT.*;
import model.state.ProgramState;
import model.exceptions.MyException;
import model.value.StringValue; // added
import java.io.BufferedReader;  // added
import model.exceptions.StatementException;
import model.value.IValue;
import model.type.IType;

public class ForkStmt implements IStmt {
    private final IStmt stmt;

    public ForkStmt(IStmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        IDict<String, IValue> parentSymTable = state.getSymTable();
        IDict<String, IValue> childSymTable = parentSymTable.deepCopy();

        IStack<IStmt> childStack = new MyStack<>();
        childStack.push(stmt);

        IList<IValue> out = state.getOut();
        IDict<StringValue, BufferedReader> fileTable = state.getFileTable();
        IHeap<Integer, IValue> heap = state.getHeap();
        IBarrierTable barrierTable = state.getBarrierTable();

        // return new ProgramState(childStack, childSymTable, out, (IDict) fileTable, (IHeap) heap, stmt);
        return new ProgramState(childStack, childSymTable, out, fileTable, heap, barrierTable);

    }

    @Override
    public IStmt deepCopy() {
        return new ForkStmt(stmt.deepCopy());
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws MyException {
        stmt.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "fork(" + stmt.toString() + ")";
    }
}
