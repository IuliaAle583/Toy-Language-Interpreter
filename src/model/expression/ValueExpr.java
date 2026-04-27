package model.expression;

import model.ADT.IDict;
import model.ADT.IHeap;
import model.value.IValue;
import model.ADT.IDict;
import model.exceptions.MyException;
import model.type.IType;

public class ValueExpr implements IExpr {
    private final IValue val;

    public ValueExpr(IValue val) { this.val = val; }

    @Override
    public IValue eval(IDict<String, IValue> tbl,
                       IHeap<Integer, IValue> heap) {
        return val;
    }

    @Override
    public IExpr deepCopy() {
        return new ValueExpr(val.deepCopy());
    }

    @Override
    public String toString() { return val.toString(); }

    @Override
    public IType typecheck(IDict<String, IType> typeEnv) throws MyException {
        return val.getType();
    }
}