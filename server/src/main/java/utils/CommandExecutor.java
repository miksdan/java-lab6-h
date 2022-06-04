package utils;

import messages.Request;
import messages.Response;
import model.Movie;
import model.MpaaRating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Execute commands from console or from file
 */
public class CommandExecutor {

    private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
    /**
     *defines the mapping of command names to its calls
     */
    private static final Map<String, Command> COMMAND_FUNCTION_MAP;
    private static final String HELP_INFO =
            "\n" +
                    "help : вывести справку по доступным командам\n" +
                    "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                    "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                    "add {element} : добавить новый элемент в коллекцию\n" +
                    "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                    "remove_by_id id : удалить элемент из коллекции по его id\n" +
                    "clear : очистить коллекцию\n" +
                    "save : сохранить коллекцию в файл (server only)\n" +
                    "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                    "exit : завершить программу (без сохранения в файл)\n" +
                    "head : вывести первый элемент коллекции\n" +
                    "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                    "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                    "max_by_creation_date : вывести любой объект из коллекции, значение поля creationDate которого является максимальным\n" +
                    "count_by_mpaa_rating mpaaRating : вывести количество элементов, значение поля mpaaRating которых равно заданному\n" +
                    "filter_by_mpaa_rating mpaaRating : вывести элементы, значение поля mpaaRating которых равно заданному\n";
    static {
        Map<String, Command> cfmTemp = new HashMap<>();
        cfmTemp.put("help", CommandExecutor::help);
        cfmTemp.put("info", CommandExecutor::info);
        cfmTemp.put("show", CommandExecutor::show);
        cfmTemp.put("add", CommandExecutor::add);
        cfmTemp.put("update", CommandExecutor::update);
        cfmTemp.put("remove_by_id", CommandExecutor::removeById);
        cfmTemp.put("clear", CommandExecutor::clear);
        cfmTemp.put("save", CommandExecutor::save);
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
     * @param request client request
     */
    public static Response startExecution(Request request) {
        return executeCommand(request);
    }

    /**
     * execute one command from scanner
     * @param request client request
     * @throws IllegalArgumentException for invalid command name
     */
    private static Response executeCommand(Request request) {
            try {
                return Optional.ofNullable(COMMAND_FUNCTION_MAP.get(request.getCommand()))
                        .orElseThrow(() -> new IllegalArgumentException("Invalid command, type \"help\" for information about commands"))
                        .execute(request);
            } catch (Exception e) {
//            e.printStackTrace();
                logger.error("The error of the command" + ": " + e.getMessage());
                return new Response("The error of the command" + ": " + e.getMessage());
//                throw new RuntimeException("The error of the command" + ": " + e.getMessage());
            }
    }

    /**
     * help command
     * @param request client request
     */
   private static Response help(Request request) {
       return new Response(HELP_INFO);
    }

    /**
     * info command
     * @param request client request
     */
    private static Response info(Request request) {
        return new Response("PriorityQueue\nDate: " + MovieStorage.getInitDate() + "\nCount of elements: " + MovieStorage.size());
    }

    /**
     * show command
     * @param request client request
     */
    private static Response show(Request request) {
        StringBuilder sb = new StringBuilder();
        List<Movie> movies = MovieStorage.getSortedListByName();
        for (Movie movie : movies) {
            String writer = movie.getScreenwriter() != null ? movie.getScreenwriter().toString() : "";
            sb.append(movie).append("\n").append(writer).append("\n");
        }
        return new Response(sb.toString());
    }

    /**
     * add command
     * @param request client request
     */
    private static Response add(Request request) {
        MovieStorage.add(request.getMovie());
        return new Response("The film is added to the collection!");
    }

    /**
     * update command
     * @param request client request
     */
    private static Response update(Request request) {
        int id = Integer.parseInt(request.getArgument());
        MovieStorage.update(id, request.getMovie());
        return new Response("The fields of the film updated!");
    }

    /**
     * remove_by_id command
     * @param request client request
     */
    private static Response removeById(Request request) {
        MovieStorage.removeById(Integer.parseInt(request.getArgument()));
        return new Response("Removed by ID!");
    }

    /**
     * clear command
     * @param request client request
     */
    private static Response clear(Request request) {
        MovieStorage.clear();
        return new Response("The collection is cleaned of elements!");
    }

    /**
     * save command
     * @param request client request
     */
    private static Response save(Request request) {
        FileAccessor.writeXmlFile();
        return new Response("The collection is saved!");
    }

    /**
     * head command
     * @param request client request
     */
    private static Response head(Request request) {
        return new Response(MovieStorage.getIterator().next().toString());
    }

    /**
     * remove_greater command
     * @param request client request
     */
    private static Response removeGreater(Request request) {
        MovieStorage.removeGreater(request.getMovie());
        return new Response("remove greater command executed");
    }

    /**
     * remove_lower command
     * @param request client request
     */
    private static Response removeLower(Request request) {
        MovieStorage.removeLower(request.getMovie());
        return new Response("remove lower command executed");
    }

    /**
     * max_by_creationDate command
     * @param request client request
     */
    private static Response maxByCreationDate(Request request) {
        return new Response(MovieStorage.getMaxCreationDate().toString());
    }

    /**
     * count_by_mpaa_rating command
     * @param request client request
     */
    private static Response countByMpaaRating(Request request) {
        return new Response(
                "With the rating '" + request.getArgument() + "' exists " +
                        MovieStorage.countByMpaaRating(MpaaRating.valueOf(request.getArgument())) + " element(s).");
    }

    /**
     * filter_by_mpaa_rating command
     * @param request client request
     */
    private static Response filterByMpaaRating(Request request) {
        StringBuilder sb = new StringBuilder();
        List<Movie> list = MovieStorage.filterByMpaaRating(MpaaRating.valueOf(request.getArgument()));
        for (Movie movie : list) {
            sb.append(movie).append("\n");
        }
        return new Response(sb.toString());
    }
}
