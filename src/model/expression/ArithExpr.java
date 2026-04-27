package model.expression;

import model.ADT.IDict;
import model.ADT.IHeap;
import model.exceptions.ExpressionException;
import model.type.IntType;
import model.value.IntValue;
import model.value.IValue;
import model.ADT.IDict;
import model.exceptions.MyException;
import model.type.IType;

public class ArithExpr implements IExpr {
    private final IExpr e1, e2;
    private final int op;

    public ArithExpr(IExpr e1, IExpr e2, int op) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    @Override
    public IValue eval(IDict<String, IValue> tbl,
                       IHeap<Integer, IValue> heap) throws ExpressionException {
        IValue v1 = e1.eval(tbl, heap);
        if (!v1.getType().equals(new IntType()))
            throw new ExpressionException("first operand is not int");

        IValue v2 = e2.eval(tbl, heap);
        if (!v2.getType().equals(new IntType()))
            throw new ExpressionException("second operand is not int");

        int n1 = ((IntValue) v1).getValue();
        int n2 = ((IntValue) v2).getValue();

        return switch (op) {
            case 1 -> new IntValue(n1 + n2);
            case 2 -> new IntValue(n1 - n2);
            case 3 -> new IntValue(n1 * n2);
            case 4 -> {
                if (n2 == 0) throw new ExpressionException("division by zero");
                yield new IntValue(n1 / n2);
            }
            default -> throw new ExpressionException("invalid arithmetic op");
        };
    }

    @Override
    public String toString() {
        String symbol = switch (op) {
            case 1 -> "+";
            case 2 -> "-";
            case 3 -> "*";
            case 4 -> "/";
            default -> "?";
        };
        return e1 + " " + symbol + " " + e2;
    }

    @Override
    public IExpr deepCopy() {
        return new ArithExpr(e1.deepCopy(), e2.deepCopy(),op);
    }

    @Override
    public IType typecheck(IDict<String, IType> typeEnv) throws MyException {
        IType typ1 = e1.typecheck(typeEnv);
        IType typ2 = e2.typecheck(typeEnv);
        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new IntType();
            } else
                throw new MyException("second operand is not an integer");
        } else
            throw new MyException("first operand is not an integer");
    }
}