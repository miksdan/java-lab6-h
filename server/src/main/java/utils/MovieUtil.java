package utils;

import model.Coordinates;
import model.MpaaRating;
import model.Person;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Utility that helps to read the parameters of the movie
 */
public class MovieUtil {
    public static class MovieBuilder {
        public Integer id;
        public String name;
        public Coordinates coordinates;
        public LocalDate creationDate;
        public Integer oscarsCount;
        public int goldenPalmCount;
        public long length;
        public MpaaRating mpaaRating;
        public Person screenwriter;
    }

    /**
     * Contains Mpaa Rating values name
     */
    private static final String UNITS = Arrays.stream(MpaaRating.values()).map(Enum::name)
            .collect(Collectors.joining(", "));
}