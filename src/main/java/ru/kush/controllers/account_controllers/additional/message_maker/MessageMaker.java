package ru.kush.controllers.account_controllers.additional.message_maker;

import java.util.List;

public interface MessageMaker {
    void appendToSuccess(String message);
    List<String> getSuccessMessages();

    void appendToError(String message);
    List<String> getErrorMessages();

    List<String> getAllMessages();
}
