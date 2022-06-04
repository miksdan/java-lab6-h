package model;

import java.io.Serializable;

/**
 * XY coordinates
 */
public class Coordinates implements Serializable {

    static final long serialVersionUID = 5L;

    private Integer x;  //Значение поля должно быть больше -162, Поле не может быть null
    private Long y;     //Максимальное значение поля: 232, Поле не может быть null

    public Coordinates(Integer x, Long y) {
        checkState(x, y);
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x=" + x + ", " +
                "y=" + y;
    }

    /**
     * Validate params before state updating
     * @param y
     * @throws IllegalArgumentException illegal args value
     */
    private void checkState(Integer x, Long y) {
        if (x > -162 && x != null && y <= 232 && y != null) {
            return;
        }
        throw new IllegalArgumentException("Illegal argument value for coordinates");
    }

    public Integer getX() {
        return x;
    }

    public Long getY() {
        return y;
    }
}
