package ru.yandex.practicum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.util.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {
    private final Path logDir = Path.of("log");

    @BeforeEach
    void setUp() throws IOException {
        cleanLogDir();
    }

    @AfterEach
    void tearDown() throws IOException {
        cleanLogDir();
    }

    private void cleanLogDir() throws IOException {
        if (Files.exists(logDir)) {
            try (var stream = Files.walk(logDir)) {
                stream.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
    }

    @Test
    void shouldCreateLogFileAndWriteMessage() throws Exception {
        Logger logger = new Logger();
        assertNotNull(logger.getPrintWriter());
        assertNotNull(logger.getLogPath());
        assertTrue(Files.exists(logger.getLogPath()));

        logger.writeLog("Тестовая запись");
        logger.close();

        String content = Files.readString(logger.getLogPath());
        assertTrue(content.contains("Тестовая запись"));
    }

    @Test
    void shouldCreateUniqueLogFileNames() throws Exception {
        Logger logger1 = new Logger();
        Logger logger2 = new Logger();

        assertNotEquals(logger1.getLogPath(), logger2.getLogPath());

        logger1.close();
        logger2.close();
    }
}