package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.CreateGameData;
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

    public Integer createGame(CreateGameData createData) throws DataAccessException {

        AuthData authExist = authDataAccess.getAuth(createData.authToken());
        if(authExist != null){
            return gameDataAccess.createGame(createData.gameName());
        }
        return null;
    }

    public String joinGame(JoinGameRecord joinGameInfo) throws DataAccessException {
        AuthData authExist = authDataAccess.getAuth(joinGameInfo.authToken());
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
