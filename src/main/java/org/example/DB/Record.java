package org.example.DB;

// import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;

public abstract class Record { // ORM Object Wrapper
    private final PropertyChangeSupport support;
    private Object obj; // đối tượng ORM hoặc Map

    public Record(Object obj) {
        this.obj = obj;
        this.support = new PropertyChangeSupport(this.obj);
    }

    public void updateObj(String fieldName, Object value) throws Exception {
        Field field = this.obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Object oldValue = field.get(obj);
        support.firePropertyChange(fieldName, oldValue, value);
    }

    public Object getObject() { return obj; }

    public abstract Object[] toJTableRow();

    // // Đăng ký listener (khi mở form update của record)
    // public void addPropertyChangeListener(PropertyChangeListener listener) {
    //     support.addPropertyChangeListener(listener);
    // }
}