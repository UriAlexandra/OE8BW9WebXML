package OE8BW9_1112.xpathOE8BW9;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class xPathQueryOE8BW9 {

    public static void main(String[] args) {
        try {
            // XML betöltése
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("student.xml");

            // Normalizálás
            document.getDocumentElement().normalize();

            // XPath létrehozása
            XPath xPath = XPathFactory.newInstance().newXPath();

            // XPath kifejezés kiértékelése
            NodeList nepunkod = (NodeList) xPath.compile("/class/student").evaluate(document, XPathConstants.NODESET);

            // Csomópontok bejárása
            for (int i = 0; i < nepunkod.getLength(); i++) {
                Node node = nepunkod.item(i);
                System.out.println("\nAktuális elem : " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("student")) {
                    Element element = (Element) node;
                    System.out.println("Hallgató ID: " + element.getAttribute("id"));
                    System.out.println("Keresztnév: " + element.getElementsByTagName("keresztnev").item(0).getTextContent());
                    System.out.println("Vezetéknév: " + element.getElementsByTagName("vezeteknev").item(0).getTextContent());
                    System.out.println("Becenév: " + element.getElementsByTagName("becenev").item(0).getTextContent());
                    System.out.println("Kor: " + element.getElementsByTagName("kor").item(0).getTextContent());
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
