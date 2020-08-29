package ru.kush.dao.jdbc_dao.worker;

import java.sql.Connection;
import java.sql.SQLException;

public interface JdbcWorker {
    Connection getNewConnection() throws SQLException;
}
