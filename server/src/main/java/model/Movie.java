package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Movie implements Comparable<Movie>, Serializable {   //implements Comparable<Movie>

    static final long serialVersionUID = 3L;

    private int id;                                 //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name;                            //Поле не может быть null
    private Coordinates coordinates;                //Поле не может быть null
    private LocalDate creationDate;                 //Поле не может быть null
    private Integer oscarsCount;                    //Значение поля должно быть больше 0
    private int goldenPalmCount;                    //Значение поля должно быть больше 0
    private long length;                            //Значение поля должно быть больше 0
    private MpaaRating mpaaRating;                  //Поле может быть null
    private Person screenwriter;                    //Не указано -> Поле может быть null

    public Movie(int id,
                 String name,
                 Coordinates coordinates,
                 LocalDate creationDate,
                 Integer oscarsCount,
                 int goldenPalmCount,
                 long length,
                 MpaaRating mpaaRating,
                 Person screenwriter) {
        checkState(name,
                coordinates,
                creationDate,
                oscarsCount,
                goldenPalmCount,
                length);
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.oscarsCount = oscarsCount;
        this.goldenPalmCount = goldenPalmCount;
        this.length = length;
        this.mpaaRating = mpaaRating;
        this.screenwriter = screenwriter;
    }

    /**
     * @return movie id
     */
    public int getId() {
        return id;
    }

    /**
     * @return screenwriter
     */
    public Person getScreenwriter() {
        return screenwriter;
    }

    @Override
    public String toString() {
        return "Movie: " + "\n" +
                "id: " + id + "\n" +
                "name: '" + name + '\'' + "\n" +
                "coordinates: " + coordinates + "\n" +
                "creation date: " + creationDate + "\n" +
                "oscars count: " + oscarsCount + "\n" +
                "golden palm count: " + goldenPalmCount + "\n" +
                "length: " + length + "\n" +
                "mpaa rating: " + mpaaRating + "\n";
    }

    /**
     * Validate params before state updating
     *
     * @throws IllegalArgumentException illegal args value
     */
    private void checkState(String name,
                            Coordinates coordinates,
                            LocalDate creationDate,
                            Integer oscarsCount,
                            int goldenPalmCount,
                            long length) {
        if (name != null
                && !name.isEmpty()
                && coordinates != null
                && creationDate != null
                && oscarsCount > 0
                && goldenPalmCount > 0
                && length > 0) {
            return;
        }
        throw new IllegalArgumentException("Illegal argument value for movie - " + "name: " + name + ", coordinates: " + coordinates + ", creation date: " + creationDate);
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Update state
     *
     * @throws IllegalArgumentException illegal args value
     */
    public void update(String name, Coordinates coordinates, Integer oscarsCount, int goldenPalmCount, long length, MpaaRating mpaaRating, Person screenwriter) {
        checkState(name,
                coordinates,
                creationDate,
                oscarsCount,
                goldenPalmCount,
                length);
        this.name = name;
        this.coordinates = coordinates;
        this.oscarsCount = oscarsCount;
        this.goldenPalmCount = goldenPalmCount;
        this.length = length;
        this.mpaaRating = mpaaRating;
        this.screenwriter = screenwriter;
    }

    /**
     * @return movie name
     */
    public String getName() {
        return name;
    }

    /**
     * @return movie coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return movie creation date
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * @return movie oscar count
     */
    public Integer getOscarsCount() {
        return oscarsCount;
    }

    /**
     * @return movie golden palm count
     */
    public int getGoldenPalmCount() {
        return goldenPalmCount;
    }

    /**
     * @return movie length
     */
    public long getLength() {
        return length;
    }

    /**
     * @return movie mpaa rating
     */
    public MpaaRating getMpaaRating() {
        return mpaaRating;
    }

    @Override
    public int compareTo(Movie o) {
        if (this.name.length() > o.getName().length()) {
            return 1;
        } else if (this.name.equals(o.getName())) {
            return 0;
        }
        return -1;
    }
}