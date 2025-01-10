package org.example;

import org.example.GUI.DatabaseConnectionForm;
import org.example.GUI.DatabaseConnectionHandler;

public class Main {
    public static void main(String[] args) {
        DatabaseConnectionForm form = new DatabaseConnectionForm();
        new DatabaseConnectionHandler(form);
    }
}
