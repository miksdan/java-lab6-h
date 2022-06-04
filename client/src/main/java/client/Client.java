package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CommandExecutor;
import utils.ConnectionKeeper;

import java.util.Scanner;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private final Scanner consoleScanner;

    /**
     * Constructor for creating a client object
     *
     * @param inetSocketAddress
     */
    public Client(Integer inetSocketAddress) {
        this.consoleScanner = new Scanner(System.in);
        logger.info("Client is started.");
        connectToServer(inetSocketAddress);
        startClient();
    }

    private void connectToServer(Integer inetSocketAddress) {
        try {
            ConnectionKeeper.connectToServer(inetSocketAddress);
            logger.info("Client connected to server.");
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            logger.info("Client is turned off.");
            System.exit(0);
        }
    }

    private void startClient() {
        try (Scanner scan = new Scanner(System.in)) {
            CommandExecutor.startExecution(scan);
        }
    }
}
