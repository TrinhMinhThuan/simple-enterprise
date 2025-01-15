package org.example.GUI.DBForm;

import org.example.DB.DBClient;
import org.example.DB.DBClientFactory;
import org.example.DB.DatabaseType;
import org.example.DB.MongoDB.MongoDBClientFactory;
import org.example.DB.MySQL.MySQLClientFactory;
import org.example.DB.SQLite.SQLiteConnectionFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class DBConnectionForm  extends JFrame {

    private static Map<DatabaseType, DBClientFactory> factoryDict = Map.of(
            DatabaseType.MYSQL, new MySQLClientFactory(),
            DatabaseType.MONGODB, new MongoDBClientFactory(),
            DatabaseType.SQLITE, new SQLiteConnectionFactory()
    );

    public static boolean createForm(AtomicReference<DBClient> connectionRef) {

        // Tạo cửa sổ JFrame
        JFrame frame = new JFrame("Database Connection");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);  // Kích thước cửa sổ
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        ArrayList<String> DBTypeList = new ArrayList<>();
        for (DatabaseType type: DatabaseType.values()){
            DBTypeList.add(type.getDisplayName());
        }

        JComboBox<?> dbTypeComboBox = new JComboBox<>(DBTypeList.toArray(new String[0]));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(dbTypeComboBox, gbc);


        // Label và TextField cho Host
        JLabel hostLabel = new JLabel("Host:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        frame.add(hostLabel, gbc);

        JTextField hostField = new JTextField(30);
        hostField.setPreferredSize(new Dimension(500, 25));  // Thay đổi kích thước ô nhập liệu
        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(hostField, gbc);

        // Label và TextField cho Username
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(30);
        usernameField.setPreferredSize(new Dimension(500, 25));  // Thay đổi kích thước ô nhập liệu
        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(usernameField, gbc);

        // Label và TextField cho mật khẩu
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(30);
        passwordField.setPreferredSize(new Dimension(500, 25));  // Thay đổi kích thước ô nhập liệu
        gbc.gridx = 1;
        gbc.gridy = 3;
        frame.add(passwordField, gbc);

        // Label và TextField cho database name
        JLabel dbNameLabel = new JLabel("Database Name:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(dbNameLabel, gbc);

        JTextField dbNameField = new JTextField(30);
        dbNameField.setPreferredSize(new Dimension(500, 25));  // Thay đổi kích thước ô nhập liệu
        gbc.gridx = 1;
        gbc.gridy = 4;
        frame.add(dbNameField, gbc);

        // Nút kết nối
        JButton connectButton = new JButton("Connect");
        connectButton.setPreferredSize(new Dimension(200, 30));  // Thay đổi kích thước nút
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        frame.add(connectButton, gbc);

        // Biến để lưu kết quả kết nối
        final boolean[] connectionSuccessful = {false};

        connectButton.addActionListener(e -> {
            String host = hostField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String dbName = dbNameField.getText();
            String selectedDbType = Objects.requireNonNull(dbTypeComboBox.getSelectedItem()).toString();
            DatabaseType dbType = DatabaseType.valueOf(selectedDbType.toUpperCase());

            if (host.isEmpty() && username.isEmpty() && password.isEmpty() && dbName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill connect information!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DBClient dbConnection = factoryDict.get(dbType).createConnection();

            try {
                dbConnection.setConnectionDetails(host, username, password, dbName);
                dbConnection.connect();
                connectionSuccessful[0] = true;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            }

            // Thử kết nối đến cơ sở dữ liệu

            if (connectionSuccessful[0]) {
                connectionRef.set(dbConnection);
                JOptionPane.showMessageDialog(frame, "Connected successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                frame.dispose(); // Đóng cửa sổ nếu kết nối thành công
            } else {
                JOptionPane.showMessageDialog(frame, "Connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Hiển thị cửa sổ
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Chờ cửa sổ đóng
        while (frame.isDisplayable()) {
            try {
                Thread.sleep(100); // Giảm tải CPU
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return connectionSuccessful[0];
    }

}
