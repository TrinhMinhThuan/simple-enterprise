package org.example.DB.MySQL;

import org.example.DB.DBClientFactory;

public class MySQLClientFactory extends DBClientFactory{
    @Override
    public MySQLClient createConnection(){
        return new MySQLClient();
    }
}
