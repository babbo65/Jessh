package model;

public class Connection {
    private String username;
    private String password;
    private String ip;
    private int port;
    public Connection(String username, String password, String ip)
    {
        this.username = username;
        this.password = password;
        this.ip = ip;
        port = 22;
    }
    public Connection(String username, String password, String ip, int port)
    {
        this.username = username;
        this.password = password;
        this.ip = ip;
        this.port = port;
    }
    public String GetUsername() { return username; }
    public String GetPassword() { return password; }
    public String Getip() { return ip; }
    public int GetPort() { return port; }
}
