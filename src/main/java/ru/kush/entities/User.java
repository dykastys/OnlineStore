package ru.kush.entities;

import java.util.Date;
import java.util.Objects;

public class User {
    private int id;
    private String login;
    private int password;
    private Date date;

    public User() { }

    public User(String login, int password) {
        this.login = login;
        this.password = password;
        this.date = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if(id <= 0) {
            throw new IllegalArgumentException("id");
        }
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if(login == null || login.isEmpty() || login.contains(" ")) {
            throw new IllegalArgumentException("login");
        }
        this.login = login;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                password == user.password &&
                Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password);
    }
}
