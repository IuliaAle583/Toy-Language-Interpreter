package model.ADT;

import model.exceptions.ADTException;

import java.util.Map;

public interface IDict<K, V> {

    void put(K key, V value);

    boolean isDefined(K key);

    V lookup(K key) throws ADTException;

    void update(K key, V value) throws ADTException;

    Map<K, V> getContent();

    String toString();

    void remove(K key) throws ADTException;

    IDict<K, V> deepCopy();
}

