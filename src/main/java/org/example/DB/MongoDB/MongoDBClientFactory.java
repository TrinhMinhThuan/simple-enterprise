package org.example.DB.MongoDB;

import org.example.DB.DBClientFactory;

public class MongoDBClientFactory extends DBClientFactory{
    @Override
    public MongoDBClient createConnection(){
        return new MongoDBClient();
    }
}
