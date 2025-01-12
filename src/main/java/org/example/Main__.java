package org.example;

import org.example.DB.ConnectionManagerSingleton;
import org.example.DB.DBClient;
import org.example.GUI.DBForm.DBConnectionForm;
import org.example.Output.CRUD.AddStrategy;
import org.example.Output.CRUD.CrudForm;
import org.example.Output.CRUD.DeleteStrategy;
import org.example.Output.CRUD.EditStrategy;
import org.example.TestObject.movies_upcoming;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class Main__ {

    private static final String TABLE_NAME = "";

    public static void main(String[] args) throws Exception{
        Class.forName("org.sqlite.JDBC");

//        AuthencationForm authencationForm = new LoginForm();
//        while (true) {
//            if (authencationForm.createForm()) {
//                break;
//            } else {
//                int retry = JOptionPane.showConfirmDialog(
//                        null,
//                        "Are you exit?",
//                        "Exit",
//                        JOptionPane.YES_NO_OPTION
//                );
//                if (retry == JOptionPane.YES_OPTION) {
//                    System.out.println("User chose to exit.");
//                    break;
//                }
//            }
//        }

        AtomicReference<DBClient> connectionRef = new AtomicReference<>();
        DBConnectionForm.createForm(connectionRef);
        ConnectionManagerSingleton.getInstance().closeConnection();
        ConnectionManagerSingleton.setConnetion(connectionRef.get());



        List<movies_upcoming> moviesList = ConnectionManagerSingleton.getInstance().getConnection()
                .getAllDataTable("movies_upcoming", movies_upcoming.class);

        SwingUtilities.invokeLater(() -> {
            // Tạo form với các chiến lược
            CrudForm<movies_upcoming> form = new CrudForm<>(
                    "Quản lý dữ liệu",
                    new AddStrategy<>(),
                    new EditStrategy<>(),
                    new DeleteStrategy<>()
            );

            // Load dữ liệu vào form
            form.loadData(moviesList);
            form.setVisible(true);
        });
    }
}
