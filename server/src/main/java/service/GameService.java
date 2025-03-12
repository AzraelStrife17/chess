package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.JoinGameRecord;
import model.GameData;
import model.AuthData;

import java.util.Collection;
import java.util.List;

public class GameService {
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    public GameService(AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public Integer createGame(String gameName, String authToken) throws DataAccessException {
        AuthData authExist = authDataAccess.getAuth(authToken);
        if(authExist != null){
            return gameDataAccess.createGame(gameName);
        }
        return null;
    }

    public String joinGame(JoinGameRecord joinGameInfo, String authToken) throws DataAccessException {
        AuthData authExist = authDataAccess.getAuth(authToken);
        if(authExist != null){
            return gameDataAccess.joinGame(joinGameInfo, authExist);

        }
        return "no authToken";
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        AuthData authExist = authDataAccess.getAuth(authToken);
        if(authExist != null) {
            return gameDataAccess.listGames();
        }

        return List.of(new GameData(0, null, null, null, null));
    }



}
