package br.com.danieldddl.filesharing.file;

public class FileContent {

    private final String encodedBytes;
    private final String filename;

    public FileContent(String encodedBytes, String filename) {
        this.encodedBytes = encodedBytes;
        this.filename = filename;
    }

    public String getEncodedBytes() {
        return encodedBytes;
    }

    public String getFilename() {
        return filename;
    }
}
