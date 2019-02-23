package gpd.task.task2;

import gpb.task.task2.App;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static gpb.task.models.Const.DELIMITER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AppTest {

    private final String datesStat = "stats-dates.csv";
    private final String officesStat = "stats-offices.csv";

    @Before
    public void init() throws IOException {
        App.main(datesStat, officesStat, "src/test/resources/1.csv", "src/test/resources/2.csv", "src/test/resources/3.csv");
    }

    @Test
    public void correctGroupAndSortOfficeStatsTest() throws IOException {
        assertTrue(Files.exists(Paths.get(officesStat)));
        assertEquals(9, Files.lines(Paths.get(officesStat)).count());

        List<Double> currentOfficeStats = Files.lines(Paths.get(officesStat))
                .map(line -> {
                    String[] vals = line.split(DELIMITER);
                    return Double.parseDouble(vals[1]);
                })
                .collect(Collectors.toList());

        List<Double> requiredOfficeStats = Arrays.asList(13.2, 9.9, 8.8, 8.8, 7.7, 5.5, 4.4, 3.3, 1.1);

        assertEquals(requiredOfficeStats, currentOfficeStats);
    }

    @Test
    public void correctGroupAndSortDateStatsTest() throws IOException {

        assertTrue(Files.exists(Paths.get(datesStat)));
        assertEquals(7, Files.lines(Paths.get(datesStat)).count());

        List<String> requiredDateStats = Arrays.asList("2018-01-15", "2018-02-18", "2018-08-14",
                "2018-09-06", "2018-10-22", "2018-11-01", "2018-12-17");

        List<String> currentDateStats = Files.lines(Paths.get(datesStat))
                .map(line -> line.split(DELIMITER)[0])
                .collect(Collectors.toList());

        assertEquals(requiredDateStats, currentDateStats);
    }
}
