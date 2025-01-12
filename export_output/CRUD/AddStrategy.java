package org.example.Output.CRUD;


public class AddStrategy<T> implements CrudStrategy<T> {
    @Override
    public void execute(CrudForm<T> crudForm) {
        AddForm<T> addForm = new AddForm<>(crudForm); // Mở form thêm mới
        addForm.setVisible(true);
    }
}
