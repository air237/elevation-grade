package elevation.grade;

import lombok.Data;

@Data
public class Coord {
    private float lat;
    private float lon;
    private float ele;
    private int grade;
}
