package gpb.task.models;

import static gpb.task.models.Const.DELIMITER;

public class StatisticEntry<K> {
    private final K key;
    private final double val;

    public StatisticEntry(K key, double val) {
        this.key = key;
        this.val = Math.round(val * 100.0) / 100.0D;
    }

    public String getLine() {
        return key + DELIMITER + val + System.lineSeparator();
    }

    @Override
    public String toString() {
        return "StatisticEntry{" +
                "key=" + key +
                ", val=" + val +
                '}';
    }
}
