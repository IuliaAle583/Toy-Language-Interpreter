package model.expression;

import model.ADT.IDict;
import model.ADT.IHeap;
import model.exceptions.ExpressionException;
import model.type.BoolType;
import model.value.BoolValue;
import model.value.IValue;
import model.ADT.IDict;
import model.exceptions.MyException;
import model.type.IType;

public class LogicalExpr implements IExpr {
    private final IExpr e1, e2;
    private final int op;

    public LogicalExpr(IExpr e1, IExpr e2, int op) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    @Override
    public IValue eval(IDict<String, IValue> tbl,
                       IHeap<Integer, IValue> heap) throws ExpressionException {
        IValue v1 = e1.eval(tbl, heap);
        if (!v1.getType().equals(new BoolType()))
            throw new ExpressionException("first operand is not bool");

        IValue v2 = e2.eval(tbl, heap);
        if (!v2.getType().equals(new BoolType()))
            throw new ExpressionException("second operand is not bool");

        boolean b1 = ((BoolValue) v1).getValue();
        boolean b2 = ((BoolValue) v2).getValue();

        return switch (op) {
            case 1 -> new BoolValue(b1 && b2);
            case 2 -> new BoolValue(b1 || b2);
            default -> throw new ExpressionException("invalid logic op");
        };
    }

    @Override
    public String toString() {
        String s = (op == 1) ? "and" : (op == 2) ? "or" : "?";
        return e1 + " " + s + " " + e2;
    }

    @Override
    public IExpr deepCopy() {
        return new LogicalExpr(e1.deepCopy(), e2.deepCopy(),op);
    }

    @Override
    public IType typecheck(IDict<String, IType> typeEnv) throws MyException {
        IType t1 = e1.typecheck(typeEnv);
        IType t2 = e2.typecheck(typeEnv);
        if (t1.equals(new BoolType())) {
            if (t2.equals(new BoolType())) {
                return new BoolType();
            } else
                throw new MyException("second operand is not a boolean");
        } else
            throw new MyException("first operand is not a boolean");
    }
}