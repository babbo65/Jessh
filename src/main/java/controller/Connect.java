package controller;
import model.Connection;

import com.jcraft.jsch.*;

public class Connect {

    public void openConnection(Connection connection) {
        JSch jsch = new JSch();
        try {
            // Args are username, pw, ip
            Session session = jsch.getSession(connection.GetUsername(), connection.Getip(), connection.GetPort());
            session.setPassword(connection.GetPassword());

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
