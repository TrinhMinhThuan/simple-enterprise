package org.example.Export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExportMainClass {

    public static void doExport(String className, String outputFilePath) throws Exception {
        StringBuilder sourceCode = new StringBuilder();

        // Phần import
        sourceCode.append("import org.example.DB.ConnectionManagerSingleton;\n")
                .append("import org.example.DB.DBClient;\n")
                .append("import org.example.GUI.DBForm.DBConnectionForm;\n")
                .append("import org.example.GUI.Membership.AuthencationForm;\n")
                .append("import org.example.GUI.Membership.LoginForm;\n\n")
                .append("import javax.swing.*;\n")
                .append("import java.util.List;\n")
                .append("import java.util.concurrent.atomic.AtomicReference;\n\n");

        // Phần class Main
        sourceCode.append("public class Main {\n\n");

        // Phần phương thức main
        sourceCode.append("\tpublic static void main(String[] args) throws Exception{\n")
                .append("\t\tClass.forName(\"org.sqlite.JDBC\");\n")
                .append("\t\tboolean isLogin = false;\n")
                .append("\t\tAuthencationForm authencationForm = new LoginForm();\n")
                .append("\t\twhile (true) {\n")
                .append("\t\t\tif (authencationForm.createForm()) {\n")
                .append("\t\t\t\tisLogin = true;\n")
                .append("\t\t\t\tbreak;\n")
                .append("\t\t\t} else {\n")
                .append("\t\t\t\tint retry = JOptionPane.showConfirmDialog(\n")
                .append("\t\t\t\t\tnull,\n")
                .append("\t\t\t\t\t\"Are you exit?\",\n")
                .append("\t\t\t\t\t\"Exit\",\n")
                .append("\t\t\t\t\tJOptionPane.YES_NO_OPTION\n")
                .append("\t\t\t\t);\n")
                .append("\t\t\t\tif (retry == JOptionPane.YES_OPTION) {\n")
                .append("\t\t\t\t\tSystem.out.println(\"User chose to exit.\");\n")
                .append("\t\t\t\t\tbreak;\n")
                .append("\t\t\t\t}\n")
                .append("\t\t\t}\n")
                .append("\t\t}\n\n");

        // Phần logic nếu login thành công
        sourceCode.append("\t\tif (isLogin) {\n")
                .append("\t\t\tAtomicReference<DBClient> connectionRef = new AtomicReference<>();\n")
                .append("\t\t\tDBConnectionForm.createForm(connectionRef);\n")
                .append("\t\t\tConnectionManagerSingleton.getInstance().closeConnection();\n")
                .append("\t\t\tConnectionManagerSingleton.setConnetion(connectionRef.get());\n\n");

        // Phần lấy dữ liệu từ database
        sourceCode.append("\t\t\tList<").append(className).append("> itemsList = ConnectionManagerSingleton.getInstance().getConnection()\n")
                .append("\t\t\t\t\t.getAllDataTable(\"").append(className.toLowerCase()).append("\", ").append(className).append(".class);\n\n");

        // Phần tạo form CRUD
        sourceCode.append("\t\t\tCrudForm<").append(className).append("> form = new CrudForm<>(\n")
                .append("\t\t\t\t\"Quản lý dữ liệu\",\n")
                .append("\t\t\t\t").append(className).append(".class,\n")
                .append("\t\t\t\tnew AddStrategy<>(),\n")
                .append("\t\t\t\tnew EditStrategy<>(),\n")
                .append("\t\t\t\tnew DeleteStrategy<>()\n")
                .append("\t\t\t);\n\n");

        // Phần load dữ liệu vào form
        sourceCode.append("\t\t\tform.loadData(itemsList);\n")
                .append("\t\t\tform.setVisible(true);\n")
                .append("\t\t}\n")
                .append("\t}\n")
                .append("}\n");

        // Ghi mã nguồn vào file
        writeFile(sourceCode.toString(), outputFilePath);
    }

    private static void writeFile(String sourceCode, String outputFilePath) {
        try {
            // Tạo đối tượng File từ đường dẫn
            File outputFile = new File(outputFilePath);

            // Lấy thư mục cha từ đường dẫn file
            File parentDirectory = outputFile.getParentFile();

            // Kiểm tra và tạo thư mục nếu chưa tồn tại
            if (parentDirectory != null && !parentDirectory.exists()) {
                boolean created = parentDirectory.mkdirs();
                if (created) {
                    System.out.println("Thư mục đã được tạo: " + parentDirectory.getAbsolutePath());
                } else {
                    System.out.println("Không thể tạo thư mục: " + parentDirectory.getAbsolutePath());
                }
            }

            // Ghi mã nguồn vào file
            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write(sourceCode);
                System.out.println("Mã nguồn đã được ghi vào file: " + outputFilePath);
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi mã nguồn vào file: " + e.getMessage());
        }
    }
}
