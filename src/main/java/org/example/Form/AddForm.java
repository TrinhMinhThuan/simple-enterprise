package org.example.Form;


import org.example.DB.ConnectionManagerSingleton;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddForm<T> extends JDialog {

    private JTextField[] textFields;
    private JButton btnSave;
    private BaseForm<T> baseForm;
    private List<Field> fields;

    public AddForm(BaseForm<T> baseForm) {
        this.baseForm = baseForm;

        setTitle("Thêm mới dữ liệu");
        setModal(true);

        // Lấy các trường của đối tượng T thông qua Reflection từ đối tượng đầu tiên trong danh sách
        fields = Arrays.asList(baseForm.data.get(0).getClass().getDeclaredFields());

        // Tạo các text field và labels
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(fields.size() + 1, 2));

        textFields = new JTextField[fields.size()];

        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            field.setAccessible(true);

            JLabel label = new JLabel(field.getName());
            JTextField textField = new JTextField();
            textFields[i] = textField;

            if(i == 0) {
                textField.setEnabled(false);
            }

            panel.add(label);
            panel.add(textField);
        }

        btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> onSave());

        // Thêm các thành phần vào form
        add(panel, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(baseForm);
    }

    private void onSave() {
        try {
            // Tạo đối tượng mới từ lớp T
            T newObject = (T) baseForm.data.get(0).getClass().getDeclaredConstructor().newInstance();

            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                field.setAccessible(true);

                String value = textFields[i].getText();
                if (field.getType() == String.class) {
                    field.set(newObject, value);
                } else if (field.getType() == int.class || field.getType() == Integer.class) {
                    field.set(newObject, Integer.parseInt(value));
                } else if (field.getType() == double.class || field.getType() == Double.class) {
                    field.set(newObject, Double.parseDouble(value));
                } else if (field.getType() == Object.class) {
                    // Xử lý Object: có thể là một đối tượng cụ thể hoặc giá trị mặc định
                    if (value != null && !value.isEmpty()) {
                        // Giả sử bạn có một cách để tạo một đối tượng từ chuỗi
                        field.set(newObject, value);  // Bạn có thể thay đổi cách này tùy thuộc vào loại Object cần gán
                    } else {
                        // Đối với giá trị mặc định hoặc null
                        field.set(newObject, null);  // Hoặc tạo một đối tượng mặc định nếu cần
                    }
                } else if (field.getType() == ArrayList.class) {
                    field.set(newObject, value);
                }
                // Thêm các kiểu dữ liệu khác nếu cần
            }

            // Cập nhật dữ liệu vào BaseForm
            baseForm.data.add(newObject);
            baseForm.updateTable();
            ConnectionManagerSingleton.getInstance().getConnection().addElement(
                    baseForm.data.get(0).getClass().getSimpleName(), newObject
            );

            this.dispose();  // Đóng form
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
