package ru.kush.dao.jdbc_dao.dao_user;

import org.junit.Before;
import org.junit.Test;
import ru.kush.dao.jdbc_dao.worker.JdbcWorker;

import static org.mockito.Mockito.*;

public class DaoUserImplTest {

    private DaoUserImpl daoUser;
    private JdbcWorker worker;

    @Before
    public void setUp() {
        this.daoUser = new DaoUserImpl();
        daoUser.worker = this.worker = mock(JdbcWorker.class);
    }

    @Test
    public void test() {

    }
}
