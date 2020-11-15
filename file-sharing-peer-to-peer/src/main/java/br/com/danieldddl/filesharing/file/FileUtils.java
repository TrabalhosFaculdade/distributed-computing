package br.com.danieldddl.filesharing.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Objects;

public class FileUtils {

    public static FileContent retrieve (final String location) {

        Objects.requireNonNull(location, "Location cannot be null");

        final File file = new File(location);
        if (!existsAsFile(file)) {
            final String message = "Error: could not find file for location informed: %s";
            throw new IllegalArgumentException(String.format(message, location));
        }

        final byte[] bytes = bytesFrom(file);
        final String encodedString = Base64.getEncoder().encodeToString(bytes);

        return new FileContent(encodedString, file.getName());
    }

    public static void persist (final File file, String encodedContent) {

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {

            byte[] decodedFile = Base64.getDecoder().decode(encodedContent);
            fileOutputStream.write(decodedFile);

        } catch (IOException e) {
            throw new IllegalStateException("Error while appending object bytes serialization to file: ", e);
        }

    }

    private static byte[] bytesFrom(final File file) {

        try {
            return Files.readAllBytes(file.toPath());
        } catch (final IOException e) {
            final String message = "Error while reading file: %s";
            throw new IllegalStateException(String.format(message, file.getAbsolutePath()), e);
        }
    }

    public static boolean existsAsFile (final File file) {
        return file.exists() && !file.isDirectory();
    }

    public static boolean existsAsDirectory (final File file) {
        return file.exists() && file.isDirectory();
    }

}
