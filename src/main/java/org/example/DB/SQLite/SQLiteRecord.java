package org.example.DB.SQLite;

import org.example.DB.Record;

public class SQLiteRecord extends Record{
    public SQLiteRecord(Object ormObj) { super(ormObj); }

    @Override
    public Object[] toJTableRow() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJTableRow'");
    }
}
