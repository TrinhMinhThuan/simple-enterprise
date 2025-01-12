package org.example.DB.SQLite;
import org.bson.Document;
import org.example.DB.DBClient;
import org.example.DB.Record;
import org.example.DB.Table;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

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
                }
                System.out.println("Done!");
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
        if (connection != null && table instanceof SQLiteTable) {
            System.out.println("launch");
            try {
                Statement stmt = ((Connection) connection).createStatement(
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY
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
    public List<String> getAllEntities() {
        if (connection != null) {
            try {
                List<String> result = new ArrayList<>();
                DatabaseMetaData metaData = ((Connection) connection).getMetaData();
                ResultSet resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});
                while (resultSet.next()) {
                    String tableName = resultSet.getString("TABLE_NAME");
                    result.add(tableName);
                }
                return result;
            } catch (Exception e) {
                System.out.println("Failed to fetch collections: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public <T> List<T> getAllDataTable(String tableName, Class<T> clazz) {
        List<T> dataList = new ArrayList<>();
    
        if (connection != null) {
            String query = "SELECT * FROM " + tableName;
            try {
                Statement stmt = ((Connection) connection).createStatement();
                ResultSet resultSet = stmt.executeQuery(query);

                // Lấy metadata của bảng để ánh xạ cột
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
    
                while (resultSet.next()) {
                    T obj = clazz.getDeclaredConstructor().newInstance(); // Khởi tạo đối tượng T
    
                    // Ánh xạ từng cột vào thuộc tính của đối tượng T
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object columnValue = resultSet.getObject(i);
    
                        // Tìm thuộc tính tương ứng trong lớp T
                        Field field;
                        try {
                            field = clazz.getDeclaredField(columnName);
                            field.setAccessible(true); // Cho phép truy cập thuộc tính private
                            field.set(obj, columnValue); // Gán giá trị từ ResultSet
                        } catch (NoSuchFieldException ignored) {
                            // Bỏ qua nếu lớp T không có thuộc tính tương ứng
                        }
                    }
    
                    // Thêm đối tượng vào danh sách
                    dataList.add(obj);
                }
    
            } catch (Exception e) {
                System.err.println("Error retrieving data from table " + tableName + ": " + e.getMessage());
            }
        } else {
            System.err.println("Database connection is not initialized.");
        }
    
        return dataList;
    }
    
    @Override
    public boolean addElement(String tableName, Object object) {
        Map<String, Object> fieldValues = extractFields(object);
        if (fieldValues.isEmpty()) {
            System.err.println("No fields found in the object!");
            return false;
        }

        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        List<Object> values = new ArrayList<>();

        // Duyệt qua các cặp key-value
        fieldValues.forEach((field, val) -> {
            columns.append(field).append(",");
            placeholders.append("?,");
            values.add(val);
        });

        // Bỏ dấu phẩy cuối cùng
        if (columns.length() > 0) columns.setLength(columns.length() - 1);
        if (placeholders.length() > 0) placeholders.setLength(placeholders.length() - 1);

        String query = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

        try (PreparedStatement stmt = ((Connection) connection).prepareStatement(query)) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding element: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean editElement(String tableName, Object object, String key, Object value) {
        Map<String, Object> fieldValues = extractFields(object);
        if (fieldValues.isEmpty()) {
            System.err.println("No fields found in the object!");
            return false;
        }

        StringBuilder setClause = new StringBuilder();
        List<Object> values = new ArrayList<>();

        // Tạo câu lệnh SET
        fieldValues.forEach((field, val) -> {
            setClause.append(field).append(" = ?,");
            values.add(val);
        });

        // Bỏ dấu phẩy cuối cùng
        if (setClause.length() > 0) setClause.setLength(setClause.length() - 1);

        String query = "UPDATE " + tableName + " SET " + setClause + " WHERE " + key + " = ?";
        values.add(value); // Thêm giá trị của khóa chính

        try (PreparedStatement stmt = ((Connection) connection).prepareStatement(query)) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error editing element: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteElement(String tableName, String key, Object value) {
        String query = "DELETE FROM " + tableName + " WHERE " + key + " = ?";

        try (PreparedStatement stmt = ((Connection) connection).prepareStatement(query)) {
            stmt.setObject(1, value);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting element: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Map<String, String>> getAllFieldName(String tableName) {
        List<Map<String, String>> fieldDetails = new ArrayList<>();
        if (connection != null) {
            try {
                DatabaseMetaData metaData = ((Connection) connection).getMetaData();
                ResultSet columnsResultSet = metaData.getColumns(null, null, tableName, null);

                while (columnsResultSet.next()) {
                    String fieldName = columnsResultSet.getString("COLUMN_NAME");
                    String fieldType = columnsResultSet.getString("TYPE_NAME");

                    Map<String, String> fieldDetail = new HashMap<>();
                    fieldDetail.put("fieldName", fieldName);
                    fieldDetail.put("fieldType", fieldType);

                    fieldDetails.add(fieldDetail);
                }
            } catch (Exception e) {
                System.out.println("Failed to get field names: " + e.getMessage());
            }
        } else {
            System.out.println("No active SQLite connection.");
        }
        return fieldDetails;
    }

    // Hàm trích xuất các trường từ object bằng Reflection API
    private Map<String, Object> extractFields(Object object) {
        Map<String, Object> fieldValues = new HashMap<>();
        Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true); // Cho phép truy cập các trường private
            try {
                Object value = field.get(object); // Lấy giá trị trường
                fieldValues.put(field.getName(), value); // Thêm tên và giá trị trường vào map
            } catch (IllegalAccessException e) {
                System.err.println("Error accessing field: " + field.getName() + " - " + e.getMessage());
            }
        }
        return fieldValues;
    }
}