package model.ADT;

import model.exceptions.ADTException;
import model.value.IValue;

import java.util.HashMap;
import java.util.Map;

public class MyHeap<V extends IValue> implements model.ADT.IHeap<Integer, V> {
    private final Map<Integer, V> content;
    private int nextFree;

    public MyHeap() {
        this.content = new HashMap<>();
        this.nextFree = 1;
    }

    @Override
    public int allocate(V value) {
        int addr = nextFree;
        content.put(addr, value);
        nextFree++;
        return addr;
    }

    @Override
    public V lookup(Integer address) throws ADTException {
        if (!content.containsKey(address)) {
            throw new ADTException("Heap: address " + address + " is not defined");
        }
        return content.get(address);
    }

    @Override
    public void update(Integer address, V value) throws ADTException {
        if (!content.containsKey(address)) {
            throw new ADTException("Heap: cannot update undefined address " + address);
        }
        content.put(address, value);
    }

    @Override
    public boolean isDefined(Integer address) {
        return content.containsKey(address);
    }

    @Override
    public Map<Integer, V> getContent() {
        return content;
    }

    @Override
    public void setContent(Map<Integer, V> newContent) {
        content.clear();
        content.putAll(newContent);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var e : content.entrySet()) {
            sb.append(e.getKey()).append(" -> ").append(e.getValue()).append("\n");
        }
        return sb.toString();
    }
}