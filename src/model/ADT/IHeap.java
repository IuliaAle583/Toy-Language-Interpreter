package model.ADT;

import model.exceptions.ADTException;
import model.value.IValue;

import java.util.Map;

public interface IHeap<K, V extends IValue> {
    int allocate(V value);

    V lookup(K address) throws ADTException;

    void update(K address, V value) throws ADTException;

    boolean isDefined(K address);

    Map<K, V> getContent();

    void setContent(Map<K, V> newContent);


}