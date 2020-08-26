package ru.kush.entities;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserTest {

    private User user;

    @Before
    public void userInit() {
        user = new User();
        user.setId(1);
        user.setLogin("log");
        user.setPassword("pass".hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_id_0() {
        user.setId(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_id_lt_0() {
        user.setId(-5);
    }

    @Test
    public void test_set_get_normal_id() {
        user.setId(5);
        assertThat(user.getId(), is(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_null_login() {
        user.setLogin(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_empty_login() {
        user.setLogin("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_login_with_backspaces() {
        user.setLogin("qwe asd");
    }

    @Test
    public void test_set_get_normal_login() {
        user.setLogin("login");
        assertThat(user.getLogin(), is("login"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_negative_password_hash() {
        user.setPassword(-10);
    }

    @Test
    public void test_set_get_normal_password() {
        int password = "password".hashCode();
        user.setPassword(password);
        assertThat(user.getPassword(), is("password".hashCode()));
    }

    @Test
    public void test_set_get_date() throws InterruptedException {
        Date date = new Date(System.currentTimeMillis() - 1000L);
        user.setDate(date);
        Thread.sleep(100);
        assertThat(user.getDate(), is(date));
    }

    @Test
    public void test_equals_and_hash_code_user() {
        Date date = new Date(System.currentTimeMillis() - 1000L);
        user.setDate(date);

        User user2 = new User();
        assertThat(user2.equals(user), is(false));

        User user3 = new User("log", "pass".hashCode());
        user3.setId(1);
        user3.setDate(date);
        assertThat(user.equals(user3), is(true));
    }
}
