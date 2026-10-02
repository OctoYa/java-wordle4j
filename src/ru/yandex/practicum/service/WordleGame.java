package ru.yandex.practicum.service;

import ru.yandex.practicum.exception.GameException;
import ru.yandex.practicum.exception.InvalidWordLengthException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;
import ru.yandex.practicum.model.WordleDictionary;

import java.io.PrintWriter;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */

public class WordleGame {
    private final Random random = new Random();
    private final PrintWriter logger;
    private final WordleDictionary dictionary;
    private final String answer;
    private int steps = 0;
    private final Map<String, String> history = new LinkedHashMap<>();
    private boolean isWinner = false;

    public WordleGame(PrintWriter logger, WordleDictionary dictionary) {
        this.logger = logger;
        this.dictionary = dictionary;
        this.answer = dictionary.get(random.nextInt(dictionary.size()));
        log("Класс игры инициализирован");
    }

    public String checkWord(String word, boolean isAnswer) throws GameException {
        if (word == null || word.isBlank()) {
            throw new GameException("Введено пустое слово!");
        }

        String processedWord = word.trim().toLowerCase().replace('ё', 'е');

        if (!processedWord.matches("^[а-я]+$")) {
            throw new GameException("Слово должно состоять только из кириллических букв!");
        }

        if (processedWord.length() != answer.length()) {
            throw new InvalidWordLengthException(answer.length(), processedWord.length());
        }

        if (!dictionary.contains(processedWord)) {
            throw new WordNotFoundInDictionaryException(processedWord);
        }

        if (isAnswer) {
            steps++;
        }
        if (processedWord.equals(answer)) {
            isWinner = true;
        }

        String result = dictionary.checkWord(processedWord, answer);
        history.put(processedWord, result);
        log("Результат проверки слова: " + processedWord + " -> " + result);
        return result;
    }

    public String getHint() {
        log("Запрошена подсказка");

        if (history.isEmpty()) {
            return dictionary.get(random.nextInt(dictionary.size()));
        }

        List<String> candidates = new ArrayList<>(dictionary.getWords());
        List<String> filteredCandidates = new ArrayList<>();

        for (String candidate : candidates) {
            boolean isPossibleAnswer = true;

            for (Map.Entry<String, String> entry : history.entrySet()) {
                String pastGuess = entry.getKey();
                String pastResult = entry.getValue();

                String simulatedResult = dictionary.checkWord(pastGuess, candidate);

                if (!simulatedResult.equals(pastResult)) {
                    isPossibleAnswer = false;
                    break;
                }
            }

            if (isPossibleAnswer) {
                filteredCandidates.add(candidate);
            }
        }

        if (filteredCandidates.isEmpty()) {
            return dictionary.get(random.nextInt(dictionary.size()));
        }

        String hint = filteredCandidates.get(random.nextInt(filteredCandidates.size()));
        log("Подобрана подсказка: " + hint);
        return hint;
    }

    public String checkHint(String hint) throws GameException {
        return hint + " " + checkWord(hint, false);
    }

    public int getSteps() {
        return steps;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public String getAnswer() {
        return answer;
    }

    private void log(String message) {
        if (logger != null) {
            logger.println(message);
        }
    }
}
