package org.example.DB.DBConnection;


import com.mongodb.MongoException;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;


public class MongoDBConnection extends DBConnection {

    @Override
    public void connect() {
        try {
            // Kết nối tới MongoDB
            String uri = "mongodb+srv://" + username + ":" + password + "@" + host;
            connection = MongoClients.create(uri); // `host` đã được thiết lập từ phương thức setConnectionDetails
            System.out.println("MongoDB connection established successfully.");
        } catch (MongoException e) {
            System.err.println("Failed to connect to MongoDB: " + e.getMessage());
        }
    }
    @Override
    public void getAllEntities() {
        if (connection != null) {
            try {
                MongoDatabase database = ((MongoClient) connection).getDatabase(dbName);
                MongoIterable<String> collections = database.listCollectionNames();
                System.out.println("Collections in the database:");
                for (String collectionName : collections) {
                    System.out.println(collectionName);
                }
            } catch (Exception e) {
                System.out.println("Failed to fetch collections: " + e.getMessage());
            }
        }
    }

    @Override
    public void disconnect() {
        if (connection != null) {
            ((MongoClient) connection).close();
        }
    }

}
