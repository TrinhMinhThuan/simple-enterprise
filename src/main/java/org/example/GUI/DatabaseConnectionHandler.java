package org.example.GUI;

import org.example.DB.ConnectionManagerSingleton;
import org.example.DB.DBClient;
import org.example.DB.DBClientFactory;
import org.example.DB.MongoDB.MongoDBClientFactory;
import org.example.DB.MySQL.MySQLClientFactory;
import org.example.DB.SQLite.SQLiteConnectionFactory;
import org.example.DB.DatabaseType;

import java.util.Map;
import javax.swing.*;

public class DatabaseConnectionHandler {

    private DatabaseConnectionForm form;
    private Map<DatabaseType, DBClientFactory> factoryDict = Map.of(
        DatabaseType.MYSQL, new MySQLClientFactory(),
        DatabaseType.MONGODB, new MongoDBClientFactory(),
        DatabaseType.SQLITE, new SQLiteConnectionFactory()
    );

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
            DBClient dbConnection = factoryDict.get(dbType).createConnection();
            ConnectionManagerSingleton.setConnetion(dbConnection);
            ConnectionManagerSingleton.openConnection(host, username, password, dbName);
            ConnectionManagerSingleton.getInstance().getConnection().getAllEntities();

            JOptionPane.showMessageDialog(form.getFrame(), dbType.name() + " connection established successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form.getFrame(), "Error: " + e.getMessage());
        }
    }
}
