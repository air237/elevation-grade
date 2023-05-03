package elevation.grade;

import lombok.Data;

import java.net.URL;

@Data
public class Coord {
    private float lat;
    private float lon;
    private float ele;
    private int grade;

    public String link() {
        return "https://maps.google.com/?q="+this.getLat()+","+this.getLon();
    }
}
