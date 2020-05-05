package web4.deelopdracht1.db;

import web4.deelopdracht1.domain.Chatbericht;
import java.util.List;

public interface ChatBerichtenRepository {

    void addChatBericht(Chatbericht c);

    List<Chatbericht> chatBerichtenVanGesprek(String userId, String userId1);
}
