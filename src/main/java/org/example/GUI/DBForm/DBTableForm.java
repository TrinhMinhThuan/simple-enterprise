package org.example.GUI.DBForm;


import org.example.DB.ConnectionManagerSingleton;
import org.example.Export.ExportObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class DBTableForm {

    private static JFrame frame;

    public static String createForm(List<String> tableNames) {
        AtomicReference<String> selectedTableName = new AtomicReference<>("");

        try {
            frame = new JFrame();
            frame.setTitle("Danh sách bảng trong DB");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            // Panel chứa các nút
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(6, 1, 10, 10)); // Hiển thị các nút theo cột

            // Tạo nút bấm cho từng tên bảng
            for (String tableName : tableNames) {
                JButton tableButton = new JButton(tableName);

                // Xử lý sự kiện khi nhấn vào nút
                tableButton.addActionListener((ActionEvent e) -> {
                    int confirm = JOptionPane.showConfirmDialog(frame, "Export " + tableName + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        selectedTableName.set(tableName); // Lưu tên bảng được chọn
                        frame.dispose(); // Đóng giao diện
                    }

                });

                panel.add(tableButton); // Thêm nút vào panel
            }

            // Thêm panel vào JFrame
            frame.add(panel, BorderLayout.CENTER);

            frame.setVisible(true);

            // Chờ cho đến khi frame bị đóng
            while (frame.isVisible()) {
                Thread.sleep(100); // Chờ
            }

            return selectedTableName.get(); // Trả về tên bảng được chọn
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Nếu xảy ra lỗi, trả về null
        }
    }

}
