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
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new File("src/main/resources/TdZ_hosszu_2022.gpx"));
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("trkpt");
        for (int i = 0; i < 2; i++) {
            Node first = nodeList.item(i);
            NamedNodeMap attributes = first.getAttributes();
            Node lat = attributes.getNamedItem("lat");
            Node lon = attributes.getNamedItem("lon");
            System.out.println("lat: " + lat.getNodeValue());
            System.out.println("lon: " + lon.getNodeValue());

            NodeList childNodes = first.getChildNodes();
            Node item = childNodes.item(1);
            if (item.getNodeName().equals("ele")) {
                Node firstChild = item.getFirstChild();
                String nodeValue = firstChild.getNodeValue();
                System.out.println("ele: " + nodeValue);
            }

            System.out.println("\n");
        }
    }
}