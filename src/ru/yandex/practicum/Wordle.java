package ru.yandex.practicum;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */

import ru.yandex.practicum.exception.GameException;
import ru.yandex.practicum.exception.SystemException;
import ru.yandex.practicum.model.WordleDictionary;
import ru.yandex.practicum.service.WordleDictionaryLoader;
import ru.yandex.practicum.service.WordleGame;
import ru.yandex.practicum.util.Logger;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        Logger loggerInstance = null;
        PrintWriter logger = null;

        try {
            loggerInstance = new Logger();
            logger = loggerInstance.getPrintWriter();

            Scanner scanner = new Scanner(System.in);
            final Path dictionaryPath = Paths.get("words_ru.txt");
            final int WORD_LENGTH = 5;
            final int MAX_STEPS = 6;

            WordleDictionary dictionary = WordleDictionaryLoader.load(logger, dictionaryPath, WORD_LENGTH);

            while (true) {
                printMainMenu();
                String input = scanner.nextLine();
                int choice;
                try {
                    choice = Integer.parseInt(input.trim());
                } catch (NumberFormatException e) {
                    System.out.println("Пожалуйста, введите число из пунктов меню.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        newGame(dictionary, WORD_LENGTH, MAX_STEPS, logger, scanner);
                        break;
                    case 2:
                        dictionary = WordleDictionaryLoader.load(logger, dictionaryPath, WORD_LENGTH);
                        System.out.println("Словарь успешно обновлён!");
                        break;
                    case 3:
                        log(logger, "Завершение программы пользователем");
                        return;
                    default:
                        System.out.println("Неизвестная команда.");
                        break;
                }
            }
        } catch (SystemException e) {
            String systemMessage = String.format("Системная ошибка работы приложения: %s", e.getMessage());
            String logSystemMessage = String.format("Системная ошибка: %s", e.getMessage());

            System.err.println(systemMessage);
            log(logger, logSystemMessage);

        } catch (Exception e) {
            String unexpectedMessage = String.format("Произошла непредвиденная ошибка: %s", e.getMessage());
            String logUnexpectedMessage = String.format("Непредвиденное исключение в main: %s", e.toString());

            System.err.println(unexpectedMessage);
            log(logger, logUnexpectedMessage);

        } finally {
            if (loggerInstance != null) {
                loggerInstance.close();
            }
        }
    }

    public static void printMainMenu() {
        String nl = System.lineSeparator();
        System.out.print(
                nl + "1 - Новая игра" +
                nl + "2 - Обновить словарь" +
                nl + "3 - Выход" +
                nl +
                nl + "Введите число соответственно пункту меню: "
        );
    }

    public static void newGame(WordleDictionary dictionary, int wordLength, int maxSteps, PrintWriter logger, Scanner scanner) throws GameException {
        WordleGame game = new WordleGame(logger, dictionary);

        while (true) {
            int stepsLeft = maxSteps - game.getSteps();
            if (game.isWinner()) {
                System.out.println("\nВы выиграли!");
                System.out.println("Загаданное слово было: " + game.getAnswer());
                log(logger, "Игра завершена победой игрока. Слово: " + game.getAnswer());
                return;
            } else if (game.getSteps() >= maxSteps) {
                System.out.println("\nДостигнуто максимальное количество шагов, вы проиграли!");
                System.out.println("Загаданное слово было: " + game.getAnswer());
                log(logger, "Игра завершена поражением по количеству шагов. Слово: " + game.getAnswer());
                return;
            }

            printGameMenu(wordLength, stepsLeft);
            String input = scanner.nextLine();

            if (input.trim().isEmpty()) {
                System.out.println("Компьютер предлагает подсказку: " + game.checkHint(game.getHint()));
            } else {
                processWordMove(game, input, logger);
            }
        }
    }



    private static void processWordMove(WordleGame game, String word, PrintWriter logger) {
        try {
            String result = game.checkWord(word, true);
            System.out.println("Результат: " + result);
        } catch (GameException e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
            log(logger, "Игровое исключение: " + e.getMessage());
        }
    }

    public static void printGameMenu(int wordLength, int stepsLeft) {
        System.out.printf("""
                \nЗагадано слово из %d букв.
                Осталось попыток: %d
                Введите слово (или нажмите Enter для авто-подсказки):\s""", wordLength, stepsLeft);
    }

    private static void log(PrintWriter logger, String message) {
        if (logger != null) {
            logger.println(message);
        }
    }
}