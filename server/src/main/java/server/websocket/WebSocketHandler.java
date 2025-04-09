package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
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
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.OnOpen;
import java.io.IOException;


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

            AuthToken authTokenModel = new AuthToken(command.getAuthToken());
            AuthData auth = authDataAccess.getAuth(authTokenModel);
            String username = "";
            if (auth != null){
                username = auth.username();
            }

            Integer gameID = command.getGameID();

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, gameID);
                case MAKE_MOVE -> makeMove(session, username, gameID);
            }
        } catch (JsonSyntaxException | DataAccessException | InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }


    private void connect(Session session, String username, Integer gameID) throws IOException, DataAccessException {
        connections.add(username, session);
        if (username.isEmpty()){
            var invalidAuthTokenMessage = "Error: invalid authToken";
            ServerMessage errorMessage = new ErrorMessage(invalidAuthTokenMessage);
            connections.broadcast(username, errorMessage);
        }
        else if(!gameDataAccess.verifyGameID(gameID)){
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

    private void makeMove(Session session, String username, Integer gameID) throws IOException, InvalidMoveException {
        ChessGame game = gameDataAccess.retrieveGame(gameID);


        var message = String.format("%s moved", username);

        ServerMessage loadGameMessage = new LoadGameMessage(message);
        connections.broadcast(username, loadGameMessage);

        ServerMessage notificationMessage = new NotificationMessage(message);
        connections.broadcast(username, notificationMessage);

    }


}

