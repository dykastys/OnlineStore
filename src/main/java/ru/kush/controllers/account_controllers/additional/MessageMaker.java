package ru.kush.controllers.account_controllers.additional;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
public class MessageMaker {

    private List<String> success = new ArrayList<>();
    private List<String> error = new ArrayList<>();

    public void appendToSuccess(String message) {
        success.add(message);
    }

    public List<String> getSuccessMessages() {
        List<String> res = success;
        success = new ArrayList<>();
        return res;
    }

    public void appendToError(String message) {
        error.add(message);
    }

    public List<String> getErrorMessages() {
        List<String> res = error;
        error = new ArrayList<>();
        return res;
    }

    public List<String> getAllMessages() {
        List<String> res = getSuccessMessages();
        res.addAll(getErrorMessages());
        return res;
    }
}
