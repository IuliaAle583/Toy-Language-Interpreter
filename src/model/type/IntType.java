package model.type;

import model.value.IntValue;
import model.value.IValue;

public class IntType implements IType {
    String name="int";

    @Override
    public boolean equals(Object another) {
        return another instanceof IntType;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public IValue defaultValue() {
        return new IntValue(0);
    }

    @Override
    public IType deepCopy() {
        return new IntType();
    }
}