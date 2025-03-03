package service;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;
import model.AuthData;
import model.GameNameRecord;

public class GameService {
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    public GameService(AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public Integer createGame(String gameName, String authToken){
        boolean authExist = authDataAccess.getAuth(authToken);
        if(authExist){
            return gameDataAccess.createGame(gameName);
        }
        return null;
    }
}
