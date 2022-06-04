package utils;

import messages.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiConsumer;

import static utils.MovieUtil.createMovie;

/**
 * Execute commands from console or from file
 */
public class CommandExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

    /**
     * defines the mapping of command names to its calls
     */
    private static final Map<String, BiConsumer<String, Scanner>> COMMAND_FUNCTION_MAP;

    static {
        Map<String, BiConsumer<String, Scanner>> cfmTemp = new HashMap<>();
        cfmTemp.put("help", CommandExecutor::help);
        cfmTemp.put("info", CommandExecutor::info);
        cfmTemp.put("show", CommandExecutor::show);
        cfmTemp.put("add", CommandExecutor::add);
        cfmTemp.put("update", CommandExecutor::update);
        cfmTemp.put("remove_by_id", CommandExecutor::removeById);
        cfmTemp.put("clear", CommandExecutor::clear);
        cfmTemp.put("execute_script", CommandExecutor::executeScript);
        cfmTemp.put("exit", CommandExecutor::exit);
        cfmTemp.put("head", CommandExecutor::head);
        cfmTemp.put("remove_greater", CommandExecutor::removeGreater);
        cfmTemp.put("remove_lower", CommandExecutor::removeLower);
        cfmTemp.put("max_by_creation_date", CommandExecutor::maxByCreationDate);
        cfmTemp.put("count_by_mpaa_rating", CommandExecutor::countByMpaaRating);
        cfmTemp.put("filter_by_mpaa_rating", CommandExecutor::filterByMpaaRating);
        COMMAND_FUNCTION_MAP = Collections.unmodifiableMap(cfmTemp);
    }

    /**
     * Starts an endless loop of receiving commands from the console
     *
     * @param scan console scanner
     */
    public static void startExecution(Scanner scan) {
        do {
            if (scan.hasNext()) {
                executeCommand(scan);
            } else {
                logger.info("Client is stopped.");
                System.exit(0);
            }
        } while (scan.hasNext());
    }

    /**
     * execute script from file
     *
     * @param scan script file scanner
     */
    public static void executeScriptCommands(Scanner scan) {
        while (scan.hasNext()) {
            executeCommand(scan);
        }
    }

    /**
     * execute script from file
     *
     * @param scan script file scanner
     */
    private static void executeScript(String params, Scanner scan) {
        if (UniqueValuesUtil.isScriptAlreadyRunning(params)) {
            System.out.println();
            throw new IllegalArgumentException("Infinite loop detected, command 'execute_script " + params + "' skipped" + "\n");
        }
        FileAccessor.readScript(params);
    }

    /**
     * execute one command from scanner
     *
     * @param scan script file scanner
     * @throws IllegalArgumentException for invalid command name
     */
    private static void executeCommand(Scanner scan) {

        String[] currentCommand = (scan.nextLine().trim() + " ").split(" ", 2);
        try {
            Optional.ofNullable(COMMAND_FUNCTION_MAP.get(currentCommand[0]))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid command, type \"help\" for information about commands"))
                    .accept(currentCommand[1].trim(), scan);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * checks if additional parameters were on the same line with the command
     *
     * @param params should be empty
     * @throws IllegalArgumentException for invalid command name
     */
    private static void isAdditionalParamsEmpty(String params) {
        if (!params.isEmpty()) {
            throw new IllegalArgumentException("This command doesn't need parameters");
        }
    }

    /**
     * help command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void help(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        ConnectionKeeper.sendMessageAndGetAnswer(new Request("help", null, null));
    }

    /**
     * info command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void info(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("info", null, null));
    }

    /**
     * show command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void show(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("show", null, null));
    }

    /**
     * add command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void add(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("add", null, createMovie(scan)));
//        System.out.println("The film is added to the collection!");
    }

    /**
     * update command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void update(String params, Scanner scan) {
        int id = Integer.parseInt(params);
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("update", String.valueOf(id), createMovie(scan)));
//        System.out.println("The fields of the film updated!");
    }

    /**
     * remove_by_id command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void removeById(String params, Scanner scan) {
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("remove_by_id", String.valueOf(params), null));
//        System.out.println("Removed by ID!");
    }

    /**
     * clear command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void clear(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("clear", null, null));
//        System.out.println("The collection is cleaned of elements!");
    }

    /**
     * exit command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void exit(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        System.out.println("The program terminated.");
        System.exit(0);
    }

    /**
     * head command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void head(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("head", null, null));
    }

    /**
     * remove_greater command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void removeGreater(String params, Scanner scan) {
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("remove_greater", null, createMovie(scan)));
//        System.out.println("Greater elements are removed!");
    }

    /**
     * remove_lower command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void removeLower(String params, Scanner scan) {
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("remove_greater", null, createMovie(scan)));
//        System.out.println("Lower elements are removed!");
    }

    /**
     * max_by_creationDate command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void maxByCreationDate(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("max_by_creation_date", null, null));
    }

    /**
     * count_by_mpaa_rating command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void countByMpaaRating(String params, Scanner scan) {
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("count_by_mpaa_rating", params, null));
    }

    /**
     * filter_by_mpaa_rating command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void filterByMpaaRating(String params, Scanner scan) {
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("filter_by_mpaa_rating", params, null));
    }
}
