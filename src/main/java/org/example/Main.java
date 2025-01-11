package org.example;

import org.example.GUI.DatabaseConnectionForm;
import org.example.GUI.DatabaseConnectionHandler;

public class Main {
    public static void main(String[] args) throws Exception{
        Class.forName("org.sqlite.JDBC"); // fix tạm thời
        DatabaseConnectionForm form = new DatabaseConnectionForm();
        new DatabaseConnectionHandler(form);
    }
}
