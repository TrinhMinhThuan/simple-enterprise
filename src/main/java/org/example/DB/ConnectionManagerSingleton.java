package org.example.DB;

public class ConnectionManagerSingleton {

    private static ConnectionManagerSingleton instance;

    // Biến tĩnh để lưu kết nối duy nhất
    private static DBClient currentConnection = null;


    // Private constructor để ngăn tạo instance từ bên ngoài
    private ConnectionManagerSingleton() {}

    // Phương thức đồng bộ để lấy instance duy nhất
    public static synchronized ConnectionManagerSingleton getInstance() {
        if (instance == null) {
            instance = new ConnectionManagerSingleton();
        }
        return instance;
    }

    public static void setConnetion (DBClient connetion) {
        currentConnection = connetion;
    }



    // Phương thức mở kết nối mới
    public static void openConnection(String uri, String username, String password, String dbName) throws Exception {
        // Mở kết nối mới và lưu vào biến tĩnh
        currentConnection.setConnectionDetails(uri, username, password, dbName);
        currentConnection.connect();
    }

    public void openConnection() throws Exception {
        // Mở kết nối mới và lưu vào biến tĩnh
        currentConnection.connect();
    }


    // Phương thức đóng kết nối
    public void closeConnection() throws Exception {
        if (currentConnection != null) {
            currentConnection.disconnect();
            currentConnection = null;
        }
    }

    // Phương thức để lấy kết nối
    public DBClient getConnection() {
        return currentConnection;
    }

    // Kiểm tra nếu có kết nối nào
    public boolean isConnected() {
        return currentConnection != null;
    }
}
