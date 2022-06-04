import client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        if (args.length < 1) {
            logger.error("The command line argument was expected.\n");
            logger.info("The program terminated.");
            System.exit(0);
        }

        try {
            final Integer inetSocketAddress = Integer.valueOf(args[0]);
            new Client(inetSocketAddress);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
}
