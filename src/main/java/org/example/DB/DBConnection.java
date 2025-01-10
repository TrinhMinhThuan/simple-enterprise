package org.example.DB;

import java.sql.SQLException;

public abstract class DBConnection {

    // Thuộc tính static lưu kết nối chung
    protected Object connection;

    protected String host;        // URI kết nối tới cơ sở dữ liệu
    protected String dbName;     // Tên database
    protected String username;
    protected String password;

    // Phương thức trừu tượng connect để kết nối đến DB
    public abstract void connect() throws SQLException;

    // Thiết lập thông tin kết nối (URI và tên database)
    public void setConnectionDetails(String uri, String dbName) {
        this.host = uri;
        this.dbName = dbName;
    }

    public void  setConnectionDetails(String uri, String username, String password, String dbName) {
        this.host = uri;
        this.username = username;
        this.password = password;
        this.dbName = dbName;
    }


    // Phương thức trừu tượng getAllEntities để lấy thông tin về các entities (bảng)
    public abstract void getAllEntities();


    public abstract void disconnect() throws SQLException;
}
