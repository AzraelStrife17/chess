package websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    ServerMessageHandler serverMessageHandler;


    public WebSocketFacade(String url, ServerMessageHandler serverMessageHandler) throws ResponseException, DeploymentException, IOException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageHandler = serverMessageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage.ServerMessageType serverMessage = new Gson().fromJson(message, ServerMessage.ServerMessageType.class);
                    serverMessageHandler.notify(serverMessage);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage());
        }


    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


}