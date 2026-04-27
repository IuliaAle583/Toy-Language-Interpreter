package model.value;

import model.type.RefType;
import model.type.IType;

public class RefValue implements IValue {
    private final int address;
    private final IType locationType;

    public RefValue(int address, IType locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress() { return address; }

    public IType getLocationType() { return locationType; }

    @Override
    public IType getType() { return new RefType(locationType); }

    @Override
    public String toString() { return "(" + address + "," + locationType + ")"; }

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof RefValue other)) return false;
        return address == other.address && locationType.equals(other.locationType);
    }

    @Override
    public IValue deepCopy() {
        return new RefValue(address, locationType);
    }
}