package gpb.task.models;

import java.util.Map;

public interface OperationStatistic<K> {

    void put(Operation operation);

    Map<K, Double> getStatistic();
}
