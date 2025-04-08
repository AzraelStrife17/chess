package websocket;
import websocket.commands.UserGameCommand;

public interface GameCommandHandler {
    void command(UserGameCommand.CommandType command);
}
