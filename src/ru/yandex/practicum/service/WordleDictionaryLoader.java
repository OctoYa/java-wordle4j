package ru.yandex.practicum.service;

import ru.yandex.practicum.exception.DictionaryLoadException;
import ru.yandex.practicum.exception.EmptyDictionaryException;
import ru.yandex.practicum.model.WordleDictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */


public final class WordleDictionaryLoader {

    public static WordleDictionary load(PrintWriter logger, Path path, int wordLength)
            throws DictionaryLoadException, EmptyDictionaryException {

        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String processedLine = line.trim().toLowerCase().replace('ё', 'е');
                if (processedLine.length() == wordLength && !processedLine.contains(" ")) {
                    lines.add(processedLine);
                }
            }
        } catch (IOException e) {
            if (logger != null) {
                logger.println("Не удалось загрузить файл словаря: " + e.getMessage());
            }
            throw new DictionaryLoadException("Ошибка чтения файла словаря: " + path, e);
        }

        return new WordleDictionary(lines, logger);
    }
}

