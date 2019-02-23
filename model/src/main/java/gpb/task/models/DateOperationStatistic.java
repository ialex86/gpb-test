package gpb.task.models;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DateOperationStatistic implements OperationStatistic<LocalDate> {
    private final Map<LocalDate, Double> statistic = new ConcurrentHashMap<>();

    @Override
    public void put(Operation operation) {
        statistic.compute(operation.getOperationDate().toLocalDate(), (k, oldV) -> {
            if (oldV == null) oldV = 0.0;
            oldV += operation.getOperationSum();
            return oldV;
        });
    }

    @Override
    public Map<LocalDate, Double> getStatistic() {
        return statistic;
    }

}
