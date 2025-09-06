import view.AddConnection;

import javax.swing.*;

public class Main {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AddConnection conn = new AddConnection();
                conn.newcon();
            }
        });
    }
}
