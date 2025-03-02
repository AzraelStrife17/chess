package service;
import dataaccess.AuthDAO;
import dataaccess.AuthMemoryDAO;
import dataaccess.UserDAO;
import dataaccess.UserMemoryDAO;
import model.UserData;
import model.AuthData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNull;
import service.UserService;

import java.util.Objects;

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
}
