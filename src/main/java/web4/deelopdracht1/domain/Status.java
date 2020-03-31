package web4.deelopdracht1.domain;

public enum Status {

    ONLINE("Online"),
    OFFLINE("Offline"),
    AWAY("Away");

    private String string;

    Status(String string) {
        this.string = string;
    }

    public String getString(){
        return string;
    }
}
