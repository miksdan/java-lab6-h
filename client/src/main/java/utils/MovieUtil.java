package utils;

import model.Coordinates;
import model.Movie;
import model.MpaaRating;
import model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Utility that helps to read the parameters of the movie
 */
public class MovieUtil {
    private static final Logger logger = LoggerFactory.getLogger(MovieUtil.class);

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

    /**
     * creates Movie
     *
     * @param scan provides params of a Movie
     * @return crated Movie
     */
    public static Movie createMovie(Scanner scan) {
        MovieBuilder mb = readMovie(scan, null);
        return new Movie(0,
                mb.name,
                mb.coordinates,
                LocalDate.now(),
                mb.oscarsCount,
                mb.goldenPalmCount,
                mb.length,
                mb.mpaaRating,
                mb.screenwriter);
    }

    /**
     * simplifies movie creation
     * (movie parameters have restrictions)
     */
    /**
     * simplifies movie creation
     * (movie parameters have restrictions)
     */
    private static MovieBuilder readMovie(Scanner scan, Movie movie) {
        final MovieBuilder mb = new MovieBuilder();
        Runnable[] paramFillingRunnable = {() -> mb.name = getName(scan),
                () -> mb.coordinates = getCoordinates(scan),
                () -> mb.oscarsCount = getOscarsCount(scan),
                () -> mb.goldenPalmCount = getGoldenPalmCount(scan),
                () -> mb.length = getLength(scan),
                () -> mb.mpaaRating = getMpaaRating(scan),
                () -> mb.screenwriter = getScreenwriter(scan, movie)};

        for (Runnable runnable : paramFillingRunnable) {
            int counter = 0;
            int maxError = 5;
            while (true) {
                try {
                    runnable.run();
                    break;
                } catch (Exception e) {
                    logger.error("Not valid parameter." + " Attempts left: " + (maxError - counter));
                    counter++;
                    if (!((maxError - counter) >= 0)) {
                        logger.error("ooops... something went wrong.");
                        logger.info("Client terminated.");
                        System.exit(0);
                    }
                }
            }
        }
        return mb;
    }

    /**
     * obtaining name
     *
     * @param scan
     * @return name
     */
    private static String getName(Scanner scan) {
        logger.info("Enter a movie name");
        logger.warn("The field cannot be empty.");
        String name = scan.nextLine().trim();
        validateMovieParams(name != null && !name.isEmpty());

        return name;
    }

    /**
     * obtaining coordinates
     *
     * @param scan
     * @return coordinates
     */
    private static Coordinates getCoordinates(Scanner scan) {
        logger.info("Enter a movie x coordinates:");
        logger.warn("The field must be more than -162");
        Integer x = Integer.valueOf(scan.nextLine().trim());
        logger.info("Enter a movie y coordinates:");
        logger.warn("The field must be no more than 232");
        Long y = Long.valueOf(scan.nextLine().trim());

        return new Coordinates(x, y);
    }

    /**
     * obtaining oscars count
     *
     * @param scan
     * @return oscars count
     */
    private static Integer getOscarsCount(Scanner scan) {
        logger.info("Enter a movie oscars count ");
        logger.warn("The field must be more than 0");
        Integer oscarsCount = Integer.valueOf(scan.nextLine().trim());
        validateMovieParams(oscarsCount > 0);

        return oscarsCount;
    }

    /**
     * obtaining golden palm count
     *
     * @param scan
     * @return golden palm count
     */
    private static int getGoldenPalmCount(Scanner scan) {
        logger.info("Enter a movie golden palm count ");
        logger.warn("The field must be more than 0");
        Integer goldenPalmCount = Integer.valueOf(scan.nextLine().trim());
        validateMovieParams(goldenPalmCount > 0);

        return goldenPalmCount;
    }

    /**
     * obtaining length
     *
     * @param scan
     * @return movie length
     */
    private static long getLength(Scanner scan) {
        logger.info("Enter a movie length ");
        logger.warn("The field must be more than 0");
        Long length = Long.valueOf(scan.nextLine().trim());
        validateMovieParams(length > 0);

        return length;
    }

    /**
     * obtaining Mpaa Rating
     *
     * @param scan
     * @return movie mpaa rating
     */
    private static MpaaRating getMpaaRating(Scanner scan) {
        logger.info("Enter a movie MpaaRating(" + UNITS + ", null):");
        logger.warn("The field can be empty. To enter NULL, use an empty line and Enter");
        String mpaaRating = scan.nextLine().trim();
        validateMovieParams(true);

        return mpaaRating.isEmpty() ? null : MpaaRating.valueOf(mpaaRating);
    }

    /**
     * obtaining Screenwriter
     *
     * @param scan
     * @param movie
     * @return screenwriter
     */
    private static Person getScreenwriter(Scanner scan, Movie movie) {
        logger.info("Movie screenwriter");
        logger.warn("The field can be empty. To enter NULL, use an empty line and Enter");

        return movie != null ? PersonUtil.updatePerson(scan, movie.getScreenwriter())
                : PersonUtil.createPerson(scan);
    }

    /**
     * throw exception for invalid params
     *
     * @param isValid displays param validity
     * @throws IllegalArgumentException if invalid
     */
    private static void validateMovieParams(boolean isValid) {
        if (!isValid) {
            throw new IllegalArgumentException("Illegal argument value for movie");
        }
    }
}