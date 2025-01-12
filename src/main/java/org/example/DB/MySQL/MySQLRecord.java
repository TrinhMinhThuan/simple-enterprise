package org.example.DB.MySQL;

import org.example.DB.Record;

public class MySQLRecord extends Record{
    public MySQLRecord(Object ormObj) { super(ormObj); }

    @Override
    public Object[] toJTableRow() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJTableRow'");
    }
}
