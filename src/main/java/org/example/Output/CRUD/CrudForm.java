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
    private JButton btnAdd, btnUpdate, btnDelete, btnChangePassword;
    private List<T> data;
    private DefaultTableModel tableModel;
    private Class<T> clazz;

    // Các chiến lược CRUD
    private CrudStrategy<T> addStrategy;
    private CrudStrategy<T> updateStrategy;
    private CrudStrategy<T> deleteStrategy;

    public CrudForm(String title,
                    Class<T> clazz,
                    CrudStrategy<T> addStrategy,
                    CrudStrategy<T> updateStrategy,
                    CrudStrategy<T> deleteStrategy) {
        super(title);
        this.clazz = clazz;
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
        btnAdd.addActionListener(e -> addStrategy.execute(this, clazz));
        btnUpdate.addActionListener(e -> updateStrategy.execute(this, clazz));
        btnDelete.addActionListener(e -> deleteStrategy.execute(this, clazz));
        btnChangePassword.addActionListener(e -> changePassword());

        // Đặt Layout cho JFrame
        JPanel panel = new JPanel();
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnChangePassword);

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
        data = ConnectionManagerSingleton.getInstance().getConnection()
                .getAllDataTable(clazz.getSimpleName(), clazz);
        var fields = clazz.getDeclaredFields();
        String[] columnNames = Arrays.stream(fields).map(field -> field.getName()).toArray(String[]::new);
        tableModel.setColumnIdentifiers(columnNames);
        if (!data.isEmpty()) {
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

    public void changePassword() {
        AuthencationForm form = new ChangePasswordForm();
        form.createForm();
    }

    // Getter cho bảng và dữ liệu
    public JTable getTable() {
        return table;
    }

    public List<T> getData() {
        return data;
    }



}
