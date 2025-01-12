package org.example.DB.MongoDB;
import org.example.DB.DBClient;
import org.example.DB.Record;
import org.example.DB.Table;

import com.mongodb.MongoException;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public List<Table> getAllEntities() throws Exception{
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'disconnect'");
    }
    @Override
    public void disconnect() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'disconnect'");
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

}
