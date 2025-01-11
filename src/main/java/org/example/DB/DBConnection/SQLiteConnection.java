package org.example.DB.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteConnection extends DBConnection {

    @Override
    public void connect() throws SQLException {
        try {
            String url = "jdbc:sqlite:" + this.dbName;
            connection = DriverManager.getConnection(url);
            System.out.println("SQLite connection established successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to SQLite: " + e.getMessage());
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
    public boolean editElement(String tableName, Object object, String key,Object value) {
        return false;
    }

    @Override
    public boolean deleteElement(String tableName, Object object) {
        return false;
    }

    @Override
    public void disconnect() throws SQLException {
        if (connection != null) {
            ((Connection) connection).close();
        }
    }
}