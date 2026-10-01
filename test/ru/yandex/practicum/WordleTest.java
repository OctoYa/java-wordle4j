package ru.yandex.practicum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.WordleDictionary;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private PrintWriter nullLogger;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        nullLogger = new PrintWriter(Writer.nullWriter());
    }

    @AfterEach
    void restore() {
        System.setOut(originalOut);
    }

    @Test
    void testPrintMainMenu() {
        Wordle.printMainMenu();
        String output = outContent.toString();

        assertTrue(output.contains("1 - Новая игра"));
        assertTrue(output.contains("2 - Обновить словарь"));
        assertTrue(output.contains("3 - Выход"));
    }

    @Test
    void testPrintGameMenu() {
        Wordle.printGameMenu(5, 6);
        String output = outContent.toString();

        assertTrue(output.contains("Загадано слово из 5 букв."));
        assertTrue(output.contains("Осталось попыток: 6"));
        assertTrue(output.contains("Введите слово (или нажмите Enter для авто-подсказки):"));
    }

    @Test
    void testNewGameWinningFlowWithDirectWordInput() throws Exception {
        ArrayList<String> words = new ArrayList<>(List.of("пирог"));
        WordleDictionary dictionary = new WordleDictionary(words, nullLogger);

        // Прямой ввод слова "пирог" без промежуточных пунктов меню
        Scanner scanner = new Scanner("пирог\n");

        Wordle.newGame(dictionary, 5, 6, nullLogger, scanner);

        String output = outContent.toString();
        assertTrue(output.contains("Результат: +++++"));
        assertTrue(output.contains("Вы выиграли!"));
        assertTrue(output.contains("Загаданное слово было: пирог"));
    }
}
