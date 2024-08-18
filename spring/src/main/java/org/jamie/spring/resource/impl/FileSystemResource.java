package org.jamie.spring.resource.impl;

import org.jamie.spring.resource.Resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * 文件系统资源
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/20 00:31
 */
public class FileSystemResource implements Resource {

    private final String filePath;

    public FileSystemResource(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        try {
            Path path = new File(this.filePath).toPath();
            return Files.newInputStream(path);
        } catch (NoSuchFileException ex) {
            throw new FileNotFoundException(ex.getMessage());
        }
    }
}
