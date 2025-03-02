package service;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

public class ClearService {
    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;

    public ClearService(UserDAO userDataAccess, AuthDAO authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public void clearDatabase(){
        userDataAccess.clearUsers();
        authDataAccess.clearAuths();
    }
}
