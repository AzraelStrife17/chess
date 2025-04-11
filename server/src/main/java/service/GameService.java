package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.*;

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
        AuthToken authToken = new AuthToken(createData.authToken());
        AuthData authExist = authDataAccess.getAuth(authToken);
        if(authExist != null){
            return gameDataAccess.createGame(createData.gameName());
        }
        return null;
    }

    public JoinGameResponse joinGame(JoinGameRecord joinGameInfo) throws DataAccessException {
        AuthToken authToken = new AuthToken(joinGameInfo.authToken());
        AuthData authExist = authDataAccess.getAuth(authToken);
        if(authExist != null){
            return gameDataAccess.joinGame(joinGameInfo, authExist);

        }
        return new JoinGameResponse("no authToken", null);
    }

    public Collection<GameData> listGames(AuthToken authToken) throws DataAccessException {
        AuthData authExist = authDataAccess.getAuth(authToken);
        if(authExist != null) {
            return gameDataAccess.listGames();
        }

        return List.of(new GameData(0, null, null, null, null));
    }



}
