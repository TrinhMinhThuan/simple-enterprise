package org.example.DB.SQLite;

import org.example.DB.DBClient;
import org.example.DB.DBClientFactory;

public class SQLiteConnectionFactory extends DBClientFactory{
    @Override
    public DBClient createConnection(){
        return new SQLiteClient();
    }
}
