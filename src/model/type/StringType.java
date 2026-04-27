package model.type;

import model.value.StringValue;
import model.value.IValue;

public class StringType implements IType {
    String name="string";

    @Override
    public boolean equals(Object another) {
        return another instanceof StringType;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public IValue defaultValue() {
        return new StringValue("EMPTY-STRING");
    }

    @Override
    public IType deepCopy() {
        return new StringType();
    }
}