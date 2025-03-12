package dataaccess;
import model.LoginRecord;
import model.UserData;

public interface UserDAO {
    UserData createUser(UserData user) throws DataAccessException;
    boolean getUser(LoginRecord loginInfo);
    void clearUsers();
}
