package org.example.Output.CRUD;

import org.example.DB.ConnectionManagerSingleton;
import org.example.GUI.Membership.AuthencationForm;
import org.example.GUI.Membership.ChangePasswordForm;
import org.example.TestObject.movies_upcoming;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrudForm<T> extends JFrame {
    private JTable table;
    protected JButton btnAdd, btnUpdate, btnDelete, btnChangePassword;
    private List<T> data;
    protected DefaultTableModel tableModel;

    // Các chiến lược CRUD
    private CrudStrategy<T> addStrategy;
    private CrudStrategy<T> updateStrategy;
    private CrudStrategy<T> deleteStrategy;

    public CrudForm(String title, CrudStrategy<T> addStrategy, CrudStrategy<T> updateStrategy, CrudStrategy<T> deleteStrategy) {
        super(title);

        this.addStrategy = addStrategy;
        this.updateStrategy = updateStrategy;
        this.deleteStrategy = deleteStrategy;

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
        btnChangePassword = new JButton("Đổi mật khẩu");

        // Lắng nghe sự kiện nút bấm
        btnAdd.addActionListener(e -> addStrategy.execute(this));
        btnUpdate.addActionListener(e -> updateStrategy.execute(this));
        btnDelete.addActionListener(e -> deleteStrategy.execute(this));
        btnChangePassword.addActionListener(e -> {
            AuthencationForm changePasswordForm = new ChangePasswordForm();
            changePasswordForm.createForm();
        });

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

    // Cập nhật bảng
    public void updateTable() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        data = (List<T>) ConnectionManagerSingleton.getInstance().getConnection()
                .getAllDataTable(data.get(0).getClass().getSimpleName(), data.get(0).getClass());
        if (!data.isEmpty()) {
            var fields = data.get(0).getClass().getDeclaredFields();
            String[] columnNames = Arrays.stream(fields).map(field -> field.getName()).toArray(String[]::new);
            tableModel.setColumnIdentifiers(columnNames);

            for (T item : data) {
                Object[] row = new Object[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    try {
                        fields[i].setAccessible(true);
                        row[i] = fields[i].get(item);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                tableModel.addRow(row);
            }
        }
    }

    // Getter cho bảng và dữ liệu
    public JTable getTable() {
        return table;
    }

    public List<T> getData() {
        return data;
    }



}
