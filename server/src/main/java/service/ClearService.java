package service;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;

public class ClearService {
    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    public ClearService(UserDAO userDataAccess, AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public void clearDatabase() throws DataAccessException {
        userDataAccess.clearUsers();
        authDataAccess.clearAuths();
        gameDataAccess.clearGames();

    }
}
