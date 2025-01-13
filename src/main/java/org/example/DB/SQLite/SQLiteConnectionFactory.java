package org.example.DB.SQLite;

import org.example.DB.DBClientFactory;

public class SQLiteConnectionFactory extends DBClientFactory{
    @Override
    public SQLiteClient createConnection(){
        return new SQLiteClient();
    }
}
