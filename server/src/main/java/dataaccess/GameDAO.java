package dataaccess;
import chess.ChessGame.TeamColor;
import model.AuthData;
import model.GameData;
import model.GameNameRecord;
import model.JoinGameRecord;

public interface GameDAO {
    Integer createGame(String gameName);
    String joinGame(JoinGameRecord joinGameInfo, AuthData authData);

}
