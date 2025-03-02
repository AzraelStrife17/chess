package dataaccess;
import model.LoginRecord;
import model.UserData;

public interface UserDAO {
    UserData createUser(UserData user);
    boolean getUser(LoginRecord loginInfo);
    void clearUsers();
}
