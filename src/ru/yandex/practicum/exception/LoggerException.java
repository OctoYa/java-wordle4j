package ru.yandex.practicum.exception;

public class LoggerException extends SystemException {
    public LoggerException(String message, Throwable cause) {
        super(message, cause);
    }
}
