package dataaccess;
import model.LoginRecord;
import model.UserData;

import java.sql.SQLException;

public interface UserDAO {
    UserData createUser(UserData user) throws DataAccessException;
    boolean getUser(LoginRecord loginInfo) throws DataAccessException;
    void clearUsers() throws DataAccessException, SQLException;
}
