package org.example.DB.SQLite;
import org.example.DB.DBClient;
import org.example.DB.Record;
import org.example.DB.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLiteClient extends DBClient {

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
    public List<Table> getAllTables() throws Exception {
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
                    columnsResultSet.close();
                    String[] columns = columnList.toArray(new String[0]);

                    // Thêm bảng vào danh sách
                    SQLiteTable table = new SQLiteTable(tableName, columns);
                    tables.add(table);
                    System.out.print(tableName+" " + columns.length + ": ");
                    for (String col: columns) {System.out.print(col+", ");}
                    getAllRecords(table);
                    System.out.println("Done!");
                }

                return tables;
            }
            catch (SQLException e) {
                System.out.println("Failed to fetch tables: " + e.getMessage());
            }
        }

        return null;
    }

    @Override
    public void getAllRecords(Table table) throws Exception {
        System.out.println("getAllRecords...!" + table.getClass().toString() +  table.getClass().getTypeName());
        if (connection != null && table instanceof SQLiteTable) {
            System.out.println("launch");
            try {
                Statement stmt = ((Connection) connection).createStatement(
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_UPDATABLE
                );
                String query = "SELECT * FROM " + table.name;
                ResultSet resultSet = stmt.executeQuery(query);
                table.setRecords(resultSet);
                System.out.println("Fetch all data successfully!");
            }
            catch (SQLException e) {
                System.out.println("Failed to fetch " + table.name + ": " + e.getMessage());
            }
        }
    }

    @Override
    public void disconnect() throws SQLException {
        if (connection != null) {
            ((Connection) connection).close();
        }
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