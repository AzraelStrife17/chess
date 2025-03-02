package service;
import dataaccess.AuthDAO;
import dataaccess.AuthMemoryDAO;
import dataaccess.UserDAO;
import dataaccess.UserMemoryDAO;
import service.UserService;

public class UserServiceTests {
    UserDAO userData = new UserMemoryDAO();
    AuthDAO authData = new AuthMemoryDAO();

    private final UserService service = new UserService(userData, authData);


    void registerUserSuccess() {

    }
}
