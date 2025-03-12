package dataaccess;
import model.AuthData;
import java.sql.ResultSet;
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
            DatabaseUtil.executeUpdate(insertAuthStatement, authToken, username);
            return newAuth;
        }
        catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM AuthTable WHERE authToken = ?";
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    String username = rs.getString("username");
                    return new AuthData(authToken, username);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String deleteAuthToken(String authToken) throws DataAccessException {
        var statement = "DELETE FROM AuthTable WHERE authToken=?";
        DatabaseUtil.executeUpdate(statement, authToken);
        AuthData verifyDeletion = getAuth(authToken);
        if(verifyDeletion == null){
            return "";
        }
        else{
            return authToken;
        }
    }


    public void clearAuths() throws DataAccessException {
        var statement ="TRUNCATE AuthTable";
        DatabaseUtil.executeUpdate(statement);
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
