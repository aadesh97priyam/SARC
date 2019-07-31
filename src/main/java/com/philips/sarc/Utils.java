package com.philips.sarc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class Utils {
    public static String getFileContents(File optionFile) {
        if (optionFile != null && optionFile.exists() && Files.isExecutable(optionFile.toPath())) {
            StringBuilder fileContents = new StringBuilder();
            if (optionFile.isDirectory())
                throw new RuntimeException("[ERROR] Cannot read contents of file provided");
            
            try (BufferedReader reader = new BufferedReader(new FileReader(optionFile))) {
                
                String line = null;
                while ((line  = reader.readLine()) != null) {
                    fileContents.append(line + "\n");
                }
            } catch (IOException ioe) {
                System.out.println("[ERROR] Couldn't read from file");
                return null;
            }
            return fileContents.toString();
        }
        return null;
    }

    public static void writeFileContents(File optionFile, String contents) {
        try {
            String name = optionFile.getName();
            if (!optionFile.exists() && name != null && name.length() > 0) {
                optionFile.createNewFile();
            }
        } catch (IOException ioe) {
            throw new RuntimeException("[ERROR] Something went wrong during file creation!");
        }

        if (optionFile != null && optionFile.isFile()) {
            if (contents == null) contents = "";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(optionFile))) {
                writer.write(contents);
            } catch (IOException ioe) {
                System.out.println("[ERROR] Couldn't write to file");
            }
        } else {
            throw new RuntimeException("[ERROR] Invalid file provided!");
        }
    }

    public static String getStreamContents(InputStream is) {
        if (is != null) {
            StringBuilder fileContents = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                
                String line = null;
                while ((line  = reader.readLine()) != null) {
                    fileContents.append(line + "\n");
                }
            } catch (IOException ioe) {
                return null;
            }
            int length = fileContents.length();
            fileContents.delete(length-1, length);
            return fileContents.toString();
        }

        return null;
    }

    public static void deleteDirectoryRecursion(Path path) throws IOException {
        if (path != null && path.toFile().exists()) {
            if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                    for (Path entry : entries) {
                    deleteDirectoryRecursion(entry);
                    }
                }
            }
            Files.delete(path);
        }
    }
}