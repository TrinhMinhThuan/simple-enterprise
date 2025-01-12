package org.example.DB.MongoDB;

import org.example.DB.DBClient;
import org.example.DB.DBClientFactory;

public class MongoDBClientFactory extends DBClientFactory{
    @Override
    public DBClient createConnection(){
        return new MongoDBClient();
    }
}
