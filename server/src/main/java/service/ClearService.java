package service;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;

public class ClearService {
    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    public ClearService(UserDAO userDataAccess, AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public void clearDatabase(){
        userDataAccess.clearUsers();
        authDataAccess.clearAuths();
        gameDataAccess.clearGames();

    }
}
