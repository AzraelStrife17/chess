package dataaccess;
import model.UserData;
import model.LoginRecord;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.ResultSet;
import java.sql.SQLException;
import static dataaccess.DatabaseUtil.executeUpdate;


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
                    String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
                    var insertUserStatement = "INSERT INTO UserDataTable (username, password, email) VALUES (?, ?, ?)";
                    executeUpdate(insertUserStatement, user.username(), hashedPassword, user.email());
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
                    String storedHashPassword = rs.getString("password");
                    return BCrypt.checkpw(loginInfo.password(), storedHashPassword);
                }
                return false;
            }
        }
        catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Failed to search for user");
        }
    }


    public void clearUsers() throws DataAccessException {
        var statement = "TRUNCATE UserDataTable";
        executeUpdate(statement);
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

