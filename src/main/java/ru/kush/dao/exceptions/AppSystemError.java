package ru.kush.dao.exceptions;

public class AppSystemError extends AppException {

    public AppSystemError(String message) {
        super(message);
    }

    public AppSystemError(String message, Throwable cause) {
        super(message, cause);
    }
}
