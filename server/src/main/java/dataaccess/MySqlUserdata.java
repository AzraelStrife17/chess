package dataaccess;
import dataaccess.UserDAO;
import dataaccess.DatabaseManager;
import dataaccess.DataAccessException;

import java.sql.SQLException;

public class MySqlUserdata {

    public MySqlUserdata() throws DataAccessException {
        configureDatabase();
    }



    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  UserData (
              `username` UNIQUE varchar (256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar (256) Not Null,
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to create table in userdata");
        }
    }
    }

