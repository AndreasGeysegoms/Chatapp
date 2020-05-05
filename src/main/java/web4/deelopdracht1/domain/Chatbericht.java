package web4.deelopdracht1.domain;

public class Chatbericht {

    private String bericht;
    private Person zender, ontvanger;

    public Chatbericht(String bericht, Person zender, Person ontvanger) {
        setBericht(bericht);
        setOntvanger(ontvanger);
        setZender(zender);
    }

    public String getBericht() {
        return bericht;
    }

    public void setBericht(String bericht) {
        this.bericht = bericht;
    }

    public Person getZender() {
        return zender;
    }

    public void setZender(Person zender) {
        this.zender = zender;
    }

    public Person getOntvanger() {
        return ontvanger;
    }

    public void setOntvanger(Person ontvanger) {
        this.ontvanger = ontvanger;
    }
}
