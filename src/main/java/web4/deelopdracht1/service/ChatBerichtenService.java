package web4.deelopdracht1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web4.deelopdracht1.db.ChatBerichtenRepository;
import web4.deelopdracht1.db.ChatBerichtenRepositoryStub;
import web4.deelopdracht1.domain.Chatbericht;
import web4.deelopdracht1.domain.Person;

import java.util.List;

@Service
public class ChatBerichtenService {

    private ChatBerichtenRepository db = new ChatBerichtenRepositoryStub();

    @Autowired
    public ChatBerichtenService() {
    }

    public void addBericht(Person zender, String bericht, Person ontvanger) {
        Chatbericht c = new Chatbericht(bericht, zender, ontvanger);
        db.addChatBericht(c);
    }

    public List<Chatbericht> getChatBerichten(Person user, Person ontvanger) {
        return db.chatBerichtenVanGesprek(user.getUserId(), ontvanger.getUserId());
    }

}
