package ru.yandex.practicum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.WordleDictionary;
import ru.yandex.practicum.service.WordleDictionaryLoader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryLoaderTest {
    private PrintWriter nullLogger;
    private final Path tempTestDir = Path.of("temptestdir");

    @BeforeEach
    void setUp() throws Exception {
        nullLogger = new PrintWriter(Writer.nullWriter());
        Files.createDirectories(tempTestDir);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(tempTestDir)) {
            deleteDirectory(tempTestDir.toFile());
        }
    }

    private void deleteDirectory(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDirectory(f);
            }
        }
        file.delete();
    }

    @Test
    void shouldLoadAndFilterWordsByLength() throws Exception {
        Path file = tempTestDir.resolve("dict.txt");
        Files.write(file, List.of("пирог", "сосна", "кот", "длинноеслово", "с лово"));

        WordleDictionary dictionary = WordleDictionaryLoader.load(nullLogger, file, 5);

        assertEquals(2, dictionary.size());
        assertTrue(dictionary.contains("пирог"));
        assertTrue(dictionary.contains("сосна"));
    }


    @Test
    void shouldNormalizeWordsToLowerCaseAndReplaceYo() throws Exception {
        Path file = tempTestDir.resolve("dict.txt");
        Files.write(file, List.of("ПИРОГ", "ПОЛЁТ"));

        WordleDictionary dictionary = WordleDictionaryLoader.load(nullLogger, file, 5);

        assertEquals("пирог", dictionary.get(0));
        assertEquals("полет", dictionary.get(1));
    }
}
