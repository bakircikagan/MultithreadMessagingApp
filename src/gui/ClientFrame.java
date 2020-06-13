package gui;

import architecture.Subject;
import network.Client;
import utilities.Utilities;

import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ClientFrame extends NetworkFrame {

    private Client client;

    public ClientFrame(String name, String serverIp) throws IOException {
        super("Client" + " , " + name);
        client = new Client(name, serverIp);
        client.addObserver(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                try {
                    client.pushMessageToServer("CLOSE");
                    client.kill();
                } catch (IOException exp) {
                    Utilities.handle(exp);
                }
            }
        });
        setVisible(true);

    }
    @Override
    public void update(Subject s) {
        if (client.isAlive()) {
            String logFromServer = client.pushLogToWindow();
            append(logFromServer);
        }
        else {
            dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        JTextField field = getField();
        if (source == field) {
            String text = field.getText();
            if (text.length() > 0) {
                client.pushMessageToServer(text);
                field.setText("");
            }
        }
    }
}
