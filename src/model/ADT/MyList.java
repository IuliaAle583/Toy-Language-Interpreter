package model.ADT;

import model.value.IValue;

import java.util.ArrayList;
import java.util.Collection;

public class MyList<T> implements model.ADT.IList<T> {
    private final java.util.List<T> list;

    public MyList() {
        this.list = new ArrayList<>();
    }

    @Override
    public void add(T v) {
        list.add(v);
    }

    @Override
    public java.util.List<T> getAll() {
        return list;
    }

    @Override
    public String toString() {
        return list.toString();
    }

    @Override
    public IValue[] getContent() {
        return list.toArray(new IValue[0]);
    }
}