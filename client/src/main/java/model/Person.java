package model;

import java.io.Serializable;

/**
 * Person description
 */
public class Person implements Serializable {

    static final long serialVersionUID = 8L;

    private String name;        //Поле не может быть null, Строка не может быть пустой
    private Integer weight;     //Значение поля должно быть больше 0
    private Color eyeColor;     //Поле не может быть null
    private Color hairColor;    //Поле не может быть null
    private Country nationality;//Поле может быть null

    public Person(String name,
                  Integer weight,
                  Color eyeColor,
                  Color hairColor,
                  Country nationality) {
        checkState(name, weight, eyeColor, hairColor);
        this.name = name;
        this.weight = weight;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return "Screenwriter info " + "\n" +
                "name: " + name +
                ", weight: " + weight +
                ", eyeColor: " + eyeColor +
                ", hairColor: " + hairColor +
                ", nationality: " + nationality + "\n" + "\n";
    }

    /**
     * @return screenwriter name
     */
    public String getName() {
        return name;
    }

    /**
     * Validate params before state updating
     *
     * @param name
     * @param weight
     * @param eyeColor
     * @param hairColor
     * @throws IllegalArgumentException illegal args value
     */
    private void checkState(String name,
                            Integer weight,
                            Color eyeColor,
                            Color hairColor) {
        if (name != null
                && !(name.isEmpty())
                && weight > 0
                && eyeColor != null
                && hairColor != null) {
            return;
        }
        throw new IllegalArgumentException("Illegal argument value for person - " + "name: " + name + ", weight: " + weight);
    }

    /**
     * Update state
     */
    public void update(String name, Integer weight, Color eyeColor, Color hairColor, Country nationality) {
        this.name = name;
        this.weight = weight;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
    }

    public Integer getWeight() {
        return weight;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public Country getNationality() {
        return nationality;
    }
}
