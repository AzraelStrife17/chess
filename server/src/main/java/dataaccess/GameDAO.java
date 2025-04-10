package dataaccess;
import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.JoinGameRecord;

import java.util.Collection;

public interface GameDAO {
    Integer createGame(String gameName) throws DataAccessException;
    String joinGame(JoinGameRecord joinGameInfo, AuthData authData) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void clearGames() throws DataAccessException;
    boolean verifyGameID(Integer gameID);
    GameData retrieveGame(Integer gameID);
    boolean removePlayer(JoinGameRecord playerInfo) throws DataAccessException;
    boolean updateGame(Integer gameID, ChessGame game) throws DataAccessException;
    void addEndedGamesStatus(Integer gameID, String status);
    String getEndedGamesStatus(Integer gameID);
    void updateEndedGamesStatus(Integer gameID, String status);

}
