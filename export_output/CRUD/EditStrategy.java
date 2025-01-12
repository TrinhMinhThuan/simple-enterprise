package org.example.Output.CRUD;

import javax.swing.*;

public class EditStrategy<T> implements CrudStrategy<T> {
    @Override
    public void execute(CrudForm<T> crudForm) {
        int selectedRow = crudForm.getTable().getSelectedRow();
        if (selectedRow != -1) {
            T selectedItem = crudForm.getData().get(selectedRow);
            EditForm<T> editForm = new EditForm<>(crudForm, selectedItem); // Mở form cập nhật
            editForm.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(crudForm, "Vui lòng chọn phần tử cần cập nhật.");
        }
    }
}
