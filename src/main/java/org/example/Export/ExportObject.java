package org.example.Export;


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
            sourceCode.append("\tprivate ").append(fieldType).append(" ").append(fieldName).append(";\n");

            // Getter
            sourceCode.append("\tpublic ").append(fieldType).append(" get").append(capitalize(fieldName)).append("() {\n");
            sourceCode.append("\t\treturn this.").append(fieldName).append(";\n");
            sourceCode.append("\t}\n");

            // Setter
            sourceCode.append("\tpublic void set").append(capitalize(fieldName)).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n");
            sourceCode.append("\t\tthis.").append(fieldName).append(" = ").append(fieldName).append(";\n");
            sourceCode.append("\t}\n\n");
        }

        // Constructor mặc định
        sourceCode.append("\tpublic ").append(className).append("() {}\n");

        sourceCode.append("}\n");

        // Ghi mã nguồn vào file
        writeFile(sourceCode.toString(), outputFilePath);
    }

    // Hỗ trợ viết hoa chữ cái đầu
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static void writeFile(String sourceCode, String outputFilePath) {
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write(sourceCode);  // Ghi mã nguồn vào file
            System.out.println("Mã nguồn đã được ghi vào file: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi mã nguồn vào file: " + e.getMessage());
        }
    }

}
