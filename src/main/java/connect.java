import com.jcraft.jsch.*;

public class connect {
    public static void main(String[] args) {
        JSch jsch = new JSch();
        try {
            // Args are username, pw, ip
            Session session = jsch.getSession(args[0], args[2], 22);
            session.setPassword(args[1]);

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
