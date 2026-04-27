package model.ADT;

import javafx.util.Pair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarrierTable implements IBarrierTable {
    private Map<Integer, Pair<Integer, List<Integer>>> table = new HashMap<>();
    private int freeLocation = 1;

    @Override
    public int put(int value, List<Integer> list) {
        table.put(freeLocation, new Pair<>(value, list));
        return freeLocation++;
    }

    @Override
    public Pair<Integer, List<Integer>> get(int key) {
        return table.get(key);
    }

    @Override
    public boolean contains(int key) {
        return table.containsKey(key);
    }

    @Override
    public void update(int key, Pair<Integer, List<Integer>> value) {
        table.put(key, value);
    }

    @Override
    public Map<Integer, Pair<Integer, List<Integer>>> getContent() {
        return table;
    }

    @Override
    public void setContent(Map<Integer, Pair<Integer, List<Integer>>> content) {
        this.table = content;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Integer key : table.keySet()) {
            javafx.util.Pair<Integer, java.util.List<Integer>> entry = table.get(key);
            sb.append(key)
                    .append(" -> (")
                    .append(entry.getKey())
                    .append(", ")
                    .append(entry.getValue())
                    .append("); ");
        }
        sb.append("}");
        return sb.toString();

    }
}
