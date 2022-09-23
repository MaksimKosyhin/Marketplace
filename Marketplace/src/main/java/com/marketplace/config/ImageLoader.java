package com.marketplace.config;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageLoader {
    private final Path imgDirectory = Paths.get("img");

    public String save(MultipartFile imgFile, String... dirs) throws IOException {
        Path fullPath = imgDirectory;

        for (String dir : dirs) {
            fullPath.resolve(dir);
        }

        fullPath.resolve(imgFile.getContentType());

        Files.createDirectories(fullPath.getParent());
        Files.write(fullPath, imgFile.getBytes());

        return fullPath.toAbsolutePath().toString();
    }

    public FileSystemResource toFileSystemResource(String location) {
        try {
            return new FileSystemResource(Paths.get(location));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
