package ru.yandex.practicum.util;

import ru.yandex.practicum.exception.LoggerException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Logger {
    private final Path logPath;
    private final PrintWriter writer;

    public Logger() throws LoggerException {
        try {
            this.logPath = createLogFile();
            this.writer = new PrintWriter(
                    Files.newBufferedWriter(logPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND),
                    true
            );
        } catch (IOException e) {
            throw new LoggerException("Не удалось создать лог-файл", e);
        }
    }

    private Path createLogFile() throws IOException {
        Path folder = Path.of("log");
        Files.createDirectories(folder);

        Path targetPath = folder.resolve("GameLog_1.txt");
        String pathTemplate = "GameLog_%d.txt";
        int index = 2;

        while (Files.exists(targetPath)) {
            targetPath = folder.resolve(String.format(pathTemplate, index));
            index++;
        }

        Files.createFile(targetPath);
        return targetPath;
    }

    public void writeLog(String message) {
        if (writer != null) {
            writer.println(message);
        }
    }

    public PrintWriter getPrintWriter() {
        return writer;
    }

    public Path getLogPath() {
        return logPath;
    }

    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
}