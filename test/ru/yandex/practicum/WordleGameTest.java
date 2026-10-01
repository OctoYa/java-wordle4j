package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.WordleDictionary;
import ru.yandex.practicum.service.WordleGame;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {
    private PrintWriter nullLogger;
    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() throws Exception {
        nullLogger = new PrintWriter(Writer.nullWriter());
        ArrayList<String> words = new ArrayList<>(List.of("пирог", "сосна", "полет"));
        dictionary = new WordleDictionary(words, nullLogger);
    }

    @Test
    void shouldInitializeGameAndExposeAnswer() {
        WordleGame game = new WordleGame(nullLogger, dictionary);
        assertNotNull(game.getAnswer());
        assertTrue(dictionary.contains(game.getAnswer()));
        assertEquals(0, game.getSteps());
        assertFalse(game.isWinner());
    }

    @Test
    void shouldIncrementStepsAndDetectWin() throws Exception {
        WordleGame game = new WordleGame(nullLogger, dictionary);
        String answer = game.getAnswer();

        String result = game.checkWord(answer, true);

        assertEquals("+++++", result);
        assertEquals(1, game.getSteps());
        assertTrue(game.isWinner());
    }

    @Test
    void shouldNormalizeYoLettersInUserGuess() throws Exception {
        ArrayList<String> words = new ArrayList<>(List.of("полет"));
        WordleDictionary singleDict = new WordleDictionary(words, nullLogger);
        WordleGame game = new WordleGame(nullLogger, singleDict);
        String result = game.checkWord("полёт", true);

        assertEquals("+++++", result);
        assertTrue(game.isWinner());
    }

    @Test
    void shouldReturnValidHintFromDictionary() {
        WordleGame game = new WordleGame(nullLogger, dictionary);
        String hint = game.getHint();

        assertNotNull(hint);
        assertTrue(dictionary.contains(hint));
    }
}
