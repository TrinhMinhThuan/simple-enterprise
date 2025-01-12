package org.example.GUI.Membership;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class RegisterForm extends AuthencationForm{

    @Override
    public boolean createForm() {
        final boolean[] isSuccess = {false}; // Biến kết quả đăng ký

        // Tạo JFrame
        JFrame frame = new JFrame("User Registration");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // File chọn đường dẫn
        JLabel fileLabel = new JLabel("Select or Create Credential File:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(fileLabel, gbc);

        JTextField fileField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        frame.add(fileField, gbc);

        JButton browseButton = new JButton("Browse");
        gbc.gridx = 2;
        gbc.gridy = 0;
        frame.add(browseButton, gbc);

        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                fileField.setText(selectedFile.getAbsolutePath());
            }
        });

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(passwordField, gbc);

        // Register Button
        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        frame.add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            String filePath = fileField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (filePath.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Kiểm tra nếu file tồn tại và username đã tồn tại
                File file = new File(filePath);
                if (file.exists()) {
                    List<String> lines = Files.readAllLines(Paths.get(filePath));
                    for (String line : lines) {
                        String[] parts = line.split(":");
                        if (parts.length > 0 && parts[0].equals(encrypt(username))) {
                            JOptionPane.showMessageDialog(frame, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                // Thêm thông tin đăng ký vào file
                String encryptedUsername = encrypt(username);
                String encryptedPassword = encrypt(password);
                String userInfo = encryptedUsername + ":" + encryptedPassword;

                // Lưu thông tin vào file
                Files.write(Paths.get(filePath), (userInfo + System.lineSeparator()).getBytes(), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);

                JOptionPane.showMessageDialog(frame, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                isSuccess[0] = true; // Đăng ký thành công
                frame.dispose(); // Đóng cửa sổ
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error writing to file!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NoSuchAlgorithmException ex) {
                JOptionPane.showMessageDialog(frame, "Encryption Error!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Đợi đến khi cửa sổ được đóng
        while (frame.isDisplayable()) {
            try {
                Thread.sleep(100); // Giảm tải CPU
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return isSuccess[0];
    }

    // Hàm mã hóa SHA-256


    public static void main(String[] args) {
        AuthencationForm a = new RegisterForm();
        // Vòng lặp yêu cầu đăng ký
        while (true) {
            if (a.createForm()) {
                System.out.println("User registered successfully!");
                break; // Thoát vòng lặp sau khi đăng ký thành công
            } else {
                int retry = JOptionPane.showConfirmDialog(
                        null,
                        "Are you exit?",
                        "Retry Registration",
                        JOptionPane.YES_NO_OPTION
                );
                if (retry != JOptionPane.YES_OPTION) {
                    System.out.println("User chose to exit.");
                    break;
                }
            }
        }
    }
}
