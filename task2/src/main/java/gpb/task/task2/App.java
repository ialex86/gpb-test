package gpb.task.task2;

import gpb.task.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Collections.reverseOrder;

/**
 * считает статистику по операциям.
 * Данные об операциях находятся в файлах, которые сгенерированы в предыдущей
 * задаче.
 * Программа должна подсчитать сумму всех операций из всех входных файлов за каждый
 * день и суммы всех операций в каждой точке продаж.
 * Программе в качестве параметров передаются имя файла со статистикой по датам, имя
 * файла со статистикой по точкам продаж и имена файлов с операциями.
 * Статистика по датам должна быть отсортирована по возрастанию дат.
 * Статистика по точкам продаж должна быть отсортирована по убыванию суммы.
 */
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    /**
     * @param args - stats-dates.txt stats-offices.txt ops1.txt ops2.txt ops3.txt
     */
    public static void main(String... args) throws IOException {
        if (args.length < 3) {
            LOGGER.error("example correct params: stats-dates.txt stats-offices.txt ops1.txt ops2.txt ops3.txt");
            throw new RuntimeException("Exactly 3 parameters required !");
        }

        final Path datesStatPath = Paths.get(args[0]);
        final Path officesStatPath = Paths.get(args[1]);
        final String[] files = Arrays.copyOfRange(args, 2, args.length);

        Files.deleteIfExists(officesStatPath);
        Files.deleteIfExists(datesStatPath);

        OperationStatistic<Integer> officeStatistic = new OfficeOperationStatistic();
        OperationStatistic<LocalDate> dateStatistic = new DateOperationStatistic();

        Arrays.stream(files)
                .map(Paths::get)
                .flatMap(patch -> {
                    try {
                        return Files.lines(patch);
                    } catch (IOException e) {
                        return Stream.empty();
                    }
                })
                .parallel()
                .map(Operation::new)
                .forEach(operation -> {
                    officeStatistic.put(operation);
                    dateStatistic.put(operation);
                });

        officeStatistic.getStatistic().entrySet().stream()
                .sorted(reverseOrder(Map.Entry.comparingByValue()))
                .map(entry -> new StatisticEntry<>(entry.getKey(), entry.getValue()))
                .forEach(entry -> App.write(officesStatPath, entry));

        dateStatistic.getStatistic().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new StatisticEntry<>(entry.getKey(), entry.getValue()))
                .forEach(entry -> App.write(datesStatPath, entry));
    }

    private static void write(Path path, StatisticEntry entry) {
        try {
            Files.write(path, entry.getLine().getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            LOGGER.error("write exception: {}", e);
            throw new RuntimeException(e);
        }
    }
}