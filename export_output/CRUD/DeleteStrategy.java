

import org.example.DB.ConnectionManagerSingleton;

import javax.swing.*;
import java.util.Objects;

public class DeleteStrategy<T> implements CrudStrategy<T> {
    @Override
    public void execute(CrudForm<T> crudForm, Class<?> clazz) {
        int selectedRow = crudForm.getTable().getSelectedRow();
        if (selectedRow != -1) {
            // Xác nhận xóa
            int confirm = JOptionPane.showConfirmDialog(crudForm, "Bạn có chắc chắn muốn xóa phần tử này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Xóa đối tượng đã chọn
                Object firstColumnValue = crudForm.getTable().getValueAt(selectedRow, 0);
                int index = 0;
                for (int i = 0; i < crudForm.getTable().getColumnCount(); i++) {
                    if (Objects.equals(crudForm.getTable().getColumnName(i), "_id")
                            || Objects.equals(crudForm.getTable().getColumnName(i), "id")) {
                        index = i;
                        break;
                    }
                }
                System.out.println(index);
                String id = crudForm.getTable().getValueAt(crudForm.getTable().getSelectedRow(), index).toString();
                ConnectionManagerSingleton.getInstance()
                        .getConnection().deleteElement(
                                clazz.getSimpleName(),
                                crudForm.getTable().getColumnName(index), id
                        );

                // Cập nhật bảng
                crudForm.getData().remove(selectedRow);
                crudForm.updateTable();
            }
        } else {
            JOptionPane.showMessageDialog(crudForm, "Vui lòng chọn phần tử cần xóa.");
        }
    }
}
