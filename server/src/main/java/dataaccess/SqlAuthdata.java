package dataaccess;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.UUID;

public class SqlAuthdata implements AuthDAO {

    public SqlAuthdata() throws DataAccessException {
        configureDatabase();
    }

    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        try (var conn = DatabaseManager.getConnection()) {
            var insertAuthStatement = "INSERT INTO AuthTable (authToken, username) VALUES (?, ?)";
            executeUpdate(insertAuthStatement, authToken, username);
            return newAuth;
        }
        catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeUpdate(String insertAuthStatement, String authToken, String username) {
        try (var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(insertAuthStatement)){
                ps.setString(1, authToken);
                ps.setString(2, username);

                int rowsAffected = ps.executeUpdate();
                if(rowsAffected == 0){
                    throw new DataAccessException("failed to update users");

                }
            }

        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public AuthData getAuth(String auth) {
        return null;
    }

    @Override
    public String deleteAuthToken(String authToken) {
        return "";
    }

    @Override
    public void clearAuths() {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  AuthTable (
              `authToken` varchar (256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
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
            throw new DataAccessException("failed to create table in AuthTable");
        }
    }
}
