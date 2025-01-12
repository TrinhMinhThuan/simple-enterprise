package org.example;

import org.example.DB.ConnectionManagerSingleton;
import org.example.DB.MongoDB.MongoDBClient;
import org.example.GUI.FeatureForm.BaseForm;
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
            BaseForm<movies_upcoming> baseForm = new BaseForm<>("Quản lý Dữ liệu");
            baseForm.loadData(moviesList);  // Truyền dữ liệu vào BaseForm
            baseForm.setVisible(true);
        });
    }
}
