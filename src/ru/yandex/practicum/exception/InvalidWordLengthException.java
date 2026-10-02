package ru.yandex.practicum.exception;

public class InvalidWordLengthException extends GameException {
    public InvalidWordLengthException(int expected, int actual) {
        super("Длина слова должна быть " + expected + " букв (вы ввели: " + actual + ")");
    }
}
