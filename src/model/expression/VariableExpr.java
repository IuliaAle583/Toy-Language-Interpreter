package model.expression;

import model.ADT.IDict;
import model.ADT.IHeap;
import model.exceptions.ADTException;
import model.exceptions.ExpressionException;
import model.value.IValue;
import model.ADT.IDict;
import model.exceptions.MyException;
import model.type.IType;

public class VariableExpr implements IExpr {
    private final String id;

    public VariableExpr(String id) { this.id = id; }

    @Override
    public IValue eval(IDict<String, IValue> tbl,
                       IHeap<Integer, IValue> heap) throws ExpressionException {
        try {
            return tbl.lookup(id);
        } catch (ADTException e) {
            throw new ExpressionException("Variable " + id + " not defined");
        }
    }

    @Override
    public IExpr deepCopy() {
        return new VariableExpr(this.id);
    }

    @Override
    public String toString() { return id; }

    @Override
    public IType typecheck(IDict<String, IType> typeEnv) throws MyException {
        return typeEnv.lookup(id);
    }
}