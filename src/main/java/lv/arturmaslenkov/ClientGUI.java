package lv.arturmaslenkov;

import javax.swing.*;
import java.awt.*;

public class ClientGUI {
    public static void main(String[] args) {
        Client client = new Client();

        JList listMessages = new JList();
        listMessages.setBounds(0,0,484,400);
        listMessages.setBorder(BorderFactory.createLineBorder(Color.black));

        //JScrollBar scrollBarChatWindow = new JScrollBar();
        //scrollBarChatWindow.setBounds(467,0,17,400);

        JTextArea textAreaMessage = new JTextArea();
        textAreaMessage.setBounds(0,400,350,61);
        textAreaMessage.setBorder(BorderFactory.createLineBorder(Color.black));
        textAreaMessage.setLineWrap(true);

        JButton buttonSend = new JButton("Send");
        buttonSend.setBounds(350,400,134,61);


        JFrame frame = new JFrame();
        frame.add(listMessages);
        //frame.add(scrollBarChatWindow);
        frame.add(textAreaMessage);
        frame.add(buttonSend);
        frame.setSize(500,500);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
