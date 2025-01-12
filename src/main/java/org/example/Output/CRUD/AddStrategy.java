package org.example.Output.CRUD;


public class AddStrategy<T> implements CrudStrategy<T> {
    @Override
    public void execute(CrudForm<T> crudForm, Class<?> clazz) {
        AddForm<T> addForm = new AddForm<>(crudForm, clazz); // Mở form thêm mới
        addForm.setVisible(true);
    }
}
