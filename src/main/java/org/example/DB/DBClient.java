package org.example.DB;
import java.util.List;
import java.util.Map;

public abstract class DBClient {

    // Thuộc tính static lưu kết nối chung
    protected Object connection;

    protected String host;        // URI kết nối tới cơ sở dữ liệu
    protected String dbName;     // Tên database
    protected String username;
    protected String password;

    // Phương thức trừu tượng connect để kết nối đến DB
    public abstract void connect() throws Exception;
 
    // Thiết lập thông tin kết nối (URI và tên database)
    public void setConnectionDetails(String uri, String dbName) {
        this.host = uri;
        this.dbName = dbName;
    }

    public void setConnectionDetails(String uri, String username, String password, String dbName) {
        this.host = uri;
        this.username = username;
        this.password = password;
        this.dbName = dbName;
    }

    public abstract void disconnect() throws Exception;

    // Phương thức trừu tượng getAllEntities để lấy thông tin về các entities (bảng)
    public abstract List<Table> getAllTables() throws Exception; // Adapter Pattern
    public abstract List<String> getAllEntities();
    // Phương thức trừu tượng để lấy tất cả thông tin và gán vào bảng table.
    public abstract void getAllRecords(Table table) throws Exception;

    // Các phương thức trừu tượng thay đổi CSDL bằng đối tượng ORM.
    // Template Method
    protected abstract boolean commitChange(); // Lưu thay đổi

    protected abstract boolean addToDB(Record r);
    public boolean addRecord(Record r) {
        return addToDB(r) && commitChange();
    }

    protected abstract boolean updateToDB(Record r, String column, Object value);
    public boolean updateRecord(Record r, String column, Object value) {
        return updateToDB(r, column, value) && commitChange();
    }

    protected abstract boolean deleteOnDB(Record r);
    public boolean deleteRecord(Record r) {
        return deleteOnDB(r) && commitChange();
    }

    public abstract boolean addElement(String tableName, Object object);
    public abstract boolean editElement(String tableName, Object object,
                                        String key, //Tên khóa chính để tìm
                                        Object value); // Gía trị khóa chính
    public abstract boolean deleteElement(String tableName, String key, Object value);
    public abstract List<Map<String,String>> getAllFieldName(String tableName);
    public abstract <T> List<T> getAllDataTable(String tableName, Class<T> clazz);

}
