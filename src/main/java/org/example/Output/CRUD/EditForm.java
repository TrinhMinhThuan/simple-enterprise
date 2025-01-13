

import org.example.DB.ConnectionManagerSingleton;


import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EditForm<T> extends JDialog {

    private JTextField[] textFields;
    private JButton btnSave;
    private CrudForm<T> crudForm;
    private T currentObject;
    private List<Field> fields;
    private Class<?> clazz;

    public EditForm(CrudForm<T> crudForm, T currentObject, Class<?> clazz) {
        this.crudForm = crudForm;
        this.currentObject = currentObject;
        this.clazz = clazz;

        setTitle("Cập nhật dữ liệu");
        setModal(true);

        // Lấy các trường của đối tượng T thông qua Reflection
        fields = Arrays.asList(clazz.getDeclaredFields());

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

        setSize(800, 600);
        setLocationRelativeTo(crudForm);
    }

    private void onSave() {
        try {
            String id = "";
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                field.setAccessible(true);

                String value = textFields[i].getText();

                if (value != null && !value.isEmpty() && !value.equals("null")) {
                    if (field.getType() == String.class) {
                        field.set(currentObject, value);
                    } else if (field.getType() == int.class || field.getType() == Integer.class) {
                        field.set(currentObject, Integer.parseInt(value));
                    } else if (field.getType() == double.class || field.getType() == Double.class) {
                        field.set(currentObject, Double.parseDouble(value));
                    } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                        field.set(currentObject, Boolean.parseBoolean(value));
                    } else if (field.getType() == Object.class) {
                        // Xử lý Object: có thể là một đối tượng cụ thể hoặc giá trị mặc định
                        field.set(currentObject, value);
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
                                } else {
                                    convertedList.add(item); // Nếu không phải số, giữ lại giá trị dạng String
                                }
                            } catch (NumberFormatException e) {
                                convertedList.add(item); // Nếu không thể chuyển đổi, giữ giá trị dưới dạng String
                            }
                        }
                        // Gán danh sách đã chuyển đổi vào trường của đối tượng
                        field.set(currentObject, convertedList);
                    }
                } else {
                    field.set(currentObject, null);
                }
            }

            int index = 0;
            for (int i = 0; i < crudForm.getTable().getColumnCount(); i++) {
                if (Objects.equals(crudForm.getTable().getColumnName(i), "_id")
                        || Objects.equals(crudForm.getTable().getColumnName(i), "id")) {
                    index = i;
                    break;
                }
            }

            ConnectionManagerSingleton.getInstance().getConnection().editElement(
                    clazz.getSimpleName(), currentObject,
                    crudForm.getTable().getColumnName(index),
                    crudForm.getTable().getValueAt(crudForm.getTable().getSelectedRow(), index)
            );
            crudForm.updateTable();


            this.dispose();  // Đóng form
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
