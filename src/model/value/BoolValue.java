package model.value;

import model.type.BoolType;
import model.type.IType;

public class BoolValue implements IValue {
    private final boolean value;

    public BoolValue(boolean value) { this.value = value; }

    public boolean getValue() { return value; }

    @Override
    public IType getType() { return new BoolType(); }

    @Override
    public String toString() { return Boolean.toString(value); }

    @Override
    public boolean equals(Object another) {
        return another instanceof BoolValue other && value == other.value;
    }

    @Override
    public IValue deepCopy() {
        return new BoolValue(this.value);
    }
}