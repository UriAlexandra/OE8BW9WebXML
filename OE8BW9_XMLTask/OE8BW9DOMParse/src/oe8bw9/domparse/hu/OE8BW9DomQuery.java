package oe8bw9.domparse.hu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OE8BW9DomQuery {

    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {

        File xmlFile = new File("OE8BW9_XML.xml");

        // ==================================================
        // DOM inicializálás és XML beolvasás fájlból
        // ==================================================
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        printHeader("Gyökér elem: " + doc.getDocumentElement().getNodeName());


        // ==================================================
        // 1) Pizza típusú ételek nevének kilistázása
        // ==================================================
        startBlock("1) Pizza típusú ételek nevei");

        List<String> pizzaNevek = new ArrayList<>();
        NodeList etelek = doc.getElementsByTagName("étel"); // Összes étel elem

        for (int i = 0; i < etelek.getLength(); i++) {
            Node n = etelek.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;

                // Típus kiolvasása
                String tipus = getText(e, "típus");

                // Csak pizzákat gyűjtünk
                if ("Pizza".equals(tipus)) {
                    String nev = getText(e, "név");
                    if (!nev.isEmpty())
                        pizzaNevek.add(nev);
                }
            }
        }

        System.out.println("Pizzák: " + asList(pizzaNevek));
        endBlock();


        // ==================================================
        // 2) Megrendelők neve és telefonszáma
        // ==================================================
        startBlock("2) Megrendelők (név + telefonszám)");

        NodeList megrendelok = doc.getElementsByTagName("megrendelő");
        List<String> megrendeloLista = new ArrayList<>();

        for (int i = 0; i < megrendelok.getLength(); i++) {
            Node n = megrendelok.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;

                String nev = getText(e, "név");
                String telefon = getText(e, "telefonszám");

                // Telefonszámot csak akkor tesszük hozzá, ha létezik
                if (!nev.isEmpty()) {
                    megrendeloLista.add(nev + (telefon.isEmpty() ? "" : " (" + telefon + ")"));
                }
            }
        }

        System.out.println("Megrendelők: " + asList(megrendeloLista));
        endBlock();


        // ==================================================
        // 3) Készpénzes fizetések száma
        // ==================================================
        startBlock("3) Készpénzes fizetések száma");

        NodeList fizetesek = doc.getElementsByTagName("fizetés");
        int kpSzam = 0;

        for (int i = 0; i < fizetesek.getLength(); i++) {
            Node n = fizetesek.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;

                // Fizetés módjának lekérése
                String modja = getText(e, "fizetés_módja");

                if ("készpénz".equals(modja)) {
                    kpSzam++;
                }
            }
        }

        System.out.println("Készpénzes fizetések száma: " + kpSzam);
        endBlock();


        // ==================================================
        // 4) Összetett lekérdezés – "JOIN"-olt adatok összeállítása:
        //    rendelés dátuma, címe, megrendelő neve és fizetés módja
        // ==================================================
        startBlock("4) Rendelések adatai (dátum + cím + megrendelő + fizetés módja)");

        // Megrendelő ID → név táblázat
        Map<String, String> megrendeloNevById = new LinkedHashMap<>();
        for (int i = 0; i < megrendelok.getLength(); i++) {
            Element e = (Element) megrendelok.item(i);
            String mId = e.getAttribute("MID");
            String nev = getText(e, "név");
            if (!mId.isEmpty())
                megrendeloNevById.put(mId, nev);
        }

        // Fizetés ID → fizetés mód táblázat
        Map<String, String> fizetesModById = new LinkedHashMap<>();
        for (int i = 0; i < fizetesek.getLength(); i++) {
            Element e = (Element) fizetesek.item(i);
            String fId = e.getAttribute("FID");
            String modja = getText(e, "fizetés_módja");
            if (!fId.isEmpty())
                fizetesModById.put(fId, modja);
        }

        // Rendelések bejárása
        NodeList rendelesek = doc.getElementsByTagName("rendelés");
        for (int i = 0; i < rendelesek.getLength(); i++) {
            Element e = (Element) rendelesek.item(i);

            String mId = e.getAttribute("MID");  // Megrendelő ID
            String fId = e.getAttribute("FID");  // Fizetés ID

            String datum = getText(e, "dátum");
            String cim = getText(e, "kiszállítási_cím");

            // "JOIN": ID alapján név és fizetési mód lekérése
            String megrendeloNev = megrendeloNevById.getOrDefault(mId, "(ismeretlen megrendelő)");
            String fizetesMod = fizetesModById.getOrDefault(fId, "(ismeretlen fizetés)");

            System.out.println(
                "- " + datum +
                " | Cím: " + cim +
                " | Megrendelő: " + megrendeloNev +
                " | Fizetés módja: " + fizetesMod
            );
        }
        endBlock();
    }


    // ======================================================================
    // Segédfüggvények: formázás, blokkkezdet, blokkvég, DOM szövegolvasás
    // ======================================================================

    private static void printHeader(String title) {
        System.out.println("========================================");
        System.out.println(title);
        System.out.println("========================================\n");
    }

    private static void startBlock(String title) {
        System.out.println("-------------- " + title + " --------------");
    }

    private static void endBlock() {
        System.out.println("------------------------------------------\n");
    }

    // Egyszerű gyermekelem szövegének visszaadása
    private static String getText(Element parent, String tagName) {
        if (parent == null)
            return "";
        NodeList nl = parent.getElementsByTagName(tagName);
        if (nl == null || nl.getLength() == 0 || nl.item(0).getTextContent() == null)
            return "";
        return nl.item(0).getTextContent().trim();
    }

    // Lista formázása [a, b, c] formára
    private static String asList(List<String> items) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            sb.append(items.get(i));
            if (i < items.size() - 1)
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
