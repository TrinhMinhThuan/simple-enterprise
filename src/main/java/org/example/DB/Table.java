package org.example.DB;

import java.util.ArrayList;

public abstract class Table {
    public String name;
    public String[] columns;
    public ArrayList<Record> records = new ArrayList<>();

    public Table(String name, String[] columns) {
        this.name = name;
        this.columns = columns;
    }

    public ArrayList<Record> getRecords(Record r) { return records; }

    // Phương thức trừu tượng để ánh xạ danh sách records ORM thành ArrayList<Record>
    public abstract void setRecords(Object records) throws Exception; // Adapter Pattern

    public abstract void addRecord(Record r) throws Exception;

    public abstract void deleteRecord(int index) throws Exception;
}
