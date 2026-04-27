package model.type;

import model.value.BoolValue;
import model.value.IValue;

public class BoolType implements IType {
    String name="bool";

    @Override
    public boolean equals(Object another) {
        return another instanceof BoolType;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public IValue defaultValue() {
        return new BoolValue(false);
    }

    @Override
    public IType deepCopy() {
        return new BoolType();
    }
}