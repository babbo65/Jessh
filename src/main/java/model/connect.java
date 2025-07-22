package model;

import com.jcraft.jsch.*;

public class connect {

    public void connect(Connection connection) {
        JSch jsch = new JSch();
        try {
            // Args are username, pw, ip
            Session session = jsch.getSession(connection.username, connection.ip, connection.port);
            session.setPassword(connection.password);

            // Avoid asking for key confirmation
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();
            System.out.println("Connected!");

            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
}
