package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.messages.LoadGameMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static websocket.commands.UserGameCommand.CommandType.CONNECT;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    LoadGameHandler loadGameHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException, DeploymentException, IOException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

                    String extractedMessage = "";
                    ChessGame extractedGame = null;

                    switch (serverMessage.getServerMessageType()){
                        case LOAD_GAME ->{
                            LoadGameMessage loadMessage = new Gson().fromJson(message, LoadGameMessage.class);
                            extractedGame = loadMessage.getGameMessage();
                            loadGameHandler.loadGame(extractedGame);
                        }
                        case NOTIFICATION ->{
                            NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                            extractedMessage = notificationMessage.getNotificationMessage();
                            notificationHandler.notify(extractedMessage);
                        }
                        case ERROR ->{
                            ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                            extractedMessage = errorMessage.getErrorMessage();
                            notificationHandler.notify(extractedMessage);
                        }
                    }

                }
            });
        }
        catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage());
        }


    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connectToGame(String authToken, Integer gameID) throws ResponseException{
        try{
            var gameCommand = new UserGameCommand(CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(gameCommand));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }
}