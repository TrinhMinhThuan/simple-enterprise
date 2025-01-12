package org.example.Export;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FolderCopy {
    public static void copyFolder(String sourceDir, String targetDir) {
        Path sourcePath = Paths.get(sourceDir);
        Path targetPath = Paths.get(targetDir);

        try {
            // Kiểm tra nếu thư mục đích chưa tồn tại, tạo thư mục
            if (!Files.exists(targetPath)) {
                Files.createDirectories(targetPath);
            }

            // Duyệt qua tất cả các tệp và thư mục trong thư mục nguồn
            Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // Sao chép từng tệp
                    Path targetFile = targetPath.resolve(sourcePath.relativize(file));
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    // Tạo thư mục con trong thư mục đích
                    Path targetDir = targetPath.resolve(sourcePath.relativize(dir));
                    if (!Files.exists(targetDir)) {
                        Files.createDirectories(targetDir);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println("Sao chép thành công từ " + sourceDir + " sang " + targetDir);
        } catch (IOException e) {
            System.err.println("Lỗi khi sao chép thư mục: " + e.getMessage());
        }
    }
}
