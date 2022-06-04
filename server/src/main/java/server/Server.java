package server;

import messages.Request;
import messages.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CommandExecutor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static utils.ConvertUtil.convertByteArrayToObject;
import static utils.ConvertUtil.convertObjectToByteArray;
import static utils.FileValidator.validateFileAndReadXml;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final ServerSocketChannel server;
    private final List<ClientConnection> clients = new ArrayList<>();

    private final String fileName;

    /**
     * Constructor for creating a server object
     *
     * @param fileName
     * @param inetSocketAddress
     * @throws IOException
     * @throws URISyntaxException
     */
    public Server(String fileName, String inetSocketAddress) throws IOException, URISyntaxException {
        this.fileName = fileName;
        this.server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.socket().bind(new InetSocketAddress(Integer.valueOf(inetSocketAddress)));
        logger.info("Server started.");
        if (validateFileAndReadXml(fileName)) {
            run();
        }
        throw new RuntimeException("Something wrong with a file: " + fileName);
    }

    /**
     * server command execution thread
     */
    private void run() {

        Thread thread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String command = "";
            while (true) {
                if (scanner.hasNext()) {
                    command = scanner.nextLine();
                    if (command.equals("save")) {
                        CommandExecutor.startExecution(new Request("save", null, null));
                        logger.info("Collection is saved.");
                    }
                    if (command.equals("exit")) {
                        CommandExecutor.startExecution(new Request("save", null, null));
                        logger.info("Collection is saved.");
                        logger.info("Server is stopped.");
                        System.exit(0);
                    }
                }
                else {
                    logger.warn("Server is stopped.");
                    CommandExecutor.startExecution(new Request("save", null, null));
                    System.exit(0);
                }
            }
        });
        thread.start();

        while (true) {
            getNewClient();
            handleNewRequests();
        }
    }

    private void handleNewRequests() {
        ByteBuffer buffer = ByteBuffer.allocate(10000);
        for (int i = 0; i < clients.size(); i++) {
            ClientConnection client = clients.get(i);
            try {
                buffer.clear();
                int countOfBytes = client.getSocket().read(buffer);
                if (countOfBytes < 1) {
                    continue;
                }
                Request request = (Request) convertByteArrayToObject(buffer.array());
                logger.info("Received Request from client with command " + request.getCommand());
                Response response = CommandExecutor.startExecution(request);
                logger.info("Sending Response to client.");
                client.getSocket().write(ByteBuffer.wrap(convertObjectToByteArray(response)));
                logger.info("Response send to client.");
            } catch (IOException e) {
                logger.warn("Client disconnected.");
                client.disconnect();
                clients.remove(client);
            }
        }

    }

    private void getNewClient() {
        try {
            SocketChannel client = server.accept();
            if (client != null) {
                client.configureBlocking(false);
                clients.add(new ClientConnection(client));
                logger.info("Client connected.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
