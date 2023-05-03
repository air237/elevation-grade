package elevation.grade;

import lombok.Data;

@Data
public class Coord {
    private float lat;
    private float lon;
    private float latPrev;
    private float lonPrev;
    private float ele;
    private int grade;

    public String link() {
        return "https://maps.google.com/?q=" + this.getLat() + "," + this.getLon();
    }

    public String direction() {
        return "https://www.google.com/maps/dir/?api=1&origin=" + this.getLatPrev() + "," + this.getLonPrev() + "&destination=" + this.getLat() + "," + this.getLon();
    }


}
