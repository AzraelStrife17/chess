package websocket.messages;
import chess.ChessGame.TeamColor;


public class LoadGameMessage extends ServerMessage {
    private final String game;
    private final TeamColor color;



    public LoadGameMessage(String game, TeamColor color) {
        super(ServerMessage.ServerMessageType.LOAD_GAME);
        this.game = game;
        this.color = color;
    }

    public String getGame() {
        return game;
    }
}
