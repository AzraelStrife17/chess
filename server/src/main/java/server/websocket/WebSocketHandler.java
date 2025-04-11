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
//
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


            Integer gameID = command.getGameID();

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, auth, gameID);
                case MAKE_MOVE -> makeMove(session, auth, gameID, move);
                case LEAVE -> leave(session, auth, gameID);
                case RESIGN -> resign(session, auth, gameID);
            }
        } catch (JsonSyntaxException | DataAccessException | InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }


    private void connect(Session session, AuthData auth, Integer gameID) throws IOException, DataAccessException {
        String username = "";
        if (auth != null){
            username = auth.username();
        }

        connections.add(username, gameID, session);
        if (username.isEmpty()){
            var invalidAuthTokenMessage = "Error: invalid authToken";
            ServerMessage errorMessage = new ErrorMessage(invalidAuthTokenMessage);
            connections.broadcast(session,gameID, errorMessage, "rootClient");
        }
        else if(!gameDataAccess.verifyGameID(gameID)){
            var invalidIDMessage = "Error: invalid game ID";
            ServerMessage errorMessage = new ErrorMessage(invalidIDMessage);
            connections.broadcast(session, gameID, errorMessage, "rootClient");

        }

        else {

            GameData gameData = gameDataAccess.retrieveGame(gameID);
            var game = gameData.game();
            ServerMessage loadGameMessage = null;

            if(Objects.equals(username, gameData.whiteUsername())){
                var role = "white";
                loadGameMessage = new LoadGameMessage(game, role);
                connections.broadcast(session, gameID, loadGameMessage, "rootClient");
                var message = String.format("%s connected to as white player", username);
                ServerMessage notificationMessage = new NotificationMessage(message);
                connections.broadcast(session, gameID, notificationMessage, "otherClients");
            }

            else if(Objects.equals(username, gameData.blackUsername())){
                var role = "black";
                loadGameMessage = new LoadGameMessage(game, role);
                connections.broadcast(session, gameID, loadGameMessage, "rootClient");
                var message = String.format("%s connected to as black player", username);
                ServerMessage notificationMessage = new NotificationMessage(message);
                connections.broadcast(session, gameID, notificationMessage, "otherClients");
            }

            else{
                var role = "observer";
                loadGameMessage = new LoadGameMessage(game, role);
                connections.broadcast(session, gameID, loadGameMessage, "rootClient");
                var message = String.format("%s connected to as observer", username);
                ServerMessage notificationMessage = new NotificationMessage(message);
                connections.broadcast(session, gameID, notificationMessage, "otherClients");

            }
        }
        gameDataAccess.addEndedGamesStatus(gameID, "NoPlayerResigned");
    }

    private void makeMove(Session session, AuthData auth, Integer gameID, ChessMove move) throws IOException, InvalidMoveException {
        String username = "";
        if (auth != null){
            username = auth.username();
        }

        GameData gameData = gameDataAccess.retrieveGame(gameID);
        ChessGame game = gameData.game();

        String resignCheck = gameDataAccess.getEndedGamesStatus(gameID);
        if(!Objects.equals(resignCheck, "NoPlayerResigned")){
            var playerResignedAlready = String.format("Error: %s", resignCheck);
            ServerMessage errorMessage = new ErrorMessage(playerResignedAlready);
            connections.broadcast(session, gameID, errorMessage, "rootClient");
        }


        else if (username.isEmpty()){
            var invalidAuthTokenMessage = "Error: invalid authToken";
            ServerMessage errorMessage = new ErrorMessage(invalidAuthTokenMessage);
            connections.broadcast(session, gameID, errorMessage, "rootClient");
        }


        else {


            if(Objects.equals(username, gameData.whiteUsername()) && game.getTeamTurn() != ChessGame.TeamColor.WHITE){
                var invalidTurnMessage = "Error: currently black's turn";
                ServerMessage errorMessage = new ErrorMessage(invalidTurnMessage);
                connections.broadcast(session, gameID, errorMessage, "rootClient");
            }

            else if(Objects.equals(username, gameData.blackUsername()) && game.getTeamTurn() != ChessGame.TeamColor.BLACK){
                var invalidTurnMessage = "Error: currently white's turn";
                ServerMessage errorMessage = new ErrorMessage(invalidTurnMessage);
                connections.broadcast(session, gameID, errorMessage, "rootClient");
            }

            else if(!Objects.equals(username, gameData.whiteUsername()) && !Objects.equals(username, gameData.blackUsername())){
                var invalidTurnMessage = "Error: observer not allowed to make moves";
                ServerMessage errorMessage = new ErrorMessage(invalidTurnMessage);
                connections.broadcast(session, gameID, errorMessage, "rootClient");
            }

            else {
                try {
                    game.makeMove(move);

                    gameDataAccess.updateGame(gameID, game);

                    var message = String.format("%s moved", username);

                    if(Objects.equals(username, gameData.whiteUsername())) {
                        ServerMessage loadGameMessage = new LoadGameMessage(game, "white");
                        connections.broadcast(session, gameID, loadGameMessage, "rootClient");

                        ServerMessage loadGameMessageOther = new LoadGameMessage(game, "black");
                        connections.broadcast(session, gameID, loadGameMessageOther, "otherClients");
                    }

                    else if(Objects.equals(username, gameData.blackUsername())) {
                        ServerMessage loadGameMessage = new LoadGameMessage(game, "black");
                        connections.broadcast(session, gameID, loadGameMessage, "rootClient");

                        ServerMessage loadGameMessageOther = new LoadGameMessage(game, "white");
                        connections.broadcast(session, gameID, loadGameMessageOther, "otherClients");
                    }


                    ServerMessage notificationMessage = new NotificationMessage(message);
                    connections.broadcast(session, gameID, notificationMessage, "otherClients");

                    if(game.isInCheckmate(game.getTeamTurn())){
                        ChessGame.TeamColor team = game.getTeamTurn();
                        var playerInCheck = String.format("%s in checkmate", team);
                        ServerMessage checkmateNotification = new NotificationMessage(playerInCheck);
                        connections.broadcast(session, gameID, checkmateNotification, "allClients");
                        gameDataAccess.updateEndedGamesStatus(gameID, "checkmate");
                    }

                    else if(game.isInCheck(game.getTeamTurn())){
                        ServerMessage checkNotification = new NotificationMessage("In Check");
                        connections.broadcast(session, gameID, checkNotification, "allClients");
                    }

                    else if(game.isInStalemate(game.getTeamTurn())){
                        ServerMessage stalemateNotification = new NotificationMessage("stalemate");
                        connections.broadcast(session, gameID, stalemateNotification, "allClients");
                        gameDataAccess.updateEndedGamesStatus(gameID, "stalemate");

                    }

                }
                catch (InvalidMoveException e) {
                    var invalidMoveMessage = "Error: invalid move";
                    ServerMessage errorMessage = new ErrorMessage(invalidMoveMessage);
                    connections.broadcast(session, gameID, errorMessage, "rootClient");
                    ;
                } catch (DataAccessException e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }

    private void leave(Session session, AuthData auth, Integer gameID) throws IOException, DataAccessException {
        String username = "";
        if (auth != null){
            username = auth.username();
        }

        GameData gameData = gameDataAccess.retrieveGame(gameID);
        connections.remove(username);

        if (Objects.equals(username, gameData.whiteUsername())){
            JoinGameRecord playerInfo = new JoinGameRecord(ChessGame.TeamColor.WHITE, gameID, null);
            gameDataAccess.removePlayer(playerInfo);
            var message = String.format("%s has left as white team", username);
            ServerMessage notificationMessage = new NotificationMessage(message);
            connections.broadcast(session, gameID, notificationMessage, "otherClients");
        }

        else if(Objects.equals(username, gameData.blackUsername())){
            JoinGameRecord playerInfo = new JoinGameRecord(ChessGame.TeamColor.BLACK, gameID, null);
            gameDataAccess.removePlayer(playerInfo);
            var message = String.format("%s has left as black team", username);
            ServerMessage notificationMessage = new NotificationMessage(message);
            connections.broadcast(session, gameID, notificationMessage, "otherClients");
        }

        else{
            var message = String.format("%s has left", username);
            ServerMessage notificationMessage = new NotificationMessage(message);
            connections.broadcast(session, gameID, notificationMessage, "otherClients");
        }
    }

    private void resign(Session session, AuthData auth, Integer gameID) throws IOException {
        String username = "";
        if (auth != null){
            username = auth.username();
        }

        GameData gameData = gameDataAccess.retrieveGame(gameID);

        String resignCheck = gameDataAccess.getEndedGamesStatus(gameID);
        if(!Objects.equals(resignCheck, "NoPlayerResigned")){
            var playerResignedAlready = String.format("Error: %s", resignCheck);
            ServerMessage errorMessage = new ErrorMessage(playerResignedAlready);
            connections.broadcast(session, gameID, errorMessage, "rootClient");
        }

        else if(Objects.equals(username, gameData.whiteUsername())){
            gameDataAccess.updateEndedGamesStatus(gameID, "White Resigned");
            var message = String.format("%s has forfeited for white team", username);
            ServerMessage notificationMessage = new NotificationMessage(message);
            connections.broadcast(session, gameID, notificationMessage, "allClients");
        }

        else if(Objects.equals(username, gameData.blackUsername())){
            gameDataAccess.updateEndedGamesStatus(gameID, "Black Resigned");
            var message = String.format("%s has forfeited for black team", username);
            ServerMessage notificationMessage = new NotificationMessage(message);
            connections.broadcast(session, gameID, notificationMessage, "allClients");
        }

        else{
            var errorObserverMessage = "Error: Observer can not resign";
            ServerMessage errorMessage = new ErrorMessage(errorObserverMessage);
            connections.broadcast(session, gameID, errorMessage, "rootClient");
        }


    }


}

