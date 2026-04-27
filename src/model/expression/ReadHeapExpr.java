package model.expression;

import model.ADT.IDict;
import model.ADT.IHeap;
import model.exceptions.ADTException;
import model.exceptions.ExpressionException;
import model.value.RefValue;
import model.value.IValue;
import model.ADT.IDict;
import model.exceptions.MyException;
import model.type.IType;
import model.type.RefType;

public class ReadHeapExpr implements IExpr {
    private final IExpr exp;

    public ReadHeapExpr(IExpr exp) {
        this.exp = exp;
    }

    @Override
    public IValue eval(IDict<String, IValue> tbl,
                       IHeap<Integer, IValue> heap) throws ExpressionException {
        IValue v = exp.eval(tbl, heap);

        if (!(v instanceof RefValue refVal))
            throw new ExpressionException("rH: expression is not Ref");

        int addr = refVal.getAddress();
        try {
            if (!heap.isDefined(addr))
                throw new ExpressionException("rH: address " + addr + " not in heap");
            return heap.lookup(addr);
        } catch (ADTException e) {
            throw new ExpressionException("rH: " + e.getMessage());
        }
    }

    @Override
    public String toString() { return "rH(" + exp + ")"; }

    @Override
    public IExpr deepCopy() {
        return new ReadHeapExpr(exp.deepCopy());
    }

    @Override
    public IType typecheck(IDict<String, IType> typeEnv) throws MyException {
        IType typ = exp.typecheck(typeEnv);
        if (typ instanceof RefType) {
            RefType reft = (RefType) typ;
            return reft.getInner();
        } else
            throw new MyException("the rH argument is not a Ref Type");
    }
}