package org.example.GUI;

import org.example.DB.ConnectionManagerSingleton;
import org.example.DB.DBConnection;
import org.example.DB.DBConnectionFactory;
import org.example.DB.DatabaseType;

import javax.swing.*;

public class DatabaseConnectionHandler {

    private DatabaseConnectionForm form;

    public DatabaseConnectionHandler(DatabaseConnectionForm form) {
        this.form = form;
        this.form.getConnectButton().addActionListener(e -> handleConnect());
    }

    private void handleConnect() {
        String selectedDbType = form.getSelectedDbType();
        DatabaseType dbType = DatabaseType.valueOf(selectedDbType.toUpperCase());

        String host = form.getHost().isEmpty() ? "" : form.getHost();
        String dbName = form.getDbName().isEmpty() ? "" : form.getDbName();
        String username = form.getUsername().isEmpty() ? "" : form.getUsername();
        String password = form.getPassword().isEmpty() ? "" : form.getPassword();

        try {
            ConnectionManagerSingleton.getInstance().closeConnection();
            DBConnection dbConnection = DBConnectionFactory.createConnection(dbType);
            ConnectionManagerSingleton.getInstance().setConnetion(dbConnection);
            ConnectionManagerSingleton.getInstance().openConnection(host, username, password, dbName);
            ConnectionManagerSingleton.getInstance().getConnection().getAllEntities();

            JOptionPane.showMessageDialog(form.getFrame(), dbType.name() + " connection established successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form.getFrame(), "Error: " + e.getMessage());
        }
    }
}
