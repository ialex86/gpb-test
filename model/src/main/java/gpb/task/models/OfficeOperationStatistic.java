package gpb.task.models;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OfficeOperationStatistic implements OperationStatistic<Integer> {

    private final Map<Integer, Double> statistic = new ConcurrentHashMap<>();

    @Override
    public void put(Operation operation) {
        statistic.compute(operation.getSalePoint(), (k, oldV) -> {
            if (oldV == null) oldV = 0.0D;
            oldV += operation.getOperationSum();
            return oldV;
        });
    }

    @Override
    public Map<Integer, Double> getStatistic() {
        return statistic;
    }
}
