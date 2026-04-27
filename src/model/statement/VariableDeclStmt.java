package model.statement;

import model.ADT.IDict;
import model.exceptions.StatementException;
import model.state.ProgramState;
import model.type.IType;
import model.value.IValue;

public class VariableDeclStmt implements IStmt {
    private final String name;
    private final IType type;

    public VariableDeclStmt(String name, IType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        IDict<String, IValue> sym = state.getSymTable();

        if (sym.isDefined(name)) {
            throw new StatementException("VarDecl: variable " + name + " already defined");
        }
        IValue defaultVal = type.defaultValue();
        sym.put(name, defaultVal);

        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new VariableDeclStmt(this.name, this.type.deepCopy());
    }

    @Override
    public String toString() {
        return type.toString() + " " + name.toString();
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnv) throws model.exceptions.MyException {
        typeEnv.put(name, type);
        return typeEnv;
    }
}