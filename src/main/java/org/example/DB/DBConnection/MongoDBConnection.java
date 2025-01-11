package org.example.DB.DBConnection;


import com.mongodb.MongoException;

import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

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
    public boolean editElement(String tableName, Object object, Object idValue) {
        if (connection != null) {
            try {
                // Lấy cơ sở dữ liệu MongoDB
                MongoDatabase database = ((MongoClient) connection).getDatabase(dbName);
                MongoCollection<Document> collection = database.getCollection(tableName);

                // Chuyển đối tượng object thành Document để cập nhật
                Document updatedDoc = new Document();

                // Duyệt qua tất cả các trường của đối tượng và thêm vào Document (trừ _id)
                for (Field field : object.getClass().getDeclaredFields()) {
                    field.setAccessible(true);  // Cho phép truy cập vào các trường private
                    if (!field.getName().equals("_id")) {  // Loại bỏ trường _id
                        updatedDoc.append(field.getName(), field.get(object));  // Lấy tên trường và giá trị của nó
                    }
                }

                // Kiểm tra nếu idValue là chuỗi, chuyển đổi sang ObjectId nếu cần
                idValue = idValue instanceof String ? new ObjectId(String.valueOf(idValue)) : idValue;

                // Tạo filter với _id từ tham số idValue
                Bson filter = new Document("_id", idValue);  // Tạo filter với _id từ tham số

                // Cập nhật tài liệu (chỉ cập nhật các trường khác ngoài _id)
                Bson updateOperation = new Document("$set", updatedDoc);
                UpdateResult result = collection.updateOne(filter, updateOperation);

                // Kiểm tra kết quả
                if (result.getMatchedCount() > 0) {
                    return true; // Thành công
                } else {
                    System.out.println("No document found with the specified _id.");
                    return false; // Không tìm thấy tài liệu
                }
            } catch (MongoException | IllegalAccessException e) {
                System.out.println("Failed to update document: " + e.getMessage());
                return false; // Lỗi
            }
        }
        return false; // Không có kết nối MongoDB
    }

    @Override
    public boolean deleteElement(String tableName, Object idValue) {
        if (connection != null) {
            try {
                // Lấy cơ sở dữ liệu MongoDB
                MongoDatabase database = ((MongoClient) connection).getDatabase(dbName);
                MongoCollection<Document> collection = database.getCollection(tableName);

                // Chuyển idValue sang ObjectId nếu cần
                Object id = idValue instanceof String ? new ObjectId(String.valueOf(idValue)) : idValue;

                // Tạo filter với _id từ tham số idValue
                Bson filter = new Document("_id", id);  // Tạo filter với _id từ tham số

                // Xóa tài liệu
                DeleteResult result = collection.deleteOne(filter);

                // Kiểm tra kết quả
                if (result.getDeletedCount() > 0) {
                    return true; // Thành công
                } else {
                    System.out.println("No document found with the specified _id.");
                    return false; // Không tìm thấy tài liệu
                }
            } catch (MongoException e) {
                System.out.println("Failed to delete document: " + e.getMessage());
                return false; // Lỗi
            }
        }
        return false; // Không có kết nối MongoDB
    }


    @Override
    public void disconnect() {
        if (connection != null) {
            ((MongoClient) connection).close();
        }
    }

}
