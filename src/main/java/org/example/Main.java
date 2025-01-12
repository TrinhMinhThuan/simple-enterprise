package org.example;

import org.example.CRUD.AddStrategy;
import org.example.CRUD.CrudForm;
import org.example.CRUD.DeleteStrategy;
import org.example.CRUD.EditStrategy;
import org.example.DB.ConnectionManagerSingleton;
import org.example.DB.MongoDB.MongoDBClient;
import org.example.TestObject.movies_upcoming;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{
        Class.forName("org.sqlite.JDBC"); // fix tạm thời
//        DatabaseConnectionForm form = new DatabaseConnectionForm();
//        new DatabaseConnectionHandler(form);
        ConnectionManagerSingleton.setConnetion(new MongoDBClient());
        ConnectionManagerSingleton.openConnection(
                "cluster0.dltte.mongodb.net",
                "user01",
                "010101",
                "tmdb_db"
        );
        // Tạo danh sách movie_genres
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
