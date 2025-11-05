package domoe8bw91029;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMRead
{
    public static void main(String argv[]) throws SAXException, IOException, ParserConfigurationException
    {
      
        File xmlFile = new File("OE8BW9hallgato.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document neptunkod = dBuilder.parse(xmlFile);
        neptunkod.getDocumentElement().normalize();
        System.out.println("Gyökér elem: " + neptunkod.getDocumentElement().getNodeName());
        NodeList nList = neptunkod.getElementsByTagName("hallgato"); 

        for (int i = 0; i < nList.getLength(); i++) {
         
            Node nNode = nList.item(i);

            System.out.println("\nAktuális elem: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

              
                Element elem = (Element) nNode;

               
                String hid = elem.getAttribute("id");
    

                
                Node node1 = elem.getElementsByTagName("keresztnev").item(0);
                String kname = node1.getTextContent();

                Node node2 = elem.getElementsByTagName("vezeteknev").item(0);
                String vname = node2.getTextContent();

                Node node3 = elem.getElementsByTagName("foglalkozas").item(0);
                String fname = node3.getTextContent();

                System.out.println("Hallgató id: " + hid);
                System.out.println("Keresztnév: " + kname);
                System.out.println("Vezetéknév: " + vname);
                System.out.println("Foglalkozás: " + fname);
                
            }
        }
    }
}
