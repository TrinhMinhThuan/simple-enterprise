package org.example.DB.MySQL;

import org.example.DB.Record;
import org.example.DB.Table;

public class MySQLTable extends Table{
    public MySQLTable(String name, String[] columns) {
        super(name, columns);
    }

    @Override
    public void setRecords(Object records) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setRecords'");
    }

    @Override
    public void addRecord(Record r) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addRecord'");
    }

    @Override
    public void deleteRecord(int index) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
