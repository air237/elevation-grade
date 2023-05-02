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
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(new File("src/main/resources/TdZ_hosszu_2022.gpx"));
        document.getDocumentElement().normalize();

        NodeList nodeListTrkpt = document.getElementsByTagName("trkpt");
        int lengthNl = nodeListTrkpt.getLength();
        float lat;
        float lon;
        float ele;

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

            NodeList childNodesNodeTrkpt = nodeTrkpt.getChildNodes();
            int length = childNodesNodeTrkpt.getLength();
            for (int j = 0; j < length; j++) {
                Node item = childNodesNodeTrkpt.item(j);
                if (item.getNodeName().equals("ele")) {
                    Node nodeEle = item.getFirstChild();
                    String nodeValueEle = nodeEle.getNodeValue();
                    System.out.println("ele: " + nodeValueEle);
                    ele = Float.parseFloat(nodeValueEle);
                    break;
                }
            }

            System.out.println("");
        }
    }
}