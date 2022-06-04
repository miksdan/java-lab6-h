import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        if (args.length < 1) {
            logger.error("The command line argument was expected.\n");
            logger.info("The program terminated.");
            System.exit(0);
        }

        try {
            new Server(args[0], args[1]);
        } catch (IOException | URISyntaxException e) {
            logger.info(e.getMessage());
        }
    }
}
