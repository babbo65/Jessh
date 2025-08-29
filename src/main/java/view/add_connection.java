package view;

import javax.swing.*;

public class add_connection {
   public void newcon()
   {
       JFrame frame = new JFrame();
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       JButton add_new_connection = new JButton("Connect");
       add_new_connection.setBounds(150, 200, 220, 50);

       JTextArea user = new JTextArea();
       JTextArea pw = new JTextArea();
       JTextArea ip = new JTextArea();

       frame.add(add_new_connection);
       frame.setSize(500, 600);
       frame.setVisible(true);
   }
}
