package utils;

import model.Person;

import java.util.HashSet;
import java.util.TreeSet;

/**
 * Ensures uniqueness
 * and prevents infinite recursion in scripts
 */
public class UniqueValuesUtil {

    private static final TreeSet<Integer> MOVIE_ID_SET = new TreeSet();
    private static final TreeSet<String> PERSON_NAME_SET = new TreeSet<>();
    private static final HashSet<String> SCRIPT_FILE_NAME_SET = new HashSet();

    public static void addScript(String fileName) {
        SCRIPT_FILE_NAME_SET.add(fileName);
    }

    public static void removeScript(String fileName) {
        SCRIPT_FILE_NAME_SET.remove(fileName);
    }

    public static boolean isScriptAlreadyRunning(String filename) {
        return SCRIPT_FILE_NAME_SET.contains(filename);
    }

    public static void removePerson(Person person) {
        PERSON_NAME_SET.remove(person.getName());
    }

    public static void updatePerson(Person person, String name) {
        PERSON_NAME_SET.add(name);
    }

    public static boolean isPersonNameAvailable(String personName) {
        return !PERSON_NAME_SET.contains(personName);
    }

    public static boolean isMovieIdUnique(Integer id) {
        return !MOVIE_ID_SET.contains(id);
    }
}