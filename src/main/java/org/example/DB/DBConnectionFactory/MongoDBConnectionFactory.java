package org.example.DB.DBConnectionFactory;

import org.example.DB.DBConnection.DBConnection;
import org.example.DB.DBConnection.MongoDBConnection;

public class MongoDBConnectionFactory extends DBConnectionFactory{
    @Override
    public DBConnection createConnection(){
        return new MongoDBConnection();
    }
}
