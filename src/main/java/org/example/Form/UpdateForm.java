package org.example.Form;// UpdateForm.java

import org.example.DB.ConnectionManagerSingleton;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UpdateForm<T> extends JDialog {

    private JTextField[] textFields;
    private JButton btnSave;
    private BaseForm<T> baseForm;
    private T currentObject;
    private List<Field> fields;

    public UpdateForm(BaseForm<T> baseForm, T currentObject) {
        this.baseForm = baseForm;
        this.currentObject = currentObject;

        setTitle("Cập nhật dữ liệu");
        setModal(true);

        // Lấy các trường của đối tượng T thông qua Reflection
        fields = Arrays.asList(currentObject.getClass().getDeclaredFields());

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

            panel.add(label);
            panel.add(textField);
            if(i == 0) {
                textField.setEnabled(false);
            }

            // Điền sẵn dữ liệu vào text field
            try {
                textField.setText(String.valueOf(field.get(currentObject)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
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
            String id = "";
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                field.setAccessible(true);

                String value = textFields[i].getText();
                if (i == 0) {
                    id = value;
                }

                if (field.getType() == String.class) {
                    field.set(currentObject, value);
                } else if (field.getType() == int.class || field.getType() == Integer.class) {
                    field.set(currentObject, Integer.parseInt(value));
                } else if (field.getType() == double.class || field.getType() == Double.class) {
                    field.set(currentObject, Double.parseDouble(value));
                } else if (field.getType() == Object.class) {
                    // Xử lý Object: có thể là một đối tượng cụ thể hoặc giá trị mặc định
                    if (value != null && !value.isEmpty()) {
                        // Giả sử bạn có một cách để tạo một đối tượng từ chuỗi
                        field.set(currentObject, value);  // Bạn có thể thay đổi cách này tùy thuộc vào loại Object cần gán
                    } else {
                        // Đối với giá trị mặc định hoặc null
                        field.set(currentObject, null);  // Hoặc tạo một đối tượng mặc định nếu cần
                    }
                } else if (field.getType() == ArrayList.class) {
                    field.set(currentObject, value);
                }
                // Thêm các kiểu dữ liệu khác nếu cần
            }

            // Cập nhật lại dữ liệu trong bảng
            baseForm.updateTable();
            ConnectionManagerSingleton.getInstance().getConnection().editElement(
                    baseForm.data.get(0).getClass().getSimpleName(), currentObject,
                    baseForm.table.getColumnName(0), id
            );

            this.dispose();  // Đóng form
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
