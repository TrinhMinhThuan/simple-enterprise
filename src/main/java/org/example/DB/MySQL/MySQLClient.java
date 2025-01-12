package org.example.DB.MySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.example.DB.DBClient;
import org.example.DB.Record;
import org.example.DB.Table;

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
    public List<Table> getAllTables() throws SQLException {
        if (connection != null) {
            List<Table> tables = new ArrayList<>();
            try {
                DatabaseMetaData metaData = ((Connection) connection).getMetaData();
                ResultSet resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});
                System.out.println("Tables in the database:");
                while (resultSet.next()) {
                    String tableName = resultSet.getString("TABLE_NAME");

                    // Lấy cột của bảng
                    ResultSet columnsResultSet = metaData.getColumns(null, null, tableName, "%");
                    List<String> columnList = new ArrayList<>();
                    while (columnsResultSet.next()) {
                        columnList.add(columnsResultSet.getString("COLUMN_NAME"));
                    }
                    String[] columns = columnList.toArray(new String[0]);

                    // Thêm bảng vào danh sách
                    MySQLTable table = new MySQLTable(tableName, columns);
                    tables.add(table);
                    System.out.println("\t"+tableName+"\t"+"\t"+columns);
                }
            }
            catch (SQLException e) {
                System.out.println("Failed to fetch tables: " + e.getMessage());
            }

            return tables;
        }
        else {
            throw new SQLException("No connection initialized!");
        }
    }

    @Override
    public void disconnect() throws SQLException {
        if (connection != null) {
            ((Connection) connection).close();
        }
    }

    @Override
    public void getAllRecords(Table table) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllRecords'");
    }

    @Override
    protected boolean commitChange() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'commitChange'");
    }

    @Override
    protected boolean addToDB(Record r) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToDB'");
    }

    @Override
    protected boolean updateToDB(Record r, String column, Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateToDB'");
    }

    @Override
    protected boolean deleteOnDB(Record r) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteOnDB'");
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
