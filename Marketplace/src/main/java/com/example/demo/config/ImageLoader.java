package com.example.demo.config;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ImageLoader {

    public void upload(byte[] content, Path destination) throws IOException {
        Files.createDirectories(destination.getParent());
        Files.write(destination, content);
    }

    public FileSystemResource get(Path location) {
        return new FileSystemResource(location);
    }
}
