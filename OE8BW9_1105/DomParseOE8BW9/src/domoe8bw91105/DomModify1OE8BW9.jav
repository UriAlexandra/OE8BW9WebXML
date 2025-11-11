package domoe8bw91105;

import java.io.File;
import java.lang.annotation.Documented;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;


public class DomModify1OE8BW9 {

    public static void main(String[] argv) {
        try {
            // --- Bemeneti és kimeneti fájlok ---
            File inputFile = new File("orarendOE8BW9.xml");
            File outputFile = new File("orarendModify1OE8BW9.xml");

            // --- DOM előkészítés (beolvasás) ---
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputFile);

            // Gyökérelem lekérése
            Node orarend = doc.getFirstChild();

            // ============================================================
            // 1.) Egy példány módosítása: <oraado> elem hozzáadása
            // ============================================================
            Node ora = doc.getElementsByTagName("ora").item(0);
            if (ora != null && ora.getNodeType() == Node.ELEMENT_NODE) {

                NamedNodeMap attr = ora.getAttributes();
                Node idAttr = attr.getNamedItem("id");
                if (idAttr != null) {
                    idAttr.setTextContent(idAttr.getTextContent());
                }

                // Új <oraado> elem létrehozása és szöveg beállítása
                Element oraado = doc.createElement("oraado");
                oraado.setTextContent("Vendég oktató: Kovács Péter");
                // Hozzáfűzés az adott <ora> elemhez
                ora.appendChild(oraado);
            }

            // --- 1.) Kiírás konzolra és fájlba ---
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // DOM forrás
            DOMSource source = new DOMSource(doc);

            // Konzolra
            System.out.println("--- 1.) Modositott fajl (konzolra) ---");
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);

            // Fájlba
            transformer.transform(new DOMSource(doc), new StreamResult(outputFile));
            System.out.println("\n--- 1.) Mentes kesz: " + outputFile.getName() + " ---\n");

            // ==================================================================
            // 2.) Minden óra tipusának módosítása: "gyakorlat" -> "eloadas"
            // és a teljes fájl strukturált kiírása a konzolra.
            // (A módosítást az aktuálisan memóriában lévő doc-on végezzük.)
            // ==================================================================

            // Összes <ora> elem bejárása és a tipus attribútum átírása
            NodeList orak = doc.getElementsByTagName("ora");
            for (int i = 0; i < orak.getLength(); i++) {
                Node o = orak.item(i);
                if (o.getNodeType() == Node.ELEMENT_NODE) {
                    NamedNodeMap a = o.getAttributes();
                    Node tipusAttr = a.getNamedItem("tipus");
                    if (tipusAttr != null) {
                        if ("gyakorlat".equalsIgnoreCase(tipusAttr.getTextContent())) {
                            tipusAttr.setTextContent("eloadas");
                        }
                    }
                }
            }

            // Strukturált kiírás a konzolra
            System.out.println("--- 2.) Strukturalt forma (minden ora listazasa) ---");
            for (int i = 0; i < orak.getLength(); i++) {
                Node o = orak.item(i);
                if (o.getNodeType() == Node.ELEMENT_NODE) {
                    Element eOra = (Element) o;

                    // Attribútumok (id, tipus)
                    String id = eOra.hasAttribute("id") ? eOra.getAttribute("id") : "";
                    String tipus = eOra.hasAttribute("tipus") ? eOra.getAttribute("tipus") : "";

                    System.out.println("Ora [id=" + id + ", tipus=" + tipus + "]");

                    // Gyerek elemek bejárása és kiírása
                    NodeList children = eOra.getChildNodes();
                    for (int j = 0; j < children.getLength(); j++) {
                        Node c = children.item(j);
                        if (c.getNodeType() == Node.ELEMENT_NODE) {
                            Element eChild = (Element) c;

                            if ("idopont".equals(eChild.getNodeName())) {
                                // Összetett elem részletezése
                                NodeList timeParts = eChild.getChildNodes();
                                for (int k = 0; k < timeParts.getLength(); k++) {
                                    Node t = timeParts.item(k);
                                    if (t.getNodeType() == Node.ELEMENT_NODE) {
                                        Element eT = (Element) t;
                                        System.out
                                                .println("  idopont/" + eT.getNodeName() + ": " + eT.getTextContent());
                                    }
                                }
                            } else {
                                // Egyszerű gyermekelemek (targy, helyszin, oktato, szak, oraado stb.)
                                System.out.println("  " + eChild.getNodeName() + ": " + eChild.getTextContent());
                            }
                        }
                    }
                    System.out.println(); // üres sor az olvashatóságért
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}