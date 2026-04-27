package model.expression;

import model.ADT.IDict;
import model.ADT.IHeap;
import model.ADT.IDict;
import model.exceptions.ExpressionException;
import model.exceptions.MyException;
import model.value.IValue;
import model.type.IType;

public interface IExpr {
    IValue eval(IDict<String, IValue> tbl,
                IHeap<Integer, IValue> heap) throws ExpressionException;

    IExpr deepCopy();

    IType typecheck(IDict<String, IType> typeEnv) throws MyException;
}