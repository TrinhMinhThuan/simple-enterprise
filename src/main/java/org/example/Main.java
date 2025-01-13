package org.example;

import org.example.DB.ConnectionManagerSingleton;
import org.example.DB.DBClient;
import org.example.Export.ExportMainClass;
import org.example.Export.ExportObject;
import org.example.Export.FolderCopy;
import org.example.GUI.DBForm.DBConnectionForm;
import org.example.GUI.DBForm.DBTableForm;
import org.example.GUI.Membership.AuthencationForm;
import org.example.GUI.Membership.RegisterForm;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) throws Exception{
        Class.forName("org.sqlite.JDBC"); // fix tạm thời

        FolderCopy.copyFolder("src/main/java/org/example/Output/", "export_output/");
        AtomicReference<DBClient> connectionRef = new AtomicReference<>();
        DBConnectionForm.createForm(connectionRef);
        ConnectionManagerSingleton.getInstance().closeConnection();
        ConnectionManagerSingleton.setConnetion(connectionRef.get());
        List<String> tableNames = ConnectionManagerSingleton
                .getInstance().getConnection().getAllEntities();
        String chooseTableName =  DBTableForm.createForm(tableNames);
        List<Map<String, String>> objInfor =  ConnectionManagerSingleton
                .getInstance()
                .getConnection()
                .getAllFieldName(chooseTableName);
        ExportObject.doExport(chooseTableName, objInfor, "export_output/Object/" + chooseTableName + ".java");
        ExportMainClass.doExport(chooseTableName, "export_output/Main.java");
        JOptionPane.showMessageDialog(null, "Export done!");
        AuthencationForm authencationForm = new RegisterForm();
        while (true) {
            if (authencationForm.createForm()) {
                break;
            } else {
                int retry = JOptionPane.showConfirmDialog(
                        null,
                        "Are you exit?",
                        "Exit",
                        JOptionPane.YES_NO_OPTION
                );
                if (retry == JOptionPane.YES_OPTION) {
                    System.out.println("User chose to exit.");
                    break;
                }
            }
        }
    }
}
