package utils;

import model.Color;
import model.Country;

/**
 * Utility that helps to read the parameters of the person
 */
public class PersonUtil {

    public static class PersonBuilder {
        String name;
        Integer weight;
        Color eyeColor;
        Color hairColor;
        Country nationality;
    }
}