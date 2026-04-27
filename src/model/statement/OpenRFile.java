package model.statement;

import model.ADT.IDict;
import model.exceptions.ExpressionException;
import model.expression.IExpr;
import model.exceptions.StatementException;
import model.exceptions.MyException;
import model.state.ProgramState;
import model.type.StringType;
import model.value.StringValue;
import model.value.IValue;
import model.type.IType;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenRFile implements IStmt {
    private final IExpr exp;

    public OpenRFile(IExpr exp) {
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        IDict<StringValue, BufferedReader> fileTable = state.getFileTable();

        try {
            IValue v = exp.eval(state.getSymTable(), state.getHeap());
            if (!v.getType().equals(new StringType())) {
                throw new StatementException("openRFile: expression is not string");
            }

            StringValue fileName = (StringValue) v;

            if (fileTable.isDefined(fileName)) {
                throw new StatementException("openRFile: file already opened: " + fileName.getVal());
            }

            try {
                BufferedReader br = new BufferedReader(new FileReader(fileName.getVal()));
                fileTable.put(fileName, br);
                return state;
            } catch (IOException e) {
                throw new StatementException("openRFile: cannot open " + fileName.getVal());
            }

        } catch (ExpressionException e) {
            throw new StatementException("openRFile (exp): " + e.getMessage());
        }
    }

    @Override
    public IStmt deepCopy() {
        return new OpenRFile(exp.deepCopy());
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws MyException {
        IType t = exp.typecheck(typeEnv);
        if (t.equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("openRFile: expression is not a string");
    }

    @Override
    public String toString() {
        return "openRFile(" + exp + ")";
    }
}
