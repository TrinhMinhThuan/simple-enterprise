package org.example.DB.MongoDB;
import org.example.DB.DBClient;
import org.example.DB.Record;
import org.example.DB.Table;

import com.mongodb.MongoException;

import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.sql.SQLException;
import java.lang.reflect.Field;
import java.util.*;

public class MongoDBClient extends DBClient {

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
    public void disconnect() {
        if (connection != null) {
            ((MongoClient) connection).close();
        }
    }

    @Override
    public boolean addElement(String tableName, Object object) {
        if (connection != null) {
            try {
                // Lấy cơ sở dữ liệu MongoDB
                MongoDatabase database = ((MongoClient) connection).getDatabase(dbName);
                MongoCollection<Document> collection = database.getCollection(tableName);

                // Chuyển đối tượng object thành Output để lưu vào MongoDB
                Document doc = new Document();

                for (Field field : object.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.getName().equals("_id")) {
                        continue;
                    }
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
    public boolean editElement(String tableName, Object object, String identifier, Object value) {
        if (connection != null) {
            try {
                // Lấy cơ sở dữ liệu MongoDB
                MongoDatabase database = ((MongoClient) connection).getDatabase(dbName);
                MongoCollection<Document> collection = database.getCollection(tableName);

                // Chuyển đối tượng object thành Output để cập nhật
                Document updatedDoc = new Document();

                // Duyệt qua tất cả các trường của đối tượng và thêm vào Output (trừ _id)
                for (Field field : object.getClass().getDeclaredFields()) {
                    field.setAccessible(true);  // Cho phép truy cập vào các trường private
                    if (!field.getName().equals("_id")) {  // Loại bỏ trường _id
                        updatedDoc.append(field.getName(), field.get(object));  // Lấy tên trường và giá trị của nó
                    }
                }

                // Kiểm tra nếu value là chuỗi và là biến _id, chuyển đổi sang ObjectId nếu cần
                if (Objects.equals(identifier, "_id")) {
                    value = value instanceof String ? new ObjectId(String.valueOf(value)) : value;
                }
                // Tạo filter với _id từ tham số idValue
                Bson filter = new Document(identifier, value);  // Tạo filter với từ tham số

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
    public boolean deleteElement(String tableName, String identifier, Object value) {
        if (connection != null) {
            try {
                // Lấy cơ sở dữ liệu MongoDB
                MongoDatabase database = ((MongoClient) connection).getDatabase(dbName);
                MongoCollection<Document> collection = database.getCollection(tableName);

                // Kiểm tra nếu identifier là _id và value là chuỗi, chuyển value thành ObjectId nếu cần
                if (Objects.equals(identifier, "_id") && value instanceof String) {
                    value = new ObjectId((String) value); // Chuyển value thành ObjectId
                }

                // Tạo filter với identifier và value từ tham số truyền vào
                Bson filter = new Document(identifier, value);

                // Xóa tài liệu dựa trên filter
                DeleteResult result = collection.deleteOne(filter);

                // Kiểm tra kết quả
                if (result.getDeletedCount() > 0) {
                    return true; // Thành công
                } else {
                    System.out.println("No document found with the specified identifier.");
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
    public List<Map<String, String>> getAllFieldName(String tableName) {
        List<Map<String, String>> fieldDetails = new ArrayList<>();
        if (connection != null) {
            try {
                // Lấy cơ sở dữ liệu MongoDB
                MongoDatabase database = ((MongoClient) connection).getDatabase(dbName);
                MongoCollection<Document> collection = database.getCollection(tableName);

                // Lấy một document từ collection
                Document doc = collection.find().first();

                if (doc != null) {
                    // Duyệt qua tất cả các trường và lấy kiểu dữ liệu của từng trường
                    for (String fieldName : doc.keySet()) {
                        Object value = doc.get(fieldName);
                        String fieldType = value != null ? value.getClass().getSimpleName() : "null";

                        // Tạo Map để lưu thông tin fieldName và fieldType
                        Map<String, String> fieldInfo = new HashMap<>();
                        fieldInfo.put("fieldName", fieldName);
                        fieldInfo.put("fieldType", fieldType);

                        // Thêm Map vào danh sách
                        fieldDetails.add(fieldInfo);
                    }
                } else {
                    System.out.println("No document found in the collection: " + tableName);
                }
            } catch (Exception e) {
                System.out.println("Failed to get field names: " + e.getMessage());
            }
        } else {
            System.out.println("No active MongoDB connection.");
        }
        return fieldDetails;
    }

    @Override
    public <T> List<T> getAllDataTable(String tableName, Class<T> clazz) {
        List<T> dataList = new ArrayList<>();
        if (connection != null) {
            try {
                // Lấy cơ sở dữ liệu MongoDB
                MongoDatabase database = ((MongoClient) connection).getDatabase(dbName);
                MongoCollection<Document> collection = database.getCollection(tableName);

                // Lấy tất cả các document trong collection
                FindIterable<Document> documents = collection.find();

                // Duyệt qua từng document và ánh xạ vào đối tượng T
                for (Document doc : documents) {
                    T obj = clazz.getDeclaredConstructor().newInstance(); // Khởi tạo đối tượng T

                    // Ánh xạ dữ liệu từ Output vào đối tượng T
                    for (String fieldName : doc.keySet()) {
                        try {
                            Field field = clazz.getDeclaredField(fieldName); // Tìm trường trong lớp T
                            field.setAccessible(true); // Cho phép truy cập vào trường private

                            Object value = doc.get(fieldName); // Giá trị từ MongoDB

                            Class<?> fieldType = field.getType();
                            if (value != null && !fieldType.isAssignableFrom(value.getClass())) {
                                if (fieldType == Double.class) {
                                    value = Double.valueOf(value.toString());
                                } else if (fieldType == Integer.class) {
                                    value = Integer.valueOf(value.toString());
                                } else if (fieldType == Long.class) {
                                    value = Long.valueOf(value.toString());
                                } else if (fieldType == String.class) {
                                    value = value.toString();
                                } else if (fieldType == Boolean.class) {
                                    value = Boolean.valueOf(value.toString());
                                }  else {
                                    throw new IllegalArgumentException("Unsupported type: " + fieldType.getName());
                                }
                            }

                            field.set(obj, value); // Gán giá trị đã chuyển đổi vào trường của đối tượng T
                        } catch (NoSuchFieldException e) {
                            // Nếu không tìm thấy trường, bỏ qua (hoặc log thông báo)
                            System.out.println("Field not found: " + fieldName);
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            // Xử lý lỗi ánh xạ
                            System.err.println("Error mapping field: " + fieldName + " - " + e.getMessage());
                        }
                    }
                    dataList.add(obj); // Thêm đối tượng vào danh sách
                }
            } catch (MongoException | ReflectiveOperationException e) {
                System.out.println("Failed to get data from collection: " + e.getMessage());
            }
        }
        return dataList;
    }
    @Override
    public void getAllRecords(Table table) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllRecords'");
    }
    @Override
    protected boolean commitChange() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'commitChange'");
    }
    @Override
    protected boolean addToDB(Record r) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToDB'");
    }
    @Override
    protected boolean updateToDB(Record r, String column, Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateToDB'");
    }
    @Override
    protected boolean deleteOnDB(Record r) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteOnDB'");
    }
    @Override
    public List<Table> getAllTables() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllTables'");
    }


}
