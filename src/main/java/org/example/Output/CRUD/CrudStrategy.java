

// Định nghĩa interface Strategy
public interface CrudStrategy<T> {
    void execute(CrudForm<T> crudForm, Class<?> clazz);
}
