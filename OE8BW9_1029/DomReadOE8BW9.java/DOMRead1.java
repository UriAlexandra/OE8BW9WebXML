package domuoe8bw91029;

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

public class DOMRead1 {

	 public static void main(String argv[]) throws SAXException, IOException, ParserConfigurationException
	    {
	      
	        File xmlFile = new File("orarendOE8BW9.xml");
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = factory.newDocumentBuilder();
	        Document neptunkod = dBuilder.parse(xmlFile);
	        neptunkod.getDocumentElement().normalize();
	        System.out.println("Gyökér elem: " + neptunkod.getDocumentElement().getNodeName());
	        NodeList nList = neptunkod.getElementsByTagName("ora"); 

	        for (int i = 0; i < nList.getLength(); i++) {
	         
	            Node nNode = nList.item(i);

	            System.out.println("\nAktuális elem: " + nNode.getNodeName());

	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

	              
	                Element elem = (Element) nNode;

	               
	                String oid = elem.getAttribute("id");
	                String tipus = elem.getAttribute("tipus");

	                
	                Node node1 = elem.getElementsByTagName("targy").item(0);
	                String targy = node1.getTextContent();

	                Node node2 = elem.getElementsByTagName("nap").item(0);
	                String nap = node2.getTextContent();
	                
	                Node node3 = elem.getElementsByTagName("tol").item(0);
	               
	                String tol = node3.getTextContent();
	                
	                Node node4 = elem.getElementsByTagName("ig").item(0);
	                String ig = node4.getTextContent();
	                

	                Node node5 = elem.getElementsByTagName("helyszin").item(0);
	                String helyszin = node5.getTextContent();
	                
	                Node node6 = elem.getElementsByTagName("oktato").item(0);
	                String oktato = node6.getTextContent();
	                
	                Node node7 = elem.getElementsByTagName("szak").item(0);
	                String szak = node7.getTextContent();

	                System.out.println("Óra id: " + oid);
	                System.out.println("Óra típus: " + tipus);
	                System.out.println("Tárgy: " + targy);
	                System.out.println("Nap: " + nap);
	                System.out.println("Tól-Ig(óra): " + tol+"-"+ig);
	                
	                System.out.println("Helyszín: " + helyszin);
	                System.out.println("Oktató: " + oktato);
	                System.out.println("Szak: " + szak);
	                
	            }
	        }
	    }
}
