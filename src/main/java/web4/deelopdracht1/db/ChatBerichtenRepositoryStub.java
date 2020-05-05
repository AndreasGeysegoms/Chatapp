package web4.deelopdracht1.db;

import web4.deelopdracht1.domain.Chatbericht;

import java.util.ArrayList;
import java.util.List;

public class ChatBerichtenRepositoryStub implements ChatBerichtenRepository {

    private List<Chatbericht> chatberichten = new ArrayList<>();

    public ChatBerichtenRepositoryStub() {
    }

    @Override
    public void addChatBericht(Chatbericht c) {
        this.chatberichten.add(c);
        System.out.println("\n\nAlle berichten:");
        for (Chatbericht ch : chatberichten) {
            System.out.println("to " +ch.getOntvanger().getUserId()+ ", from " + ch.getZender().getUserId() +", message:" +ch.getBericht());
        }
    }

    @Override
    public List<Chatbericht> chatBerichtenVanGesprek(String userId, String ontvangerId) {
        List<Chatbericht> res = new ArrayList<>();

        for (Chatbericht c : chatberichten) {
            if (c.getOntvanger().getUserId().equals(userId) && c.getZender().getUserId().equals(ontvangerId) || c.getZender().getUserId().equals(userId) && c.getOntvanger().getUserId().equals(ontvangerId)) {
                res.add(c);
            }
        }
        return res;
    }
}
