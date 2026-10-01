package ru.yandex.practicum.model;

import ru.yandex.practicum.exception.EmptyDictionaryException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */


public class WordleDictionary {
    private final List<String> words;
    private final PrintWriter logger;

    public WordleDictionary(ArrayList<String> words, PrintWriter logger) throws EmptyDictionaryException {
        this.logger = logger;
        if (words == null || words.isEmpty()) {
            if (logger != null) {
                logger.println("Ошибка: В словаре нет подходящих слов");
            }
            throw new EmptyDictionaryException("Файл словаря пуст или не содержит подходящих слов");
        }
        this.words = words;
        if (logger != null) {
            logger.println("Словарь инициализирован, количество слов: " + words.size());
        }
    }

    public boolean contains(String word) {
        if (word == null) {
            return false;
        }
        String normalizedWord = word.trim().toLowerCase().replace('ё', 'е');
        return words.contains(normalizedWord);
    }

    public List<String> getWords() {
        return words;
    }

    public String get(int index) {
        return words.get(index);
    }

    public int size() {
        return words.size();
    }

    public String checkWord(String guessWord, String answerWord) {
        String normalizedGuess = guessWord.trim().toLowerCase().replace('ё', 'е');
        String normalizedAnswer = answerWord.trim().toLowerCase().replace('ё', 'е');

        int length = normalizedAnswer.length();
        char[] matchStatus = new char[length];

        boolean[] answerLetterUsed = new boolean[length];
        boolean[] guessLetterProcessed = new boolean[length];

        for (int i = 0; i < length; i++) {
            if (normalizedGuess.charAt(i) == normalizedAnswer.charAt(i)) {
                matchStatus[i] = '+';
                answerLetterUsed[i] = true;
                guessLetterProcessed[i] = true;
            }
        }

        for (int i = 0; i < length; i++) {
            if (guessLetterProcessed[i]) {
                continue;
            }

            char guessChar = normalizedGuess.charAt(i);
            boolean foundPartialMatch = false;

            for (int j = 0; j < length; j++) {
                if (!answerLetterUsed[j] && normalizedAnswer.charAt(j) == guessChar) {
                    matchStatus[i] = '^';
                    answerLetterUsed[j] = true;
                    foundPartialMatch = true;
                    break;
                }
            }

            if (!foundPartialMatch) {
                matchStatus[i] = '-';
            }
        }

        StringBuilder result = new StringBuilder(length);
        for (char c : matchStatus) {
            result.append(c);
        }

        return result.toString();
    }
}