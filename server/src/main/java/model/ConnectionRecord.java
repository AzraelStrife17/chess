package model;
import org.eclipse.jetty.websocket.api.Session;

public record ConnectionRecord(Integer gameID, Session session){
}
