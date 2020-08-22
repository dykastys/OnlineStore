package ru.kush.temporary;

import ru.kush.dao.jdbc_dao.JdbcWorker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        JdbcWorker worker = new JdbcWorker();
        try(Connection connection = worker.getNewConnection();
            Statement statement = connection.createStatement()) {

            statement.executeUpdate("insert into products (name, maker, quantity, price) values " +
                    "('Printer','Sony', 21, 1569)");

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
