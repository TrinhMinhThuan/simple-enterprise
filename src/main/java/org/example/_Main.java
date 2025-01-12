package org.example;

import org.example.DB.ConnectionManagerSingleton;
import org.example.GUI.DBForm.DBConnectionForm;
import org.example.GUI.DBForm.DBTableForm;
import org.example.GUI.Membership.AuthencationForm;
import org.example.GUI.Membership.RegisterForm;

import javax.swing.*;
import java.util.List;

public class _Main {
    public static void main(String[] args) throws Exception{
        Class.forName("org.sqlite.JDBC"); // fix tạm thời
        while (!DBConnectionForm.createForm());
        List<String> tableNames = ConnectionManagerSingleton
                .getInstance().getConnection().getAllEntities();
        while (!DBTableForm.createForm(tableNames));
        AuthencationForm authencationForm = new RegisterForm();
        while (true) {
            if (authencationForm.createForm()) {
                System.out.println("Password changed successfully!");
                break; // Thoát vòng lặp sau khi thay đổi mật khẩu thành công
            } else {
                int retry = JOptionPane.showConfirmDialog(
                        null,
                        "Password change failed. Try again?",
                        "Retry Change Password",
                        JOptionPane.YES_NO_OPTION
                );
                if (retry != JOptionPane.YES_OPTION) {
                    System.out.println("User chose to exit.");
                    break;
                }
            }
        }

    }
}
