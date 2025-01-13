package org.example.DB.MySQL;

import org.example.DB.DBClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MySQLClient extends DBClient {

    @Override
    public void connect() throws SQLException {

        // Đảm bảo driver đã được tải
        try {
            String url = "jdbc:mysql://" + host + "/" + dbName;
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("MySQL connection established successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to MySQL: " + e.getMessage());
            throw e;
        }

    }


    @Override
    public void disconnect() throws SQLException {
        if (connection != null) {
            ((Connection) connection).close();
        }
    }



    @Override
    public boolean addElement(String tableName, Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addElement'");
    }

    @Override
    public boolean editElement(String tableName, Object object, String key, Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editElement'");
    }

    @Override
    public boolean deleteElement(String tableName, String key, Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteElement'");
    }

    @Override
    public List<Map<String, String>> getAllFieldName(String tableName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllFieldName'");
    }

    @Override
    public List<String> getAllEntities() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllEntities'");
    }

    @Override
    public <T> List<T> getAllDataTable(String tableName, Class<T> clazz) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllDataTable'");
    }
}
