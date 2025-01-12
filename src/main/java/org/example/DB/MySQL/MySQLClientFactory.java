package org.example.DB.MySQL;

import org.example.DB.DBClient;
import org.example.DB.DBClientFactory;

public class MySQLClientFactory extends DBClientFactory{
    @Override
    public DBClient createConnection(){
        return new MySQLClient();
    }
}
