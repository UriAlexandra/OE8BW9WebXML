package domOE8BW91029;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomReadOE8BW9 {

    public static void main(String argv[]) throws SAYException, IOException, ParserConfigurationException {

        File xmlFile = new File("OE8BW9_1029/hallgatok.xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        //DOM fa előállítása
        Document neptunkod = dBuilder.parse(xmlFile);

        neptunkod.getDocumentElement().normalize();

        System.out.println("Gyökérelem: " + neptunkod.getDocumentElement().getNodeName());

        NodeList nList = neptunkod.getElementsByTagName("hallgato"); //gyerekelem mentés listába

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("\nAktuális elem: " + nNode.getNodeName());

            if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String hid = elem.getAttribute("id");

                Node node1 = elem.getElementsByTagName("keresztnev").item(0);
                String kname = node1.getTextContent();

                Node node2 = elem.getElementsByTagName("vezeteknev").item(0);
                String vname = node2.getTextContent();

                Node node3 = elem.getElementsByTagName("foglalkozas").item(0);
                String foglalkozas = node3.getTextContent();

                System.out.println("Hallgató ID: " + hid);
                System.out.println("Keresztnév: " + kname);
                System.out.println("Vezetéknév: " + vname);
                System.out.println("Foglalkozás: " + foglalkozas);
            }
        }
        }

    }

