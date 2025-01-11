package org.example.Form;// UpdateForm.java

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

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
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                field.setAccessible(true);

                String value = textFields[i].getText();
                if (field.getType() == String.class) {
                    field.set(currentObject, value);
                } else if (field.getType() == int.class) {
                    field.set(currentObject, Integer.parseInt(value));
                } else if (field.getType() == double.class) {
                    field.set(currentObject, Double.parseDouble(value));
                }
                // Thêm các kiểu dữ liệu khác nếu cần
            }

            // Cập nhật lại dữ liệu trong bảng
            baseForm.updateTable();

            this.dispose();  // Đóng form
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
