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

/**
 * Server endpoint en handler voor socket 'a'
 */
@ServerEndpoint("/a")
public class AlpineSocket extends TextWebSocketHandler {

    /**
     * Set van sessies om users in bij te houden als sessie id's
     */
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    /**
     * Deze methode wordt opgeroepen wanneer een socket geopend wordt
     * @param session de sessie
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Socket A: "+session.getId() + " has opened a connection");
        sendMessageToAll("User " + session.getId() + " has connected");
        try {
            //stuur naar de client van de sessie 'Connection Established' over TCP.
            session.getBasicRemote().sendText("Connection Established");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        sessions.add(session);
    }

    /**
     * Deze methode wordt opgeroepen wanneer een bericht over een socket gestuurd wordt.
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println("Socket A: Message from " + session.getId() + ": " + message);
        sendMessageToAll(message);
    }

    /**
     * Deze methode wordt opgeroepen wanneer een websocket gesloten wordt.
     * @param session
     */
    @OnClose
    public void onClose(Session session){
        System.out.println("Socket A: Chat " +session.getId()+" has ended");
        sessions.remove(session);
    }

    /**
     * Deze methode stuurt een bericht naar alle geconnecteerde gebruikers over TCP.
     * @param message
     */
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
