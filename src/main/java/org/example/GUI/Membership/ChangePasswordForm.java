package org.example.GUI.Membership;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ChangePasswordForm extends AuthencationForm{


    public boolean createForm() {
        final boolean[] isSuccess = {false}; // Biến kết quả thay đổi mật khẩu

        // Tạo JFrame
        JFrame frame = new JFrame("Change Password");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // File chọn đường dẫn
        JLabel fileLabel = new JLabel("Select Credential File:");
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
            int returnValue = fileChooser.showOpenDialog(null);
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

        // Old Password
        JLabel oldPasswordLabel = new JLabel("Old Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(oldPasswordLabel, gbc);

        JPasswordField oldPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(oldPasswordField, gbc);

        // New Password
        JLabel newPasswordLabel = new JLabel("New Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(newPasswordLabel, gbc);

        JPasswordField newPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        frame.add(newPasswordField, gbc);

        // Confirm New Password
        JLabel confirmPasswordLabel = new JLabel("Confirm New Password:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        frame.add(confirmPasswordField, gbc);

        // Change Password Button
        JButton changePasswordButton = new JButton("Change Password");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        frame.add(changePasswordButton, gbc);

        changePasswordButton.addActionListener(e -> {
            String filePath = fileField.getText();
            String username = usernameField.getText();
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (filePath.isEmpty() || username.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "New passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(frame, "Credential file does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<String> lines = Files.readAllLines(Paths.get(filePath));
                boolean found = false;

                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    String[] parts = line.split(":");
                    if (parts.length == 2 && parts[0].equals(encrypt(username)) && parts[1].equals(encrypt(oldPassword))) {
                        // Mật khẩu cũ đúng, thay đổi mật khẩu
                        lines.set(i, parts[0] + ":" + encrypt(newPassword));
                        found = true;
                        break;
                    }
                }

                if (found) {
                    // Cập nhật lại file với mật khẩu mới
                    Files.write(Paths.get(filePath), lines);
                    JOptionPane.showMessageDialog(frame, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    isSuccess[0] = true; // Thay đổi mật khẩu thành công
                    frame.dispose(); // Đóng cửa sổ
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or old password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error reading or writing to file!", "Error", JOptionPane.ERROR_MESSAGE);
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


    public static void main(String[] args) {
        AuthencationForm a = new ChangePasswordForm();

        // Vòng lặp yêu cầu thay đổi mật khẩu
        while (true) {
            if (a.createForm()) {
                System.out.println("Password changed successfully!");
                break; // Thoát vòng lặp sau khi thay đổi mật khẩu thành công
            } else {
                int retry = JOptionPane.showConfirmDialog(
                        null,
                        "Password change failed. Try again?",
                        "Retry Change Password",
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
