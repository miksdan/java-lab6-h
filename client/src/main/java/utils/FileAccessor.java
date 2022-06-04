package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * implements reading and writing files
 */
public class FileAccessor {

    public static final Logger logger = LoggerFactory.getLogger(FileAccessor.class);

    /**
     * starts execution of a script in a file
     *
     * @param scriptFile path to script file
     */
    public static void readScript(String scriptFile) {

        try (Scanner scan = new Scanner(Files.newInputStream(Paths.get(scriptFile)))) {
            UniqueValuesUtil.addScript(scriptFile);
            CommandExecutor.executeScriptCommands(scan);
            UniqueValuesUtil.removeScript(scriptFile);
        } catch (IOException e) {
            logger.error("Cannot read script file");
        }
    }
}
