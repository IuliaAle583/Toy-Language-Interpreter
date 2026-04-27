package model.ADT;
import java.util.Map;
import java.util.List;
import javafx.util.Pair;

public interface IBarrierTable {
    int put(int value, List<Integer> list);
    Pair<Integer, List<Integer>> get(int key);
    boolean contains(int key);
    void update(int key, Pair<Integer, List<Integer>> value);
    Map<Integer, Pair<Integer, List<Integer>>> getContent();
    void setContent(Map<Integer, Pair<Integer, List<Integer>>> content);
}