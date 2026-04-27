package model.ADT;

import model.exceptions.ADTException;

public interface IStack<T> {

    T pop() throws ADTException;

    void push(T v);

    T peek() throws ADTException;

    boolean isEmpty();

    String toString();

    java.util.List<T> toListTopToBottom();

}