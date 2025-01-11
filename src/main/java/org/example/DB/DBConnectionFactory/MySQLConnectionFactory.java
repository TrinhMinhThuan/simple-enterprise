package org.example.DB.DBConnectionFactory;

import org.example.DB.DBConnection.DBConnection;
import org.example.DB.DBConnection.MySQLConnection;

public class MySQLConnectionFactory extends DBConnectionFactory{
    @Override
    public DBConnection createConnection(){
        return new MySQLConnection();
    }
}
