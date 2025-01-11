package org.example.DB.DBConnection;

import java.sql.*;

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
    public void getAllEntities() {
        if (connection != null) {
            try {
                DatabaseMetaData metaData = ((Connection) connection).getMetaData();
                ResultSet resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});
                System.out.println("Tables in the database:");
                while (resultSet.next()) {
                    String tableName = resultSet.getString("TABLE_NAME");
                    System.out.println(tableName);
                }
            } catch (SQLException e) {
                System.out.println("Failed to fetch tables: " + e.getMessage());
            }
        }
    }

    @Override
    public void disconnect() throws SQLException {
        if (connection != null) {
            ((Connection) connection).close();
        }
    }
}