package websocket.messages;


import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    private final ChessGame game;
    private final String role;


    public LoadGameMessage(ChessGame game, String role) {
        super(ServerMessage.ServerMessageType.LOAD_GAME);
        this.game = game;
        this.role = role;
    }

    public ChessGame getGameMessage() {
        return game;
    }

    public String getRole(){
        return role;
    }
}
