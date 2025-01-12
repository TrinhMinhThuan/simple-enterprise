package org.example;

import org.example.DB.ConnectionManagerSingleton;
import org.example.DB.MongoDB.MongoDBClient;
import org.example.Form.BaseForm;
import org.example.GUI.DatabaseConnectionForm;
import org.example.GUI.DatabaseConnectionHandler;
import org.example.TestObject.movie_genres;
import org.example.TestObject.movie_rating_list;
import org.example.TestObject.movies;

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
        List<movie_rating_list> moviesList = ConnectionManagerSingleton.getInstance().getConnection()
                .getAllDataTable("movie_rating_list", movie_rating_list.class);


        SwingUtilities.invokeLater(() -> {
            BaseForm<movie_rating_list> baseForm = new BaseForm<>("Quản lý Dữ liệu");
            baseForm.loadData(moviesList);  // Truyền dữ liệu vào BaseForm
            baseForm.setVisible(true);
        });
    }
}
