package model.state;

import model.ADT.*;
import model.exceptions.MyException;
import model.statement.IStmt;
import model.value.RefValue;
import model.value.StringValue;
import model.value.IValue;

import java.io.BufferedReader;
import java.util.Set;
import java.util.stream.Collectors;

public class ProgramState {
    private final IStack<IStmt> exeStack;
    private final IDict<String, IValue> symTable;
    private final IList<IValue> out;
    private final IDict<StringValue, BufferedReader> fileTable;
    private final IHeap<Integer, IValue> heap;
    private final IStmt originalProgram;
    private final int id;
    private static int lastId = 0;
    private final IBarrierTable barrierTable;



    private static synchronized int getNewId() {
        return ++lastId;
    }

    public ProgramState(IStack<IStmt> exeStack,
                        IDict<String, IValue> symTable,
                        IList<IValue> out,
                        IDict<StringValue, BufferedReader> fileTable,
                        IHeap<Integer, IValue> heap,
                        IBarrierTable barrierTable,
                        IStmt originalProgram) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.barrierTable = barrierTable;
        this.originalProgram = originalProgram.deepCopy();
        this.id = getNewId();

        this.exeStack.push(originalProgram);
    }

    public ProgramState(IStack<IStmt> exeStack,
                        IDict<String, IValue> symTable,
                        IList<IValue> out,
                        IDict<StringValue, BufferedReader> fileTable,
                        IHeap<Integer, IValue> heap,
                        IBarrierTable barrierTable) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.barrierTable = barrierTable;
        this.originalProgram = null;
        this.id = getNewId();
    }

    public IBarrierTable getBarrierTable() { return barrierTable; }
    public IStack<IStmt> getExeStack() { return exeStack; }
    public IDict<String, IValue> getSymTable() { return symTable; }
    public IList<IValue> getOut() { return out; }
    public IDict<StringValue, BufferedReader> getFileTable() { return fileTable; }
    public IHeap<Integer, IValue> getHeap() { return heap; }
    public int getId() { return id; }

    @Override
    public String toString() {
        return "Id: " + id + "\n" +
                "ExeStack:\n" + exeStack +
                "\nSymTable:\n" + symTable +
                "\nOut:\n" + out +
                "\nFileTable:\n" + fileTable +
                "\nHeap:\n" + heap+
                "\nBarrierTable:\n" + barrierTable+"\n";
    }

    public Boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    public ProgramState oneStep() throws MyException {
        if (exeStack.isEmpty())
            throw new MyException("Program state stack is empty");
        IStmt crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }

    //used for garbage collector
    public Set<Integer> getUsedAddresses() {
        return symTable.getContent().values().stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddress())
                .collect(Collectors.toSet());
    }


}