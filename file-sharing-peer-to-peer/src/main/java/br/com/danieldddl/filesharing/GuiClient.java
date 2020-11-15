package br.com.danieldddl.filesharing;

import java.util.Scanner;

import br.com.danieldddl.filesharing.communication.config.BrokerConnector;
import br.com.danieldddl.filesharing.communication.consumer.Consumer;
import br.com.danieldddl.filesharing.communication.sender.Sender;

import static br.com.danieldddl.filesharing.utils.PrintUtils.print;

public class GuiClient {

    private static final int EXIT_OPTION = 0;
    private static final int SEND_MESSAGE_OPTION = 1;
    private static final int NON_EXISTING_OPTION = -1;

    public static void main(String[] args) {

        if (args.length != 2) {
            throw new IllegalStateException("Only two parameters allowed: 1. username, 2. saving directory");
        }

        final String username = args[0];
        final String savingDirectory = args[1];

        Scanner scanner = new Scanner(System.in);

        BrokerConnector connector = new BrokerConnector();

        Sender sender = new Sender(connector);
        Consumer consumer = new Consumer(connector, username, savingDirectory);
        consumer.start();

        while (true) {

            try {

                displayMenu();
                final String input = scanner.nextLine();
                int selectedOption;

                try {
                    selectedOption = Integer.parseInt(input);
                } catch (final NumberFormatException e) {
                    selectedOption = NON_EXISTING_OPTION;
                }

                switch (selectedOption) {
                    case EXIT_OPTION:
                        print("Exting the application.");
                        System.exit(0);
                    case SEND_MESSAGE_OPTION:

                        print("Username: ");
                        final String receiver = scanner.nextLine();

                        print("File location: ");
                        final String fileLocation = scanner.nextLine();

                        print("Message sent. File: " + fileLocation);
                        sender.sendMessage(fileLocation, receiver);
                        break;
                    default:
                        print("Option informed couldn't be found, please stick to the numbers [0] and [1]");
                }

            } catch (final Exception e) {
                //don't mind me.
            }
        }
    }

    private static void displayMenu () {
        print("===========================");
        print("[ 1 ] - Send file ");
        print("[ 0 ] - Exit ");
        print("===========================");
    }





}
