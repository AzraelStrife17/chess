package server.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.DataAccessException;
import model.AuthData;
import dataaccess.AuthDAO;
import model.AuthToken;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.OnOpen;
import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final AuthDAO authDataAccess;

    public WebSocketHandler(AuthDAO authDataAccess) {
        this.authDataAccess = authDataAccess;
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket connection opened");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

            AuthToken authToken = new AuthToken(command.getAuthToken());
            AuthData auth = authDataAccess.getAuth(authToken);
            String username = auth.username();

            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username);
            }
        } catch (JsonSyntaxException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveSession(Integer gameID, Session session) {
    }

    private void connect(Session session, String username) throws IOException {
        connections.add(username, session);
        var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(username, loadGame);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(username, notification);
    }
}

