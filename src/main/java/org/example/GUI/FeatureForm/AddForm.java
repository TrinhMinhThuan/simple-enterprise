package org.example.GUI.FeatureForm;


import org.example.DB.ConnectionManagerSingleton;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        setSize(800, 600);
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
                } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                    field.set(newObject, Boolean.parseBoolean(value));
                } else if (field.getType() == Object.class) {
                    // Xử lý Object: có thể là một đối tượng cụ thể hoặc giá trị mặc định
                    if (value != null && !value.isEmpty()) {
                        // Giả sử bạn có một cách để tạo một đối tượng từ chuỗi
                        field.set(newObject, value);  // Bạn có thể thay đổi cách này tùy thuộc vào loại Object cần gán
                    } else {
                        // Đối với giá trị mặc định hoặc null
                        field.set(newObject, null);  // Hoặc tạo một đối tượng mặc định nếu cần
                    }
                } else if (field.getType() == ArrayList.class && value instanceof String) {
                    String stringValue = (String) value;

                    // Loại bỏ dấu ngoặc vuông ở đầu và cuối chuỗi
                    if (stringValue.length() > 2) {
                        stringValue = stringValue.substring(1, stringValue.length() - 1); // Loại bỏ ký tự đầu và cuối
                    }

                    // Tách chuỗi thành các phần tử, đảm bảo không bị chia nhỏ lỗi
                    String[] items = stringValue.split(",\\s*"); // Tách chuỗi theo dấu phẩy và khoảng trắng

                    ArrayList<Object> convertedList = new ArrayList<>();

                    // Duyệt qua từng phần tử và chuyển thành Object
                    for (String item : items) {
                        item = item.trim(); // Loại bỏ khoảng trắng ở đầu và cuối

                        // Kiểm tra nếu phần tử có thể chuyển thành số nguyên hoặc số thập phân
                        try {
                            // Kiểm tra xem nếu giá trị là một số nguyên
                            if (item.matches("-?\\d+")) {
                                convertedList.add(Integer.valueOf(item)); // Chuyển thành Integer
                            }
                            // Kiểm tra xem nếu giá trị là một số thập phân
                            else if (item.matches("-?\\d*\\.\\d+")) {
                                convertedList.add(Double.valueOf(item)); // Chuyển thành Double
                            }
                            else {
                                convertedList.add(item); // Nếu không phải số, giữ lại giá trị dạng String
                            }
                        } catch (NumberFormatException e) {
                            convertedList.add(item); // Nếu không thể chuyển đổi, giữ giá trị dưới dạng String
                        }
                    }

                    // Gán danh sách đã chuyển đổi vào trường của đối tượng
                    field.set(newObject, convertedList);
                }
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
