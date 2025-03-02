package service;
import model.UserData;
import model.AuthData;
import java.util.Collection;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

public class UserService {

    private final UserDAO userDataAccess;
    private final AuthDAO AuthDataAccess;

    public UserService(UserDAO userDataAccess, AuthDAO authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.AuthDataAccess = authDataAccess;
    }


    public AuthData registerUser(UserData user){
        UserData userData = userDataAccess.createUser(user);
        if(userData == null){
            return null;
        }
        String username = userData.username();
        return AuthDataAccess.createAuth(username);
    }
}
