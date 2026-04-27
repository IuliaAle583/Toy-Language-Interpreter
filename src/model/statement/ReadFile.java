package model.statement;

import model.ADT.IDict;
import model.exceptions.ADTException;
import model.exceptions.ExpressionException;
import model.exceptions.StatementException;
import model.expression.IExpr;
import model.state.ProgramState;
import model.type.IntType;
import model.type.StringType;
import model.type.IType;
import model.value.IntValue;
import model.value.StringValue;
import model.value.IValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements IStmt {
    private final IExpr exp;
    private final String varName;

    public ReadFile(IExpr exp, String varName) {
        this.exp = exp;
        this.varName = varName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        IDict<String, IValue> sym = state.getSymTable();
        IDict<StringValue, BufferedReader> fileTable = state.getFileTable();

        try {

            if (!sym.isDefined(varName)) {
                throw new StatementException("readFile: variable " + varName + " not declared");
            }
            if (!sym.lookup(varName).getType().equals(new IntType())) {
                throw new StatementException("readFile: variable " + varName + " is not int");
            }

            IValue v = exp.eval(sym, state.getHeap());
            if (!v.getType().equals(new StringType())) {
                throw new StatementException("readFile: expression is not string");
            }

            StringValue fileName = (StringValue) v;
            if (!fileTable.isDefined(fileName)) {
                throw new StatementException("readFile: file " + fileName.getVal() + " not opened");
            }

            BufferedReader br = fileTable.lookup(fileName);
            String line = br.readLine();

            int number;
            if (line == null) {
                number = 0;
            } else {
                try {
                    number = Integer.parseInt(line.trim());
                } catch (NumberFormatException e) {
                    throw new StatementException("readFile: line is not an integer");
                }
            }

            sym.update(varName, new IntValue(number));
            return state;

        } catch (ExpressionException e) {
            throw new StatementException("readFile (exp): " + e.getMessage());
        } catch (ADTException e) {
            throw new StatementException("readFile (sym/fileTable): " + e.getMessage());
        } catch (IOException e) {
            throw new StatementException("readFile: IO error: " + e.getMessage());
        }
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws model.exceptions.MyException {
        IType varType = typeEnv.lookup(varName);
        IType expType = exp.typecheck(typeEnv);
        if (!varType.equals(new IntType()))
            throw new model.exceptions.MyException("readFile: variable " + varName + " is not int");
        if (!expType.equals(new StringType()))
            throw new model.exceptions.MyException("readFile: expression is not string");
        return typeEnv;
    }

    @Override
    public IStmt deepCopy() {
        return new ReadFile(exp.deepCopy(), varName);
    }

    @Override
    public String toString() {
        return "readFile(" + exp.toString() + ", " + varName.toString() + ")";
    }
}