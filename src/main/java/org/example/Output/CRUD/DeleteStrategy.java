package org.example.Output.CRUD;

import org.example.DB.ConnectionManagerSingleton;

import javax.swing.*;

public class DeleteStrategy<T> implements CrudStrategy<T> {
    @Override
    public void execute(CrudForm<T> crudForm) {
        int selectedRow = crudForm.getTable().getSelectedRow();
        if (selectedRow != -1) {
            // Xác nhận xóa
            int confirm = JOptionPane.showConfirmDialog(crudForm, "Bạn có chắc chắn muốn xóa phần tử này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Xóa đối tượng đã chọn
                Object firstColumnValue = crudForm.getTable().getValueAt(selectedRow, 0);
                String id = firstColumnValue.toString();
                ConnectionManagerSingleton.getInstance()
                        .getConnection().deleteElement(
                                crudForm.getData().get(0).getClass().getSimpleName(),
                                crudForm.getTable().getColumnName(0), id
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
