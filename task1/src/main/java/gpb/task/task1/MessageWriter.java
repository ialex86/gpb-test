package gpb.task.task1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class MessageWriter implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageWriter.class);

    private static final int BATCH_SIZE = 10_000;
    private final BufferedWriter writeBuffer;
    private int currentCount = 0;
    private ExecutorService executor;


    public MessageWriter(String outFilePath) {
        try {
            writeBuffer = Files.newBufferedWriter(Paths.get(outFilePath),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("cannot create file:{} exception: {}", outFilePath, e);
            throw new RuntimeException("cannot create file: " + outFilePath);
        }
    }

    public void process(BlockingQueue<String> msgsQ) {
        executor = Executors.newFixedThreadPool(1);
        executor.execute(() -> Stream.generate(() -> {
                    try {
                        return msgsQ.take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).forEach(msg -> {
                    try {
                        writeBuffer.write(msg);

                        currentCount++;
                        if (currentCount % BATCH_SIZE == 0) {
                            writeBuffer.flush();
                        }

                        if (Thread.interrupted()) {
                            throw new RuntimeException(new InterruptedException());
                        }

                    } catch (IOException e) {
                        LOGGER.error("write exception: {}", e);
                        throw new RuntimeException(e);
                    }
                })
        );
    }

    @Override
    public void close() throws Exception {
        executor.shutdown();
        writeBuffer.flush();
        writeBuffer.close();
    }
}
