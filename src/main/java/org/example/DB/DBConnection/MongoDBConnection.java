package org.example.DB.DBConnection;


import com.mongodb.MongoException;

import com.mongodb.client.*;
import org.bson.Document;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


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
    public List<String> getAllEntities() {
        if (connection != null) {
            try {
                MongoDatabase database = ((MongoClient) connection).getDatabase(dbName);
                MongoIterable<String> collections = database.listCollectionNames();
                List<String> result = new ArrayList<>();
                for (String collectionName : collections) {
                    result.add(collectionName);
                }
                return result;
            } catch (Exception e) {
                System.out.println("Failed to fetch collections: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public boolean addElement(String tableName, Object object) {
        if (connection != null) {
            try {
                // Lấy cơ sở dữ liệu MongoDB
                MongoDatabase database = ((MongoClient) connection).getDatabase(dbName);
                MongoCollection<Document> collection = database.getCollection(tableName);

                // Chuyển đối tượng object thành Document để lưu vào MongoDB
                Document doc = new Document();

                for (Field field : object.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    doc.append(field.getName(), field.get(object));
                }
                collection.insertOne(doc);
                return true;
            } catch (MongoException | IllegalAccessException e) {
                System.out.println("Failed to add document: " + e.getMessage());
                return false; // Lỗi
            }
        }
        return false; // Kết nối MongoDB không tồn tại
    }

    @Override
    public void disconnect() {
        if (connection != null) {
            ((MongoClient) connection).close();
        }
    }

}
