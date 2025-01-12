package org.example.GUI.FeatureForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseForm<T> extends JFrame {

    protected JTable table;
    protected JButton btnAdd, btnUpdate, btnDelete;
    protected List<T> data;
    protected DefaultTableModel tableModel;

    public BaseForm(String title) {
        super(title);
        data = new ArrayList<>();
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Cấu hình bảng
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // Tạo nút chức năng
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");

        // Lắng nghe sự kiện nút bấm
        btnAdd.addActionListener(e -> onAdd());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());

        // Đặt Layout cho JFrame
        JPanel panel = new JPanel();
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);

        // Thêm các thành phần vào JFrame
        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // Cập nhật dữ liệu vào bảng
    public void loadData(List<T> data) {
        this.data = data;
        updateTable();
    }

    // Cập nhật TableModel
    void updateTable() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        if (!data.isEmpty()) {
            var fields = data.get(0).getClass().getDeclaredFields();
            String[] columnNames = Arrays.stream(fields)
                    .map(field -> field.getName())
                    .toArray(String[]::new);
            tableModel.setColumnIdentifiers(columnNames);

            for (T item : data) {
                Object[] row = new Object[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    try {
                        fields[i].setAccessible(true);
                        if (fields[i].get(item) != null ) {
                            row[i] = fields[i].get(item).toString();
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                tableModel.addRow(row);
            }
        }
    }

    // Thêm dữ liệu mới
    protected void onAdd() {
        System.out.println("Thêm mới dữ liệu");
        AddForm<T> addForm = new AddForm<>(this);  // Mở form AddForm
        addForm.setVisible(true);
    }

    protected void onUpdate() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            T selectedItem = data.get(selectedRow);
            System.out.println("Cập nhật dữ liệu: " + selectedItem);
            UpdateForm<T> updateForm = new UpdateForm<>(this, selectedItem);  // Mở form UpdateForm
            updateForm.setVisible(true);
        }
    }

    // Phương thức gọi khi nhấn nút Xóa
    protected void onDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            // Xác nhận xóa
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa phần tử này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Xóa đối tượng đã chọn
                data.remove(selectedRow);

                // Cập nhật bảng
                updateTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phần tử cần xóa.");
        }
    }

}
