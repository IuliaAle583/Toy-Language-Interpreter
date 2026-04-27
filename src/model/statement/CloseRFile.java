package model.statement;

import model.ADT.IDict;
import model.exceptions.ADTException;
import model.exceptions.ExpressionException;
import model.exceptions.MyException;
import model.exceptions.StatementException;
import model.expression.IExpr;
import model.state.ProgramState;
import model.type.StringType;
import model.type.IType;
import model.value.StringValue;
import model.value.IValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFile implements IStmt {
    private final IExpr exp;

    public CloseRFile(IExpr exp) {
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        IDict<StringValue, BufferedReader> fileTable = state.getFileTable();

        try {
            IValue v = exp.eval(state.getSymTable(), state.getHeap());
            if (!v.getType().equals(new StringType())) {
                throw new StatementException("closeRFile: expression is not string");
            }

            StringValue fileName = (StringValue) v;
            if (!fileTable.isDefined(fileName)) {
                throw new StatementException("closeRFile: file " + fileName.getVal() + " not opened");
            }

            BufferedReader br = fileTable.lookup(fileName);
            try {
                br.close();
                fileTable.remove(fileName);
                return state;
            } catch (IOException e) {
                throw new StatementException("closeRFile: IO error: " + e.getMessage());
            }

        } catch (ExpressionException e) {
            throw new StatementException("closeRFile (exp): " + e.getMessage());
        } catch (ADTException e) {
            throw new StatementException("closeRFile (fileTable): " + e.getMessage());
        }
    }

    @Override
    public IStmt deepCopy() {
        return new CloseRFile(exp.deepCopy());
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp + ")";
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws MyException {
        IType t = exp.typecheck(typeEnv);
        if (t.equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("closeRFile: expression is not a string");
    }
}