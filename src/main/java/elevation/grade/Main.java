package elevation.grade;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    static final double earthRadius = 6371; // Approximate radius of the earth in kilometers


    public Main() throws ParserConfigurationException, IOException, SAXException {
        List<Coord> coords = new ArrayList<>();
        Coord coordPrev = null;
        Coord coord;

        Document document = getDocument();
        NodeList nodeListTrkpt = document.getElementsByTagName("trkpt");
        int lengthNl = nodeListTrkpt.getLength();
        for (int i = 0; i < lengthNl; i++) {
            coord = new Coord();
            Node nodeTrkpt = nodeListTrkpt.item(i);
            NamedNodeMap attributesNodeTrkpt = nodeTrkpt.getAttributes();
            coord.setLat(getParsedFloat(attributesNodeTrkpt, "lat"));
            coord.setLon(getParsedFloat(attributesNodeTrkpt, "lon"));

            NodeList childNodesNodeTrkpt = nodeTrkpt.getChildNodes();
            int length = childNodesNodeTrkpt.getLength();
            for (int j = 0; j < length; j++) {
                Node item = childNodesNodeTrkpt.item(j);
                if (item.getNodeName().equals("ele")) {
                    Node nodeEle = item.getFirstChild();
                    String nodeValueEle = nodeEle.getNodeValue();
                    coord.setEle(Float.parseFloat(nodeValueEle));

                    if (coordPrev != null) {
                        int grade = calculateGrade(coordPrev, coord);
                        coord.setGrade(grade);
                        coords.add(coord);
                    }

                    coordPrev = coord;
                    break;
                }
            }
        }

        Comparator<Coord> comparator = Comparator.comparingInt(Coord::getGrade);
        coords.stream().sorted(comparator).forEach(coord1 -> System.out.println(coord1.getGrade()+"% "+ coord1.link()));
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        new Main();
    }

    private static float getParsedFloat(NamedNodeMap attributesNodeTrkpt, String name) {
        Node nodeLat = attributesNodeTrkpt.getNamedItem(name);
        String nodeLatNodeValue = nodeLat.getNodeValue();
        float parsedFloat = Float.parseFloat(nodeLatNodeValue);
        return parsedFloat;
    }

    private static Document getDocument() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(new File("src/main/resources/route.gpx"));
        document.getDocumentElement().normalize();
        return document;
    }

    private int calculateGrade(Coord coordPrev, Coord coord) {
        double distance = getDistance(coordPrev.getLat(), coordPrev.getLon(), coord.getLat(), coord.getLon());
        System.out.println("distance: " + distance);

        float elevation = coord.getEle() - coordPrev.getEle();
        System.out.println("elevation: " + elevation);

        double grade = (elevation / distance) * 100;
        int IntValue = (int) Math.round(grade);

        System.out.println("grade: " + IntValue);

        return IntValue;
    }

    /**
     * Get the distance in meters between two coordinates.
     *
     * @param lat1  Latitude of the first coordinate, in degrees
     * @param long1 Longitude of the first coordinate, in degrees
     * @param lat2  Latitude of the second coordinate, in degrees
     * @param long2 Longitude of the second coordinate, in degrees
     * @return Distance in kilometers
     */
    private double getDistance(double lat1, double long1, double lat2, double long2) {
        double distance = Math.acos(Math.sin(lat2 * Math.PI / 180.0) * Math.sin(lat1 * Math.PI / 180.0) +
                Math.cos(lat2 * Math.PI / 180.0) * Math.cos(lat1 * Math.PI / 180.0) *
                        Math.cos((long1 - long2) * Math.PI / 180.0)) * earthRadius;
        return distance * 1000;
    }

}