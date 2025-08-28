package model;

public class Connection {
    String username;
    String password;
    String ip;
    int port;
    Connection(String username, String password, String ip)
    {
        this.username = username;
        this.password = password;
        this.ip = ip;
        port = 22;
    }
    Connection(String username, String password, String ip, int port)
    {
        this.username = username;
        this.password = password;
        this.ip = ip;
        this.port = port;
    }
}
