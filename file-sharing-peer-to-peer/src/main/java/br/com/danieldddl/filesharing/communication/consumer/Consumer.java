package br.com.danieldddl.filesharing.communication.consumer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

import br.com.danieldddl.filesharing.communication.config.BrokerConnector;
import br.com.danieldddl.filesharing.communication.dto.FileMessage;
import br.com.danieldddl.filesharing.file.FileUtils;
import br.com.danieldddl.filesharing.utils.JsonParser;

import static br.com.danieldddl.filesharing.utils.PrintUtils.print;

public class Consumer {

    private final BrokerConnector connector;
    private final String username;
    private final File savingDirectory;

    public Consumer(final BrokerConnector connector, final String username, final String savingDirectoryLocation) {

        Objects.requireNonNull(connector);
        Objects.requireNonNull(username);
        Objects.requireNonNull(savingDirectoryLocation);

        this.connector = connector;
        this.username = username;
        this.savingDirectory = directoryFrom(savingDirectoryLocation);
    }

    public void start () {

        connector.declareQueue(username);
        connector.startConsuming(username, ((consumerTag, message) -> {

            print("Message received");

            final FileMessage incomingMessage = JsonParser.fromJson(message.getBody(), FileMessage.class);

            final String newFilename = filenameFrom(incomingMessage);
            final Path incomingFileLocation = Paths.get(savingDirectory.toString(), newFilename);

            FileUtils.persist(incomingFileLocation.toFile(), incomingMessage.getEncodedBytes());

        }));
    }

    private File directoryFrom (final String location) {

        final File savingDirectory = new File(location);
        if (!FileUtils.existsAsDirectory(savingDirectory)) {
            final String message = "Directory informed doesn't exist: %s";
            throw new IllegalArgumentException(String.format(message, location));
        }

        return savingDirectory;
    }

    private String filenameFrom (final FileMessage incomingMessage) {

        final String format = "%d-%s-%s";

        final Long epoch = epochFromDate(incomingMessage.getDate());
        final String uuid = UUID.randomUUID().toString();

        return String.format(format, epoch, uuid, incomingMessage.getFilename());
    }

    private Long epochFromDate (final LocalDateTime date) {
        final ZoneId zone = ZoneId.systemDefault();
        return date.atZone(zone).toEpochSecond();
    }

}
