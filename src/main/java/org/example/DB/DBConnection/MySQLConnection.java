package org.example.DB.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MySQLConnection extends DBConnection {

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
    public List<String> getAllEntities() {
        if (connection != null) {
            try {
                DatabaseMetaData metaData = ((Connection) connection).getMetaData();
                ResultSet resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});
                List<String> tableNames = new ArrayList<>();
                while (resultSet.next()) {
                    String tableName = resultSet.getString("TABLE_NAME");
                    tableNames.add(tableName);
                }
                return tableNames;
            } catch (SQLException e) {
                System.out.println("Failed to fetch tables: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public boolean addElement(String tableName, Object object) {
        return false;
    }

    @Override
    public boolean editElement(String tableName, Object object, String key, Object value) {
        return false;
    }

    @Override
    public boolean deleteElement(String tableName, String key, Object value) {
        return false;
    }


    @Override
    public void disconnect() throws SQLException {
        if (connection != null) {
            ((Connection) connection).close();
        }
    }

    @Override
    public List<Map<String, String>> getAllFieldName(String tableName) {
        return List.of();
    }

    @Override
    public <T> List<T> getAllDataTable(String tableName, Class<T> clazz) {
        return List.of();
    }
}
