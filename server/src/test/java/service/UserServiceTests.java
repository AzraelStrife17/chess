package service;
import dataaccess.AuthDAO;
import dataaccess.AuthMemoryDAO;
import dataaccess.UserDAO;
import dataaccess.UserMemoryDAO;
import model.LoginRecord;
import model.UserData;
import model.AuthData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    UserDAO userData = new UserMemoryDAO();
    AuthDAO authData = new AuthMemoryDAO();

    private final UserService service = new UserService(userData, authData);


    @Test
    void registerUserSuccess() {
        var user = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData = service.registerUser(user);

        assertNotNull(authData.authToken());
        assertEquals("James", authData.username());
    }

    @Test
    void registerUserAlreadyTaken(){
        var user1 = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData1 = service.registerUser(user1);

        var user2 = new UserData("James", "007", "BOND@gmail.com");
        AuthData authData2 = service.registerUser(user2);
        assertNull(authData2);
    }

    @Test
    void loginUserSuccess(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "007");
        AuthData authData = service.loginUser(userLogin);
        assertNotNull(authData.authToken());
        assertEquals("James", authData.username());
    }

    @Test
    void loginUserFailedPassword(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "000");
        AuthData authData = service.loginUser(userLogin);
        assertNull(authData);
    }

    @Test
    void loginUserFailedUsername(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("SolidSnake", "007");
        AuthData authData = service.loginUser(userLogin);
        assertNull(authData);
    }

    @Test
    void logoutUserSuccess(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "007");
        AuthData authData = service.loginUser(userLogin);

        String authToken = service.logoutUser(authData.authToken());

        assertEquals("", authToken);

    }

    @Test
    void logoutUserFail(){
        var user = new UserData("James", "007", "BOND@gmail.com");
        userData.createUser(user);

        var userLogin = new LoginRecord("James", "007");
        AuthData authData = service.loginUser(userLogin);

        String authToken = service.logoutUser("cf6f6db8-38cb-446a-9589-8049b0154009");

        assertNotEquals("", authToken);

    }
}
