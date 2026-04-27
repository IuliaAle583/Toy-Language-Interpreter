package model.value;

import model.type.StringType;
import model.type.IType;

public class StringValue implements IValue {
    private final String value;

    public StringValue(String value) { this.value = value; }

    public String getVal() { return value; }

    @Override
    public IType getType() { return new StringType(); }

    @Override
    public String toString() { return value; }

    @Override
    public boolean equals(Object another) {
        return another instanceof StringValue other && value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public IValue deepCopy() {
        return new StringValue(value);
    }
}