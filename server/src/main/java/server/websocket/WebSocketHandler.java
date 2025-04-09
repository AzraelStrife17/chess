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
import model.GameData;
import model.JoinGameRecord;
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
import java.util.Objects;


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

            ChessMove move = null;
            if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
                MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                move = moveCommand.getMove();
            }

            AuthToken authTokenModel = new AuthToken(command.getAuthToken());
            AuthData auth = authDataAccess.getAuth(authTokenModel);
            String username = "";
            if (auth != null){
                username = auth.username();
            }

            Integer gameID = command.getGameID();

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, gameID);
                case MAKE_MOVE -> makeMove(username, gameID, move);
                case LEAVE -> leave(username, gameID);
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
            connections.broadcast(username, errorMessage, "rootClient");
        }
        else if(!gameDataAccess.verifyGameID(gameID)){
            var invalidIDMessage = "Error: invalid game ID";
            ServerMessage errorMessage = new ErrorMessage(invalidIDMessage);
            connections.broadcast(username, errorMessage, "rootClient");

        }

        else {
            String game = String.valueOf(gameID);


            ServerMessage loadGameMessage = new LoadGameMessage(game);
            connections.broadcast(username, loadGameMessage, "rootClient");

            var message = "connected to game";
            ServerMessage notificationMessage = new NotificationMessage(message);
            connections.broadcast(username, notificationMessage, "otherClients");
        }
    }

    private void makeMove(String username, Integer gameID, ChessMove move) throws IOException, InvalidMoveException {

        GameData gameData = gameDataAccess.retrieveGame(gameID);
        ChessGame game = gameData.game();

        game.makeMove(move);

        var message = String.format("%s moved", username);

        ServerMessage loadGameMessage = new LoadGameMessage(message);
        connections.broadcast(username, loadGameMessage, "allClients");

        ServerMessage notificationMessage = new NotificationMessage(message);
        connections.broadcast(username, notificationMessage, "otherClients");

    }

    private void leave(String username, Integer gameID) throws IOException, DataAccessException {
        GameData gameData = gameDataAccess.retrieveGame(gameID);
        connections.remove(username);

        if (Objects.equals(username, gameData.whiteUsername())){
            JoinGameRecord playerInfo = new JoinGameRecord(ChessGame.TeamColor.WHITE, gameID, null);
            gameDataAccess.removePlayer(playerInfo);
            var message = String.format("%s has left as white team", username);
            ServerMessage notificationMessage = new NotificationMessage(message);
            connections.broadcast(username, notificationMessage, "otherClients");
        }

        else if(Objects.equals(username, gameData.blackUsername())){
            JoinGameRecord playerInfo = new JoinGameRecord(ChessGame.TeamColor.BLACK, gameID, null);
            gameDataAccess.removePlayer(playerInfo);
            var message = String.format("%s has left as black team", username);
            ServerMessage notificationMessage = new NotificationMessage(message);
            connections.broadcast(username, notificationMessage, "otherClients");
        }

        else{
            var message = String.format("%s has left", username);
            ServerMessage notificationMessage = new NotificationMessage(message);
            connections.broadcast(username, notificationMessage, "otherClients");
        }


    }


}

