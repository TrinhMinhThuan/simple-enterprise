package org.example.Export;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class ExportObject {

    public ExportObject() {
    }

    public static void doExport(String className, List<Map<String, String>> attributes, String outputFilePath) throws Exception {
        // Tạo mã nguồn thủ công
        StringBuilder sourceCode = new StringBuilder();
        sourceCode.append("public class ").append(className).append(" {\n");

        // Thêm các thuộc tính và phương thức getter/setter vào mã nguồn
        for (Map<String, String> attribute : attributes) {
            String fieldName = attribute.get("fieldName");
            String fieldType = attribute.get("fieldType"); // Lấy kiểu dữ liệu từ fieldType

            // Thêm thuộc tính với kiểu dữ liệu tương ứng
            sourceCode.append("\tprivate ").append(getType(fieldType)).append(" ").append(fieldName).append(";\n");

            // Getter
//            sourceCode.append("\tpublic ").append(fieldType).append(" get").append(capitalize(fieldName)).append("() {\n");
//            sourceCode.append("\t\treturn this.").append(fieldName).append(";\n");
//            sourceCode.append("\t}\n");
//
//            // Setter
//            sourceCode.append("\tpublic void set").append(capitalize(fieldName)).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n");
//            sourceCode.append("\t\tthis.").append(fieldName).append(" = ").append(fieldName).append(";\n");
//            sourceCode.append("\t}\n\n");
        }

        // Constructor mặc định
        sourceCode.append("\tpublic ").append(className).append("() {}\n");

        sourceCode.append("}\n");

        // Ghi mã nguồn vào file
        writeFile(sourceCode.toString(), outputFilePath);
    }

    // Hỗ trợ viết hoa chữ cái đầu
    private static String getType(String type) {
        if (
                type.equalsIgnoreCase("string")
                || type.equalsIgnoreCase("varchar")
                || type.equalsIgnoreCase("nvarchar")
                || type.equalsIgnoreCase("char")
                || type.equalsIgnoreCase("text")
                || type.equalsIgnoreCase("longtext")
        ) {
            return "String";
        }
        if (
                type.equalsIgnoreCase("integer")
                || type.equalsIgnoreCase("int")
                || type.equalsIgnoreCase("year")
        ) {
            return "Integer";
        }
        if (
                type.equalsIgnoreCase("float")
                || type.equalsIgnoreCase("double")
                || type.equalsIgnoreCase("real")
        ) {
            return "Double";
        }
        if (
                type.equalsIgnoreCase("arraylist")
                || type.equalsIgnoreCase("list")
                || type.equalsIgnoreCase("array")
        ) {
            return "ArrayList";
        }
        if (
                type.equalsIgnoreCase("objectid")
        ) {
            return "ObjectId";
        }
        if (
                type.equalsIgnoreCase("boolean")
                || type.equalsIgnoreCase("bool")
        ) {
            return "ObjectId";
        }
        if (
                type.equalsIgnoreCase("date")
        ) {
            return "LocalDate";
        }
        if (
                type.equalsIgnoreCase("datetime")
        ) {
            return "LocalDateTime";
        }
        if (
                type.equalsIgnoreCase("timestamp")
        ) {
            return "Timestamp";
        }
        return "Object";
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
