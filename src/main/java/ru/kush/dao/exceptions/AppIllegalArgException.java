package ru.kush.dao.exceptions;

public class AppIllegalArgException extends AppException {

    public AppIllegalArgException(String message) {
        super(message);
    }

    public AppIllegalArgException(String message, Throwable cause) {
        super(message, cause);
    }
}
