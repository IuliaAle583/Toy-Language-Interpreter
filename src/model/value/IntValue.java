package model.value;

import model.type.IntType;
import model.type.IType;

public class IntValue implements IValue {
    private final int value;

    public IntValue(int value) { this.value = value; }

    public int getValue() { return value; }

    @Override
    public IType getType() { return new IntType(); }

    @Override
    public String toString() { return Integer.toString(value); }

    @Override
    public boolean equals(Object another) {
        return another instanceof IntValue other && value == other.value;
    }

    @Override
    public IValue deepCopy() {
        return new IntValue(this.value);
    }
}