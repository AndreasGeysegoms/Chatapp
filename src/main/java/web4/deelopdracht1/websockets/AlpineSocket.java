package web4.deelopdracht1.websockets;

import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/a")
public class AlpineSocket extends TextWebSocketHandler {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Socket A: "+session.getId() + " has opened a connection");
        sendMessageToAll("User " + session.getId() + " has connected");
        try {
            session.getBasicRemote().sendText("Connection Established");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println("Socket A: Message from " + session.getId() + ": " + message);
        sendMessageToAll(message);
    }

    @OnClose
    public void onClose(Session session){
        System.out.println("Socket A: Chat " +session.getId()+" has ended");
        sessions.remove(session);
    }

    private void sendMessageToAll(String message){
        for(Session s : sessions){
            try {
                s.getBasicRemote().sendText(message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
