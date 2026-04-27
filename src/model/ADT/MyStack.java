package model.ADT;

import model.exceptions.ADTException;

import java.util.ArrayDeque;
import java.util.Deque;

public class MyStack<T> implements model.ADT.IStack<T> {
    private final Deque<T> stack;

    public MyStack() {
        this.stack = new ArrayDeque<>();
    }

    @Override
    public T pop() throws ADTException {
        if (stack.isEmpty())
            throw new ADTException("Cannot pop from empty stack");
        return stack.pop();
    }

    @Override
    public void push(T v) {
        stack.push(v);
    }

    @Override
    public T peek() throws ADTException {
        if (stack.isEmpty()) {
            throw new ADTException("Stack is empty");
        }
        return stack.peek();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public String toString() {
        return stack.toString();
    }

    @Override
    public java.util.List<T> toListTopToBottom() {
        return new java.util.ArrayList<>(stack);
    }
}