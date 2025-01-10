package org.example.GUI;

import javax.swing.*;
import java.awt.*;

public class DatabaseConnectionForm {

    private JFrame frame;
    private JComboBox<String> dbTypeComboBox;
    private JTextField hostField;
    private JTextField dbNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton connectButton;

    public DatabaseConnectionForm() {
        frame = new JFrame("Database Connection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2, 10, 10));

        dbTypeComboBox = new JComboBox<>(new String[]{"MySQL", "MongoDB"});
        hostField = new JTextField();
        dbNameField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        connectButton = new JButton("Connect");

        frame.add(new JLabel("Select Database Type:"));
        frame.add(dbTypeComboBox);
        frame.add(new JLabel("Host:"));
        frame.add(hostField);
        frame.add(new JLabel("Database Name:"));
        frame.add(dbNameField);
        frame.add(new JLabel("Username:"));
        frame.add(usernameField);
        frame.add(new JLabel("Password:"));
        frame.add(passwordField);
        frame.add(connectButton);

        frame.setVisible(true);
    }

    public String getSelectedDbType() {
        return (String) dbTypeComboBox.getSelectedItem();
    }

    public String getHost() {
        return hostField.getText();
    }

    public String getDbName() {
        return dbNameField.getText();
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public JButton getConnectButton() {
        return connectButton;
    }

    public JFrame getFrame() {
        return frame;
    }
}
