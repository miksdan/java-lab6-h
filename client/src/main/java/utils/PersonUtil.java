package utils;

import model.Color;
import model.Country;
import model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Utility that helps to read the parameters of the person
 */
public class PersonUtil {

    private static final Logger logger = LoggerFactory.getLogger(PersonUtil.class);

    private static final String PERSON_COUNTRIES = Arrays.stream(Country.values()).map(Enum::name)
            .collect(Collectors.joining(", "));

    private static final String PERSON_COLOR = Arrays.stream(Color.values()).map(Enum::name)
            .collect(Collectors.joining(", "));

    public static class PersonBuilder {
        String name;
        Integer weight;
        Color eyeColor;
        Color hairColor;
        Country nationality;
    }

    /**
     * creates Person
     *
     * @param scan provides params of a Person
     * @return crated Person
     */
    public static Person createPerson(Scanner scan) {
        PersonBuilder pb = readPerson(scan);
        if (pb == null) {
            return null;
        }
        return new Person(pb.name,
                pb.weight,
                pb.eyeColor,
                pb.hairColor,
                pb.nationality);
    }

    /**
     * updating person
     *
     * @param scan
     * @param person
     * @return person
     */
    public static Person updatePerson(Scanner scan, Person person) {
        PersonBuilder pb = readPerson(scan);
        if (pb == null) {
            UniqueValuesUtil.removePerson(person);
            return null;
        }
        UniqueValuesUtil.updatePerson(person, pb.name);
        person.update(pb.name,
                pb.weight,
                pb.eyeColor,
                pb.hairColor,
                pb.nationality);
        return person;
    }

    /**
     * simplifies person creating
     *
     * @param scan
     */
    public static PersonBuilder readPerson(Scanner scan) {
        if(!aimValidation(scan)) {
            return null;
        }
        final PersonBuilder pb = new PersonBuilder();
        Runnable[] paramFillingRunnable = {() -> pb.name = getName(scan),
                () -> pb.weight = getWeight(scan),
                () -> pb.eyeColor = getEyeColor(scan),
                () -> pb.hairColor = getHairColor(scan),
                () -> pb.nationality = getNationality(scan)};

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
        return pb;
    }

    /**
     * makes sure the person is needed (it nullable)
     *
     * @param scan provides answer
     * @return true if person is needed
     */
    private static boolean aimValidation(Scanner scan) {
        logger.info("Add a person \n['Y' to accept / Any symbol for cancellation] ");
        String answer = scan.nextLine().trim();

        return answer.equals("Y");
    }

    /**
     * obtaining name
     *
     * @param scan
     * @return person's name
     */
    private static String getName(Scanner scan) {
        logger.info("Enter a person name: ");
        logger.warn("The field cannot be empty.");
        String name = scan.nextLine().trim();
        validatePersonParams(name != null && !name.isEmpty());

        return name;
    }

    /**
     * obtaining weight
     *
     * @param scan
     * @return person's weight
     */
    private static Integer getWeight(Scanner scan) {
        logger.info("Enter a person weight: ");
        logger.warn("The field must be more than 0");
        Integer weight = Integer.valueOf(scan.nextLine().trim());
        validatePersonParams(weight == null || weight > 0);

        return weight;
    }

    /**
     * obtaining person's eye color
     *
     * @param scan
     * @return person's eye color
     */
    private static Color getEyeColor(Scanner scan) {
        logger.info("Enter a person eye color: " + PERSON_COLOR);
        logger.warn("The field cannot be empty.");
        String eyeColorStr = scan.nextLine().trim();
        validatePersonParams(!eyeColorStr.equals(null));

        return Color.valueOf(eyeColorStr);
    }

    /**
     * obtaining person's hair color
     *
     * @param scan
     * @return person's hair color
     */
    private static Color getHairColor(Scanner scan) {
        logger.info("Enter a person hair color: " + PERSON_COLOR);
        logger.warn("The field cannot be empty.");
        String hairColorStr = scan.nextLine().trim();
        validatePersonParams(!hairColorStr.equals(null));

        return Color.valueOf(hairColorStr);
    }

    /**
     * obtaining person's nationality
     *
     * @param scan
     * @return person's nationality
     */
    private static Country getNationality(Scanner scan) {
        logger.info("Enter a person nationality: " + PERSON_COUNTRIES);
        logger.warn("The field can be empty. To enter NULL, use an empty line and Enter");
        String nationality = scan.nextLine().trim();

        return nationality.isEmpty() ? null : Country.valueOf(nationality);
    }

    /**
     * throw exception for invalid params
     *
     * @param isValid displays param validity
     * @throws IllegalArgumentException if invalid
     */
    private static void validatePersonParams(boolean isValid) {
        if (!isValid) {
            throw new IllegalArgumentException("Illegal argument value for person");
        }
    }
}