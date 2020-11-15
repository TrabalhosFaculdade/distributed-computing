package br.com.danieldddl.filesharing.communication.sender;

import java.util.Objects;

import br.com.danieldddl.filesharing.communication.config.BrokerConnector;
import br.com.danieldddl.filesharing.communication.dto.FileMessage;
import br.com.danieldddl.filesharing.file.FileContent;
import br.com.danieldddl.filesharing.file.FileUtils;
import br.com.danieldddl.filesharing.utils.JsonParser;

public class Sender {

    private final BrokerConnector brokerSender;

    public Sender (final BrokerConnector brokerSender) {
        this.brokerSender = brokerSender;
    }

    public void sendMessage (final String fileLocation, final String receiverUsername) {

        Objects.requireNonNull(fileLocation);

        final FileContent fileContent = FileUtils.retrieve(fileLocation);
        final FileMessage fileMessage = new FileMessage(fileContent);

        brokerSender.sendMessage(receiverUsername, JsonParser.toJson(fileMessage));
    }

}
