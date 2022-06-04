package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class ClientConnection {

    private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);

    private SocketChannel socket;

    public ClientConnection(SocketChannel socketChannel) throws IOException {
        this.socket = socketChannel;
    }

    public SocketChannel getSocket() {
        return socket;
    }

    public void setSocket(SocketChannel socket) {
        this.socket = socket;
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
