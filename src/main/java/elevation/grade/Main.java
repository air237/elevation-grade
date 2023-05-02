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

public class Main {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        new Main();
    }


    public Main() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(new File("src/main/resources/TdZ_hosszu_2022.gpx"));
        document.getDocumentElement().normalize();

        float latPrev = -1;
        float lonPrev = -1;
        float elePrev = -1;
        float lat;
        float lon;
        float ele;

        NodeList nodeListTrkpt = document.getElementsByTagName("trkpt");
        int lengthNl = nodeListTrkpt.getLength();
        for (int i = 0; i < lengthNl; i++) {
            Node nodeTrkpt = nodeListTrkpt.item(i);
            NamedNodeMap attributesNodeTrkpt = nodeTrkpt.getAttributes();
            Node nodeLat = attributesNodeTrkpt.getNamedItem("lat");
            Node nodeLon = attributesNodeTrkpt.getNamedItem("lon");
            String nodeLatNodeValue = nodeLat.getNodeValue();
            String nodeLonNodeValue = nodeLon.getNodeValue();
            System.out.println("nodeLat: " + nodeLatNodeValue);
            System.out.println("nodeLon: " + nodeLonNodeValue);
            lat = Float.parseFloat(nodeLatNodeValue);
            lon = Float.parseFloat(nodeLonNodeValue);
            System.out.println("latPrev: " + latPrev);
            System.out.println("lonPrev: " + lonPrev);

            NodeList childNodesNodeTrkpt = nodeTrkpt.getChildNodes();
            int length = childNodesNodeTrkpt.getLength();
            for (int j = 0; j < length; j++) {
                Node item = childNodesNodeTrkpt.item(j);
                if (item.getNodeName().equals("ele")) {
                    Node nodeEle = item.getFirstChild();
                    String nodeValueEle = nodeEle.getNodeValue();
                    System.out.println("ele: " + nodeValueEle);
                    ele = Float.parseFloat(nodeValueEle);

                    if (latPrev != -1) {
                        double distance = getDistance(latPrev, lonPrev, lat, lon);
//                        double distance = distance(latPrev, lonPrev, lat, lon);
                        System.out.println("distance: "+ distance);


                        float elevation = ele - elePrev;
                        System.out.println("elevation: "+ elevation);

                        double grade = (elevation / distance)*100;
                        System.out.println("grade: "+ grade);

                    }

                    elePrev = ele;
                    break;
                }
            }

            latPrev = lat;
            lonPrev = lon;

            System.out.println("");
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

     static final double earthRadius = 6371; // Approximate radius of the earth in kilometers

    /**
     * Get the distance in kilometers between two coordinates.
     * @param lat1 Latitude of the first coordinate, in degrees
     * @param long1 Longitude of the first coordinate, in degrees
     * @param lat2 Latitude of the second coordinate, in degrees
     * @param long2 Longitude of the second coordinate, in degrees
     * @return Distance in kilometers
     */
    private double getDistance(double lat1, double long1, double lat2, double long2) {
        double distance = Math.acos(Math.sin(lat2 * Math.PI / 180.0) * Math.sin(lat1 * Math.PI / 180.0) +
                Math.cos(lat2 * Math.PI / 180.0) * Math.cos(lat1 * Math.PI / 180.0) *
                        Math.cos((long1 - long2) * Math.PI / 180.0)) * earthRadius;
        return distance *1000;
    }

}