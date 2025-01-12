package org.example.DB.SQLite;

import java.sql.ResultSet;

import org.example.DB.Record;
import org.example.DB.Table;

public class SQLiteTable extends Table{
    public SQLiteTable(String name, String[] columns) {
        super(name, columns);
    }

    @Override
    public void setRecords(Object resultSet) throws Exception {
        System.out.println("Setting SQLite Table Records.");
        if (resultSet instanceof ResultSet) {
            while (((ResultSet) resultSet).next()) {
                this.records.add(new SQLiteRecord(resultSet));
                System.out.println(resultSet);
            }
        } else {
            System.out.println("Invalid ormRecords type.");
        }
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
