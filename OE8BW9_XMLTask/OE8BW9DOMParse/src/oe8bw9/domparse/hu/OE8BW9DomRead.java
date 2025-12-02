package oe8bw9.domparse.hu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OE8BW9DomRead {

    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {

        // Bemeneti XML és a kiírt TXT állomány
        File xmlFile = new File("OE8BW9_XML.xml");
        File outFile = new File("OE8BW9DomRead_output.txt");

        // DOM inicializálása
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();

        // XML beolvasása DOM dokumentumba
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        // Kiíratáshoz PrintWriter: a try-with-resources automatikusan zárja a fájlt
        try (PrintWriter pw = new PrintWriter(new FileWriter(outFile, false))) {

            // Gyökérelem kiírása
            String root = doc.getDocumentElement().getNodeName();
            printlnBoth(pw, "Gyökér elem: " + root);
            printlnBoth(pw, "----------------------------------------");

            // ======================================================================
            // 1. RENDELÉSEK BEJÁRÁSA
            // ======================================================================
            NodeList rendelesek = doc.getElementsByTagName("rendelés");
            for (int i = 0; i < rendelesek.getLength(); i++) {

                Node n = rendelesek.item(i);
                printlnBoth(pw, "\nAktuális elem: " + n.getNodeName());

                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) n;

                    // Attribútumok és gyermekelemek kiolvasása
                    String rid    = e.getAttribute("RID");
                    String mid    = e.getAttribute("MID");
                    String fid    = e.getAttribute("FID");
                    String datum  = getText(e, "dátum");
                    String cim    = getText(e, "kiszállítási_cím");
                    String osszeg = getText(e, "összeg");

                    // Kiírás konzolra és fájlba
                    printlnBoth(pw, "Rendelés azonosító (RID): " + rid);
                    printlnBoth(pw, "Megrendelő azonosító (MID): " + mid);
                    printlnBoth(pw, "Fizetés azonosító (FID): " + fid);
                    printlnBoth(pw, "Dátum: " + datum);
                    printlnBoth(pw, "Kiszállítási cím: " + cim);
                    printlnBoth(pw, "Összeg: " + osszeg);
                }
            }
            printlnBoth(pw, "\n----------------------------------------");


            // ======================================================================
            // 2. MEGRENDELŐK
            // ======================================================================
            NodeList megrendelok = doc.getElementsByTagName("megrendelő");
            for (int i = 0; i < megrendelok.getLength(); i++) {

                Node n = megrendelok.item(i);
                printlnBoth(pw, "\nAktuális elem: " + n.getNodeName());

                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) n;

                    String mid       = e.getAttribute("MID");
                    String visszatero = getText(e, "visszatérő_vendég");
                    String telefon   = getText(e, "telefonszám");
                    String nev       = getText(e, "név");

                    printlnBoth(pw, "Megrendelő azonosító (MID): " + mid);
                    printlnBoth(pw, "Visszatérő vendég: " + visszatero);
                    printlnBoth(pw, "Telefonszám: " + telefon);
                    printlnBoth(pw, "Név: " + nev);
                }
            }
            printlnBoth(pw, "\n----------------------------------------");


            // ======================================================================
            // 3. FIZETÉSEK
            // ======================================================================
            NodeList fizetesek = doc.getElementsByTagName("fizetés");
            for (int i = 0; i < fizetesek.getLength(); i++) {

                Node n = fizetesek.item(i);
                printlnBoth(pw, "\nAktuális elem: " + n.getNodeName());

                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) n;

                    String fid     = e.getAttribute("FID");
                    String atvitel = getText(e, "átvitel_ideje");
                    String kupon   = getText(e, "kupon");
                    String modja   = getText(e, "fizetés_módja");

                    printlnBoth(pw, "Fizetés azonosító (FID): " + fid);
                    printlnBoth(pw, "Átvitel ideje: " + atvitel);
                    printlnBoth(pw, "Kupon: " + kupon);
                    printlnBoth(pw, "Fizetés módja: " + modja);
                }
            }
            printlnBoth(pw, "\n----------------------------------------");


            // ======================================================================
            // 4. RENDELT TÉTELEK (kapcsolótábla)
            // ======================================================================
            NodeList rendeltetek = doc.getElementsByTagName("rendelt");
            for (int i = 0; i < rendeltetek.getLength(); i++) {

                Node n = rendeltetek.item(i);
                printlnBoth(pw, "\nAktuális elem: " + n.getNodeName());

                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) n;

                    String rid        = e.getAttribute("RID");
                    String eid        = e.getAttribute("EID");
                    String mennyiseg  = getText(e, "mennyiség");
                    String tetelar    = getText(e, "tétel_ár");
                    String megjegyzes = getText(e, "megjegyzés");

                    printlnBoth(pw, "Rendelés azonosító (RID): " + rid);
                    printlnBoth(pw, "Étel azonosító (EID): " + eid);
                    printlnBoth(pw, "Mennyiség: " + mennyiseg);
                    printlnBoth(pw, "Tétel ár: " + tetelar);
                    printlnBoth(pw, "Megjegyzés: " + megjegyzes);
                }
            }
            printlnBoth(pw, "\n----------------------------------------");


            // ======================================================================
            // 5. ÉTELEK
            // ======================================================================
            NodeList etelek = doc.getElementsByTagName("étel");
            for (int i = 0; i < etelek.getLength(); i++) {

                Node n = etelek.item(i);
                printlnBoth(pw, "\nAktuális elem: " + n.getNodeName());

                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) n;

                    String eid   = e.getAttribute("EID");
                    String nev   = getText(e, "név");
                    String ar    = getText(e, "ár");
                    String tipus = getText(e, "típus");

                    printlnBoth(pw, "Étel azonosító (EID): " + eid);
                    printlnBoth(pw, "Név: " + nev);
                    printlnBoth(pw, "Ár: " + ar);

                    // Több allergén elem lehet → külön bejárás
                    NodeList allergensek = e.getElementsByTagName("allergén");
                    for (int j = 0; j < allergensek.getLength(); j++) {
                        printlnBoth(pw, "Allergén: " + allergensek.item(j).getTextContent().trim());
                    }

                    printlnBoth(pw, "Típus: " + tipus);
                }
            }
            printlnBoth(pw, "\n----------------------------------------");


            // ======================================================================
            // 6. AKCIÓK
            // ======================================================================
            NodeList akciok = doc.getElementsByTagName("akció");
            for (int i = 0; i < akciok.getLength(); i++) {

                Node n = akciok.item(i);
                printlnBoth(pw, "\nAktuális elem: " + n.getNodeName());

                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) n;

                    String aid      = e.getAttribute("AID");
                    String eid      = e.getAttribute("EID");
                    String learazas = getText(e, "leárazás");

                    printlnBoth(pw, "Akció azonosító (AID): " + aid);
                    printlnBoth(pw, "Étel azonosító (EID): " + eid);

                    // Az időtartam egy nested komplex elem
                    Node idoNode = e.getElementsByTagName("időtartam").item(0);
                    if (idoNode != null) {
                        printlnBoth(pw, "Kezdete: " + getText((Element) idoNode, "kezdete"));
                        printlnBoth(pw, "Vége: " + getText((Element) idoNode, "vége"));
                    }

                    printlnBoth(pw, "Leárazás: " + learazas + "%");
                }
            }

            printlnBoth(pw, "\n----------------------------------------");
            printlnBoth(pw, "\n--- Mentve fájlba: " + outFile.getName() + " ---");
        }
    }

    // ======================================================================
    // Segédfüggvények
    // ======================================================================

    /** Gyermekelem szövegének biztonságos visszaadása. */
    private static String getText(Element parent, String tagName) {
        if (parent == null)
            return "";
        NodeList nl = parent.getElementsByTagName(tagName);
        if (nl == null || nl.getLength() == 0 || nl.item(0).getTextContent() == null)
            return "";
        return nl.item(0).getTextContent().trim();
    }

    /** Kiír egy sort a konzolra és a fájlba is. */
    private static void printlnBoth(PrintWriter pw, String line) {
        System.out.println(line);
        pw.println(line);
    }
}