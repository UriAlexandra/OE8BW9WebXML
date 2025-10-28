package domOE8BW91015;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import java.io.File;

public class DomReadOE8BW9 {

    public static void main(String[] args) {
        try {
            File xmlFile = new File("orarendOE8BW9.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            System.out.println("Gyökérelem: " + doc.getDocumentElement().getNodeName());
            System.out.println("==========================================222");

            NodeList oraLista = doc.getElementsByTagName("ora");

            for (int i = 0; i < oraLista.getLength(); i++) {
                Node oraNode = oraLista.item(i);

                if (oraNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element oraElem = (Element) oraNode;

                    System.out.println("Óra ID: " + oraElem.getAttribute("id"));
                    System.out.println("Típus: " + oraElem.getAttribute("tipus"));

                    System.out.println("Tárgy: " + oraElem.getElementsByTagName("targy").item(0).getTextContent());

                    Element idopontElem = (Element) oraElem.getElementsByTagName("idopont").item(0);
                    System.out.println("Időpont: " +
                            idopontElem.getElementsByTagName("nap").item(0).getTextContent() + " " +
                            idopontElem.getElementsByTagName("tol").item(0).getTextContent() + "-" +
                            idopontElem.getElementsByTagName("ig").item(0).getTextContent());

                    System.out
                            .println("Helyszín: " + oraElem.getElementsByTagName("helyszin").item(0).getTextContent());
                    System.out.println("Oktató: " + oraElem.getElementsByTagName("oktato").item(0).getTextContent());
                    System.out.println("Szak: " + oraElem.getElementsByTagName("szak").item(0).getTextContent());
                    System.out.println("------------------------------------------");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}