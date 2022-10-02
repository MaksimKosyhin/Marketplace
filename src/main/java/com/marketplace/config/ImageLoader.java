package com.marketplace.config;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageLoader {
    private final Path root =
            Paths.get("Marketplace","src", "main", "resources", "static").toAbsolutePath();

    public String save(MultipartFile imgFile, String... dirs) {
        Path fullPath = root;

        for (String dir : dirs) {
            fullPath = fullPath.resolve(dir);
        }

        fullPath = fullPath.resolve(imgFile.getOriginalFilename());

        System.out.println(fullPath);

        try {
            Files.createDirectories(fullPath.getParent());
            Files.write(fullPath, imgFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return root.relativize(fullPath).toString();
    }
}
