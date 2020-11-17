package br.com.danieldddl.filesharing.communication.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.danieldddl.filesharing.file.FileContent;

public class FileMessage {

    private final LocalDateTime date;
    private final String encodedBytes;
    private final String filename;

    public FileMessage(FileContent content) {
        this.encodedBytes = content.getEncodedBytes();
        this.filename = content.getFilename();
        this.date = LocalDateTime.now();
    }

    @JsonCreator
    private FileMessage(@JsonProperty("timestamp") LocalDateTime date,
                       @JsonProperty("encodedBytes") String encodedBytes,
                       @JsonProperty("filename") String filename) {

        this.date = date;
        this.encodedBytes = encodedBytes;
        this.filename = filename;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getEncodedBytes() {
        return encodedBytes;
    }

    public String getFilename() {
        return filename;
    }
}
