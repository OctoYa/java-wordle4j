package ru.yandex.practicum.exception;

public class DictionaryLoadException extends SystemException {
    public DictionaryLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
