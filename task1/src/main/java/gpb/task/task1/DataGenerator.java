package gpb.task.task1;

import gpb.task.models.Operation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class DataGenerator {

    private static final double MIN_SUM = 10_000.12D;
    private static final double MAX_SUM = 100_000.50D;

    private final List<String> salePoints;

    private final long secondsOfYear;

    private final LocalDateTime startDate;
    private final AtomicInteger operationNum = new AtomicInteger();

    public DataGenerator(List<String> salePoints) {
        this.salePoints = salePoints;

        int year = LocalDate.now().getYear() - 1;
        startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        secondsOfYear = LocalDateTime.of(year, 12, 31, 0, 0).getDayOfYear() * 24 * 60 * 60 - 1;

    }

    public String generateLine() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        LocalDateTime operationDate = startDate.plusSeconds(random.nextLong(secondsOfYear));
        String salePoint = salePoints.get(random.nextInt(salePoints.size()));
        int currentOperationNum = operationNum.incrementAndGet();
        double operationSum = random.nextDouble(MIN_SUM, MAX_SUM);

        return new Operation(operationDate, salePoint, currentOperationNum, operationSum).getLine();
    }

}
