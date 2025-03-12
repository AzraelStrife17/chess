package dataaccess;
import model.UserData;
import model.LoginRecord;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlUserdata implements UserDAO {

    public MySqlUserdata() throws DataAccessException {
        configureDatabase();
    }

    public UserData createUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM UserDataTable WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1, user.username());
                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    var insertUserStatement = "INSERT INTO UserDataTable (username, password, email) VALUES (?, ?, ?)";
                    executeUpdate(insertUserStatement, user.username(), user.password(), user.email());
                    return user;
                }
                else{
                    return null;
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("User could not be created");
        }
    }

    public boolean getUser(LoginRecord loginInfo) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM UserDataTable WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1, loginInfo.username());
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    return true;
                }
                return false;
            }
        }
        catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Failed to search for user");
        }
    }

    @Override
    public void clearUsers() {

    }

    private void executeUpdate(String insertUserStatement, String username, String password, String email) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(insertUserStatement)){
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, email);

                int rowsAffected = ps.executeUpdate();
                if(rowsAffected == 0){
                    throw new DataAccessException("failed to update users");

                }
            }

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  UserDataTable (
              `username` varchar (256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar (256) Not Null,
              Primary KEY (`username`)
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

