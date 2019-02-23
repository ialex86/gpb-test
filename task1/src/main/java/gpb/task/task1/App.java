package gpb.task.task1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * создает текстовые файлы, содержащие дату, время, номер
 * точки продаж, номер операции и сумму операции.
 * Время должно выбираться случайно в диапазоне за предыдущий год. Т. е. если
 * программа запущенна 02.06.2018, то время операции может быть от 01.01.2017 00:00 до
 * 01.01.2018 00:00.
 * Номер точки продаж случайным образом выбирается из заранее подготовленного списка.
 * Список точек продаж хранится в текстовом файле, где на одной строке находится одна
 * точка продаж.
 * Сумма операции — случайное значение в диапазоне от 10 000,12 до 100 000,50 рублей.
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final int CAPACITY = 1_000_000;


    /**
     * @param args - offices.txt 90000 ops1.txt ops2.txt ops3.txt
     * @throws Exception
     */
    public static void main(String... args) throws Exception {
        if (args.length < 3) {
            LOGGER.error("example correct params: offices.txt 90000 ops1.txt ops2.txt ops3.txt");
            throw new RuntimeException("Exactly 3 parameters required !");
        }

        final String salePointsPath = args[0];
        final int count = Integer.valueOf(args[1]);
        final String[] files = Arrays.copyOfRange(args, 2, args.length);

        DataGenerator dataGenerator;
        dataGenerator = initDataGenerator(salePointsPath);

        process(count, files, dataGenerator);

    }

    private static void process(int count, String[] files, DataGenerator dataGenerator) throws Exception {
        final BlockingDeque<String> msgsQ = new LinkedBlockingDeque<>(CAPACITY);

        List<MessageWriter> writers = Arrays.stream(files).map(MessageWriter::new).collect(Collectors.toList());
        writers.forEach(messageWriter -> messageWriter.process(msgsQ));

        Stream.generate(dataGenerator::generateLine)
                .limit(count)
                .parallel()
                .forEach(msg -> {
                    try {
                        msgsQ.put(msg);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        while (msgsQ.size() > 0) {
            Thread.sleep(100);
        }

        for (MessageWriter writer : writers) {
            writer.close();
        }
    }

    private static DataGenerator initDataGenerator(String salePointsPath) {
        DataGenerator dataGenerator;
        try (Stream<String> stream = Files.lines(Paths.get(salePointsPath))) {
            dataGenerator = new DataGenerator(stream.collect(Collectors.toList()));
        } catch (IOException e) {
            LOGGER.error("cannot read file:{} ", salePointsPath, e);
            throw new RuntimeException("cannot read file: " + salePointsPath);
        }
        return dataGenerator;
    }
}
