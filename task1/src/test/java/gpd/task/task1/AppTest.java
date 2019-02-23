package gpd.task.task1;

import gpb.task.models.Operation;
import gpb.task.task1.App;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AppTest {

    private static final int OPERATION_COUNT = 15_000;
    private final int currentYear = LocalDate.now().getYear();

    private Set<Integer> requiredSalePoints;
    private Stream<Operation> result;

    @Before
    public void init() throws Exception {
        final String csv1 = "1.csv";
        final String csv2 = "2.csv";
        final String csv3 = "3.csv";

        final String salePointsFilePath = "src/test/resources/sale_points";
        App.main(salePointsFilePath, String.valueOf(OPERATION_COUNT), csv1, csv2, csv3);
        requiredSalePoints = Files.lines(Paths.get(salePointsFilePath))
                .map(Integer::parseInt).collect(Collectors.toSet());

        result = Stream.of(Files.lines(Paths.get(csv1)), Files.lines(Paths.get(csv2)), Files.lines(Paths.get(csv3)))
                .flatMap(stream -> stream).map(Operation::new);
    }

    @Test
    public void generator() {

    }

    @Test
    public void uniqueOperationNumTest() {
        Set<Integer> currentOpNums = result.map(Operation::getCurrentOperationNum).collect(Collectors.toSet());
        assertEquals(currentOpNums.size(), OPERATION_COUNT);
    }

    @Test
    public void usedAllSailPointTest() {
        Set<Integer> currentSalePoints = result.map(Operation::getSalePoint).collect(Collectors.toSet());
        assertEquals(currentSalePoints, requiredSalePoints);
    }

    @Test
    public void allDateCorrectlyTest() {
        result.forEach(operation ->
                assertTrue(operation.getOperationDate().getYear() < currentYear));
    }

    @Test
    public void allSumCorrectlyTest() {
        result.forEach(operation ->
                assertTrue(operation.getOperationSum() > 10_000.12 && operation.getOperationSum() < 100_000.50));
    }

    @Test
    public void countCorrectlyTest() {
        assertEquals(result.count(), OPERATION_COUNT);
    }

}
