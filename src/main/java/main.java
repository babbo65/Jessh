import view.add_connection;

import javax.swing.*;

public class main {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                add_connection conn = new add_connection();
                conn.newcon();
            }
        });
    }
}
