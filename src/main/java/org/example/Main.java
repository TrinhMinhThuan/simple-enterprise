package org.example;

import org.example.DB.ConnectionManagerSingleton;
import org.example.DB.DBConnection.MongoDBConnection;
import org.example.Form.BaseForm;
import org.example.GUI.DatabaseConnectionForm;
import org.example.GUI.DatabaseConnectionHandler;
import org.example.TestObject.movie_genres;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{
        Class.forName("org.sqlite.JDBC"); // fix tạm thời
        ConnectionManagerSingleton.setConnetion(new MongoDBConnection());
        ConnectionManagerSingleton.openConnection(
                "cluster0.dltte.mongodb.net",
                "user01",
                "010101",
                "tmdb_db"
        );
        // Tạo danh sách movie_genres
        List<movie_genres> genresList = ConnectionManagerSingleton.getInstance().getConnection()
                .getAllDataTable("movie_genres", movie_genres.class);


        SwingUtilities.invokeLater(() -> {
            BaseForm<movie_genres> baseForm = new BaseForm<>("Quản lý Dữ liệu");
            baseForm.loadData(genresList);  // Truyền dữ liệu vào BaseForm
            baseForm.setVisible(true);
        });
    }
}
