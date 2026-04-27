package model.ADT;

import model.exceptions.ADTException;
import model.value.IValue;

import java.util.HashMap;
import java.util.Map;

public class MyDict<K, V> implements model.ADT.IDict<K, V> {
    private  Map<K, V> map;

    public MyDict() {
        this.map = new HashMap<>();
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public V lookup(K key) throws ADTException {
        if (!map.containsKey(key)) {
            throw new ADTException("Variable " + key + " is not defined");
        }
        return map.get(key);
    }

    @Override
    public void update(K key, V value) throws ADTException {
        if (!map.containsKey(key)) {
            throw new ADTException("Variable " + key + " is not defined");
        }
        map.put(key, value);
    }

    @Override
    public Map<K, V> getContent() {
        return map;
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public void remove(K key) throws ADTException {
        if (!map.containsKey(key))
            throw new ADTException("Key " + key + " not found for remove");
        map.remove(key);
    }

    @Override
    public IDict<K, V> deepCopy() {
        MyDict<K, V> copy = new MyDict<>();
        copy.map = new HashMap<>(map);
        return copy;
    }
}