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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

import static utils.ConvertUtil.convertByteArrayToObject;
import static utils.ConvertUtil.convertObjectToByteArray;
import static utils.FileValidator.validateFileAndReadXml;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final ServerSocketChannel server;
    private final List<ClientConnection> clients = new ArrayList<>();
    private Selector selector;
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
        selector = Selector.open();
        server.configureBlocking(false);
        server.socket().bind(new InetSocketAddress(Integer.valueOf(inetSocketAddress)));
        server.register(selector, SelectionKey.OP_ACCEPT);
        logger.info("Server started.");
        if (validateFileAndReadXml(fileName)) {
            run();
        }
        throw new RuntimeException("Something wrong with a file: " + fileName);
    }

    /**
     * server command execution thread
     */
    private void run() throws IOException {

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

        ByteBuffer buffer = ByteBuffer.allocate(10000);
        while (true) {
            int select = selector.select();
            if (select == 0) continue;

            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();

            while(selectedKeys.hasNext()) {
                SelectionKey selectionKey = selectedKeys.next();

                if (selectionKey.channel() == server) {
                    SocketChannel client = server.accept();
                    if (client != null) {
                        client.configureBlocking(false);
                        logger.info("Client connected.");
                    }
                    client.register(selector, SelectionKey.OP_READ);
                } else {
                    try {
                        buffer.clear();
                        int countOfBytes = ((SocketChannel) selectionKey.channel()).read(buffer);
                        if (countOfBytes < 1) {
                            return;
                        }
                        Request request = (Request) convertByteArrayToObject(buffer.array());
                        logger.info("Received Request from client with command: " + request.getCommand() + ".");
                        Response response = CommandExecutor.startExecution(request);
                        logger.info("Sending Response to client.");
                        ((SocketChannel) selectionKey.channel()).write(ByteBuffer.wrap(convertObjectToByteArray(response)));
                        logger.info("Response send to client.");
                    } catch (IOException e) {
                        logger.warn("Client disconnected.");
                        selectionKey.cancel();
                    }
                }
            }
            selectedKeys.remove();
        }
    }
}
