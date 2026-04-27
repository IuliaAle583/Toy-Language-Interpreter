package model.expression;

import model.ADT.IDict;
import model.ADT.IHeap;
import model.exceptions.ExpressionException;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.IValue;
import model.ADT.IDict;
import model.exceptions.MyException;
import model.type.IType;
import model.type.BoolType;

public class RelExpr implements IExpr {
    private final IExpr e1, e2;
    private final int op;

    public RelExpr(IExpr e1, IExpr e2, int op) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    @Override
    public IValue eval(IDict<String, IValue> tbl,
                       IHeap<Integer, IValue> heap) throws ExpressionException {
        IValue v1 = e1.eval(tbl, heap);
        IValue v2 = e2.eval(tbl, heap);

        if (!v1.getType().equals(new IntType()) || !v2.getType().equals(new IntType()))
            throw new ExpressionException("relational operands must be int");

        int n1 = ((IntValue) v1).getValue();
        int n2 = ((IntValue) v2).getValue();

        return switch (op) {
            case 1 -> new BoolValue(n1 < n2);
            case 2 -> new BoolValue(n1 <= n2);
            case 3 -> new BoolValue(n1 == n2);
            case 4 -> new BoolValue(n1 != n2);
            case 5 -> new BoolValue(n1 > n2);
            case 6 -> new BoolValue(n1 >= n2);
            default -> throw new ExpressionException("invalid relational op");
        };
    }

    @Override
    public String toString() {
        String s = switch (op) {
            case 1 -> "<";
            case 2 -> "<=";
            case 3 -> "==";
            case 4 -> "!=";
            case 5 -> ">";
            case 6 -> ">=";
            default -> "?";
        };
        return e1 + " " + s + " " + e2;
    }

    @Override
    public IExpr deepCopy() {
        return new RelExpr(e1.deepCopy(), e2.deepCopy(), op);
    }

    @Override
    public IType typecheck(IDict<String, IType> typeEnv) throws MyException {
        IType t1 = e1.typecheck(typeEnv);
        IType t2 = e2.typecheck(typeEnv);
        if (t1.equals(new IntType())) {
            if (t2.equals(new IntType())) {
                return new BoolType();
            } else
                throw new MyException("second operand is not an integer");
        } else
            throw new MyException("first operand is not an integer");
    }
}