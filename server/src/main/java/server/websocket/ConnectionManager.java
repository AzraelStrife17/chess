package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session) {
        var connection = new Connection(username, session);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void broadcast(Session excludeSession, ServerMessage serverMessage, String receivers) throws IOException {
        var removeList = new ArrayList<Connection>();

        String jsonMessage = new Gson().toJson(serverMessage);

        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (Objects.equals(receivers, "rootClient")){
                    if(c.session.equals(excludeSession)){
                        c.send(jsonMessage);
                    }

                }
                else if (Objects.equals(receivers, "otherClients")) {
                    if (!c.session.equals(excludeSession)) {
                        c.send(jsonMessage);
                    }
                }

                else if(Objects.equals(receivers,"allClients")){
                    c.send(jsonMessage);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }
}
