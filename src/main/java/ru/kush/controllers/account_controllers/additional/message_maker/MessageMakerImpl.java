package ru.kush.controllers.account_controllers.additional.message_maker;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class MessageMakerImpl implements MessageMaker {

    private List<String> success = new ArrayList<>();
    private List<String> error = new ArrayList<>();

    @Override
    public void appendToSuccess(String message) {
        success.add(message);
    }

    @Override
    public List<String> getSuccessMessages() {
        List<String> res = success;
        success = new ArrayList<>();
        return res;
    }

    @Override
    public void appendToError(String message) {
        error.add(message);
    }

    @Override
    public List<String> getErrorMessages() {
        List<String> res = error;
        error = new ArrayList<>();
        return res;
    }

    @Override
    public List<String> getAllMessages() {
        List<String> res = getSuccessMessages();
        res.addAll(getErrorMessages());
        return res;
    }
}
