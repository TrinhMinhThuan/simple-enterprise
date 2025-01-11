package org.example.DB.DBConnectionFactory;

import org.example.DB.DBConnection.DBConnection;
import org.example.DB.DBConnection.SQLiteConnection;

public class SQLiteConnectionFactory extends DBConnectionFactory{
    @Override
    public DBConnection createConnection(){
        return new SQLiteConnection();
    }
}
