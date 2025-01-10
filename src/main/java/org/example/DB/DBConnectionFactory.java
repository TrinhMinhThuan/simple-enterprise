package org.example.DB;


public class DBConnectionFactory {

    // Factory method để tạo đối tượng kết nối phù hợp
    public static DBConnection createConnection(DatabaseType dbType) {
        DBConnection dbConnection = null;

        switch (dbType) {
            case MYSQL: // MySQL
                dbConnection = new MySQLConnection();
                break;

            case MONGODB: // MongoDB
                dbConnection = new MongoDBConnection();
                break;

            default:
                System.out.println("Invalid database type.");
                break;
        }
        return dbConnection;
    }
}
