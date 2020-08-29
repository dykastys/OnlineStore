package ru.kush.controllers.account_controllers.additional.message_maker;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MessageMakerTest {

    private MessageMakerImpl maker;

    @Before
    public void setUp() {
        this.maker = new MessageMakerImpl();
    }

    @Test
    public void test_append_to_success_and_get() {
        String success1 = "success1";
        String success2 = "success2";

        maker.appendToSuccess(success1);
        maker.appendToSuccess(success2);

        List<String> success = maker.getSuccessMessages();

        assertThat(success.size(), is(2));
        assertThat(success.get(0), is(success1));
        assertThat(success.get(1), is(success2));
    }

    @Test
    public void test_append_to_error_and_get() {
        String error1 = "error1";
        String error2 = "error2";

        maker.appendToError(error1);
        maker.appendToError(error2);

        List<String> error = maker.getErrorMessages();

        assertThat(error.size(), is(2));
        assertThat(error.get(0), is(error1));
        assertThat(error.get(1), is(error2));
    }

    @Test
    public void test_get_all_messages() {
        String success1 = "success1";
        String success2 = "success2";
        String error1 = "error1";
        String error2 = "error2";

        maker.appendToSuccess(success1);
        maker.appendToSuccess(success2);
        maker.appendToError(error1);
        maker.appendToError(error2);

        List<String> messages = maker.getAllMessages();

        assertThat(messages.size(), is(4));
        assertThat(messages.get(0), is(success1));
        assertThat(messages.get(1), is(success2));
        assertThat(messages.get(2), is(error1));
        assertThat(messages.get(3), is(error2));
    }
}
