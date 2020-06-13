package gui;

import architecture.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

public abstract class NetworkFrame extends JFrame implements ActionListener, Observer {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 800;
    private static final Font FONT = new Font("Arial", Font.BOLD, 20);

    private JTextField field;
    private JTextArea area;
    private JPanel mainPanel;

    public NetworkFrame(String title) {
        super(title);

        field = new JTextField(20);
        area = new JTextArea(20, 20);
        mainPanel = new JPanel();

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(area, BorderLayout.CENTER);
        mainPanel.add(field, BorderLayout.SOUTH);
        add(mainPanel);
        field.addActionListener(this);

        area.setEditable(false);
        area.setFont(FONT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
    }

    protected void append(String str) {
        area.append(str + "\n");
    }

    protected JTextField getField() {
        return field;
    }
}