package utils;

import messages.Request;
import messages.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static utils.ConvertUtil.convertByteArrayToObject;
import static utils.ConvertUtil.convertObjectToByteArray;

public class ConnectionKeeper {

    private static SocketChannel client;

    private static final Logger logger = LoggerFactory.getLogger(ConnectionKeeper.class);

    private static Integer inetSocketAddress;

    public static void connectToServer(Integer inetSocketAddress) {
        setInetSocketAddress(inetSocketAddress);
        int attempts = 10;
        while (attempts > 0) {
            try {
                logger.info("Trying to connect to server.");
                client = SocketChannel.open(new InetSocketAddress("localhost", inetSocketAddress));
                client.configureBlocking(false);
                return;
            } catch (IOException e) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException("Exception in client connection to server.");
                }
            }
            attempts--;
        }
        throw new RuntimeException("Server is not available.");
    }

    public static void setInetSocketAddress(Integer inetSocketAddress) {
        ConnectionKeeper.inetSocketAddress = inetSocketAddress;
    }

    public static void sendMessageAndGetAnswer(Request request) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(10000);
            buffer.flip();
            client.write(ByteBuffer.wrap(convertObjectToByteArray(request)));
            logger.info("Send request to server.");
            buffer.clear();
            waitServerAnswer(buffer);
            Response response = (Response) convertByteArrayToObject(buffer.array());
            logger.info(response.getAnswer());
        } catch (IOException e) {
            logger.error(e.getMessage());
            connectToServer(inetSocketAddress);
            logger.info("The connection is restored.");
        }
    }

    private static void waitServerAnswer(ByteBuffer buffer) throws IOException {
        while (client.read(buffer) == 0) {
            try {
                logger.info("Waiting answer.");
                Thread.sleep(600);
            } catch (InterruptedException e) {
                logger.info(e.getMessage());
            }
        }
    }
}
