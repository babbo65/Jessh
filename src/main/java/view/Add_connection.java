package view;

import controller.Connect;
import model.Connection;

import javax.swing.*;
import java.awt.event.*;

public class Add_connection {
   public void newcon()
   {
       JFrame frame = new JFrame();
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       JButton add_new_connection = new JButton("Connect");
       add_new_connection.setBounds(150, 200, 220, 50);

       JTextArea user = new JTextArea();
       JTextArea pw = new JTextArea();
       JTextArea ip = new JTextArea();
       frame.add(user);
       frame.add(pw);
       frame.add(ip);

       add_new_connection.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               Connection userConnection = new Connection(user.getText(), pw.getText(), ip.getText());
               Connect connectionOpener = new Connect();
               connectionOpener.openConnection(userConnection);

           }
       });

       frame.add(add_new_connection);
       frame.setSize(500, 600);
       frame.setVisible(true);
   }
}
