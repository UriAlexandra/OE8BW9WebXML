package domoe8bw91105;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class DomQueryOE8BW9 {

    public static void main(String[] args) {
        try {
            // Bemeneti XML
            File inputFile = new File("OE8BW9hallgato.xml");

            // DOM inicializálás
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Beolvasás
            Document doc = db.parse(inputFile);
            doc.getDocumentElement().normalize();

            // Gyökérelem kiírása
            String rootName = doc.getDocumentElement().getNodeName();
            System.out.println("Gyökér elem: " + rootName);
            System.out.println("------------------------------");

            // Minden <hallgato> példány lekérdezése
            NodeList hallgatok = doc.getElementsByTagName("hallgato");

            for (int i = 0; i < hallgatok.getLength(); i++) {
                Node hallgato = hallgatok.item(i);

                if (hallgato.getNodeType() == Node.ELEMENT_NODE) {
                    Element eHallgato = (Element) hallgato;

                    System.out.println();
                    System.out.println("Aktuális elem:");
                    System.out.println(eHallgato.getNodeName());

                    // A gyermekelemek között keressük a <vezeteknev>-et
                    NodeList children = eHallgato.getChildNodes();
                    for (int j = 0; j < children.getLength(); j++) {
                        Node child = children.item(j);
                        if (child.getNodeType() == Node.ELEMENT_NODE) {
                            Element eChild = (Element) child;
                            if ("vezeteknev".equals(eChild.getNodeName())) {
                                System.out.println("vezeteknev: " + eChild.getTextContent());
                                break; // találtunk, mehetünk a következő hallgatóra
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
