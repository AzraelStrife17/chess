package server.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import dataaccess.AuthDAO;
import model.AuthToken;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.OnOpen;
import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    public WebSocketHandler(AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
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

            Integer gameID = command.getGameID();

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, gameID);
            }
        } catch (JsonSyntaxException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private void connect(Session session, String username, Integer gameID) throws IOException {
        connections.add(username, session);
        if(!gameDataAccess.verifyGameID(gameID)){
            var invalidIDMessage = "Error: invalid game ID";
            ServerMessage errorMessage = new ErrorMessage(invalidIDMessage);
            connections.broadcast(username, errorMessage);

        }

        else {
            String game = String.valueOf(gameID);

            ServerMessage loadGameMessage = new LoadGameMessage(game);
            connections.broadcast(username, loadGameMessage);

            var message = "connected to game";
            ServerMessage notificationMessage = new NotificationMessage(message);
            connections.broadcast(username, notificationMessage);
        }
    }


}

