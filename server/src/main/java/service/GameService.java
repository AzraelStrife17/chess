package service;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.JoinGameRecord;
import chess.ChessGame.TeamColor;
import model.GameData;
import model.AuthData;
import model.GameNameRecord;

import java.util.Collection;

public class GameService {
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    public GameService(AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public Integer createGame(String gameName, String authToken){
        AuthData authExist = authDataAccess.getAuth(authToken);
        if(authExist != null){
            return gameDataAccess.createGame(gameName);
        }
        return null;
    }

    public String joinGame(JoinGameRecord joinGameInfo, String authToken){
        AuthData authExist = authDataAccess.getAuth(authToken);
        if(authExist != null){
            return gameDataAccess.joinGame(joinGameInfo, authExist);

        }
        return "no authToken";
    }

    public Collection<GameData> listGames(String authToken){
        AuthData authExist = authDataAccess.getAuth(authToken);
        if(authExist != null) {
            return gameDataAccess.listGames();
        }
        return null;
    }



}
