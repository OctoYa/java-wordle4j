package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.WordleDictionary;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {
    private PrintWriter nullLogger;

    @BeforeEach
    void setUp() {
        nullLogger = new PrintWriter(Writer.nullWriter());
    }

    @Test
    void shouldCorrectlyCheckWordPresenceAndNormalization() throws Exception {
        ArrayList<String> words = new ArrayList<>(List.of("пирог", "сосна", "полет"));
        WordleDictionary dictionary = new WordleDictionary(words, nullLogger);

        assertTrue(dictionary.contains("пирог"));
        assertTrue(dictionary.contains("ПИРОГ"));
        assertTrue(dictionary.contains("полёт")); // 'ё' заменяется на 'е'
        assertFalse(dictionary.contains("трава"));
    }

    @Test
    void shouldCorrectlyEvaluateWordMatches() throws Exception {
        ArrayList<String> words = new ArrayList<>(List.of("пирог", "сосна"));
        WordleDictionary dictionary = new WordleDictionary(words, nullLogger);

        String result1 = dictionary.checkWord("парус", "пирог");
        assertEquals("+-+--", result1);

        String result2 = dictionary.checkWord("пирог", "пирог");
        assertEquals("+++++", result2);

        String result3 = dictionary.checkWord("горох", "пирог");
        assertEquals("^-++-", result3);
    }
}
