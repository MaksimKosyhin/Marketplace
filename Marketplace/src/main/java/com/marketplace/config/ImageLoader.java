package com.marketplace.config;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageLoader {
    private final Path imgDirectory = Paths.get("img");

    public String save(byte[] content, String... dirs) throws IOException {
        Path fullPath = imgDirectory;

        for (String dir : dirs) {
            fullPath.resolve(dir);
        }

        Files.createDirectories(fullPath.getParent());
        Files.write(fullPath, content);

        return fullPath.toAbsolutePath().toString();
    }

    public FileSystemResource findInFileSystem(String location) {
        try {
            return new FileSystemResource(Paths.get(location));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
