package org.example.GUI.DBForm;


import org.example.DB.ConnectionManagerSingleton;
import org.example.Export.ExportObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

public class DBTableForm {

    private static JFrame frame;

    public static boolean createForm(List<String> tableNames) {
        try {
            frame = new JFrame();
            frame.setTitle("Danh sách bảng trong DB");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                    try {
                        onTableButtonClick(tableName);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });

                panel.add(tableButton); // Thêm nút vào panel
            }

            // Thêm panel vào JFrame
            frame.add(panel, BorderLayout.CENTER);

            frame.setVisible(true);
            return true; // Giao diện được tạo thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Giao diện không được tạo thành công
        }
    }

    private static void onTableButtonClick(String tableName) throws Exception {
        int confirm = JOptionPane.showConfirmDialog(frame, "Export " + tableName + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            List<Map<String, String>> objInfor =  ConnectionManagerSingleton.getInstance().getConnection().getAllFieldName(tableName);
            ExportObject.doExport(tableName, objInfor, "export_output/" + tableName + ".java");
            JOptionPane.showMessageDialog(frame, "Export done!");

        }
    }
}
