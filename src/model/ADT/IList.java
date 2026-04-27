package model.ADT;

import model.value.IValue;

import java.util.List;

public interface IList<T> {

    void add(T v);

    List<T> getAll();

    String toString();

    IValue[] getContent();
}