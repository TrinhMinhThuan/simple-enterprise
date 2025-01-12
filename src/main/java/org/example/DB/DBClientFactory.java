package org.example.DB;

public abstract class DBClientFactory {

    // Factory method để tạo đối tượng kết nối phù hợp
    public abstract DBClient createConnection();
}
