package ru.kush.dao.jdbc_dao.worker;

import javax.ejb.Singleton;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Singleton
public class JdbcWorkerImpl implements JdbcWorker {
    private final String url;
    private final String userName;
    private final String userPassword;

    {
        Properties properties = new Properties();
        try {
            properties.load(JdbcWorkerImpl.class.getClassLoader().getResourceAsStream("accessToDataBase.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.url = properties.getProperty("url");
        this.userName = properties.getProperty("userName");
        this.userPassword = properties.getProperty("password");
    }


    @Override
    public Connection getNewConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.userName, this.userPassword);
    }
}
