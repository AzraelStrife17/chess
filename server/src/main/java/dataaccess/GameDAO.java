package dataaccess;
import chess.ChessGame.TeamColor;
import model.AuthData;
import model.GameData;
import model.GameNameRecord;
import model.JoinGameRecord;

import java.util.Collection;

public interface GameDAO {
    Integer createGame(String gameName) throws DataAccessException;
    String joinGame(JoinGameRecord joinGameInfo, AuthData authData) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void clearGames() throws DataAccessException;
    boolean verifyGameID(Integer gameID);

}
