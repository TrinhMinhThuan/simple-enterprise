package org.example.DB.DBConnectionFactory;

import org.example.DB.DBConnection.DBConnection;

public abstract class DBConnectionFactory {

    // Factory method để tạo đối tượng kết nối phù hợp
    public abstract DBConnection createConnection();
}
