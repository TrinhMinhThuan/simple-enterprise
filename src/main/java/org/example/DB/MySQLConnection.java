package org.example.DB;

import java.sql.*;

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
