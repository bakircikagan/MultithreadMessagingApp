package gui;

import architecture.Subject;
import network.Log;
import network.Server;
import utilities.Utilities;

import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ServerFrame extends NetworkFrame {

    private Server server;

    public ServerFrame(String name) throws IOException {
        super("Server" + " , " + name);

        server = new Server(name);
        server.addObserver(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                try {
                    server.kill();
                } catch (IOException exp) {
                    Utilities.handle(exp);
                }
            }
        });
        setVisible(true);
    }

    @Override
    public void update(Subject s) {
        if (server.isAlive()) {
            if(server.hasNext()) {
                String logFromServer = server.next().toString();
                append(logFromServer);
            }
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
                server.offer(new Log(text, server.getName()));
                field.setText("");
            }
        }
    }
}
