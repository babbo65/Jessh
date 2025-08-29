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

       JPanel mainPanel = new JPanel();
       mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

       JPanel userPanel = new JPanel();
       JPanel pwPanel = new JPanel();
       JPanel ipPanel = new JPanel();

       JLabel userLabel = new JLabel("Username");
       JLabel pwLabel = new JLabel("Password");
       JLabel ipLabel = new JLabel("ip address");
       JTextField user = new JTextField(20);
       JPasswordField pw = new JPasswordField(20);
       JTextField ip = new JTextField(20);

       userPanel.add(userLabel);
       userPanel.add(user);
       pwPanel.add(pwLabel);
       pwPanel.add(pw);
       ipPanel.add(ipLabel);
       ipPanel.add(ip);



       add_new_connection.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               Connection userConnection = new Connection(user.getText(), new String(pw.getPassword()), ip.getText());
               Connect connectionOpener = new Connect();
               connectionOpener.openConnection(userConnection);

           }
       });

       mainPanel.add(userPanel);
       mainPanel.add(pwPanel);
       mainPanel.add(ipPanel);
       mainPanel.add(add_new_connection);

        frame.add(mainPanel);
       frame.setSize(500, 600);
       frame.setVisible(true);
   }
}
