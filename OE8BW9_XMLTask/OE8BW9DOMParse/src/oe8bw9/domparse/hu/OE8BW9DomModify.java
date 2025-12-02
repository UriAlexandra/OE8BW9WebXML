package oe8bw9.domparse.hu;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

public class OE8BW9DomModify {

    public static void main(String[] argv) {
        try {
            // XML fájl betöltése
            File inputFile = new File("OE8BW9_XML.xml");

            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(inputFile);
            doc.getDocumentElement().normalize();

            System.out.println("Győkérelem: " + doc.getDocumentElement().getNodeName() + "\n");

            // ============================================================
            // 1) Étel (EID = 1) árának módosítása +500 Ft-tal
            // ============================================================
            title("1) Étel módosítása (EID=1 → ár + 500 Ft)");

            // Keresés attribútum alapján
            Element etel1 = byAttr(doc, "étel", "EID", "1");
            if (etel1 != null) {
                // Jelenlegi ár lekérése
                int currentPrice = parseInt(getText(etel1, "ár"), 0);
                int newPrice = currentPrice + 500;

                // Új ár beállítása
                setText(etel1, "ár", String.valueOf(newPrice));

                // Ellenőrző kiírás
                printEtel(etel1);
            } else
                System.out.println("Nincs EID=1 étel.\n");


            // ============================================================
            // 2) Rendelt tétel (RID=1, EID=6): megjegyzés hozzáadása
            // ============================================================
            title("2) Rendelt tétel módosítása (RID=1, EID=6 → megjegyzés hozzáadása)");

            // Végigmegyünk minden 'rendelt' elemen
            NodeList rendeltItems = doc.getElementsByTagName("rendelt");
            for (int i = 0; i < rendeltItems.getLength(); i++) {
                Element e = (Element) rendeltItems.item(i);

                // Megfelelő RID és EID keresése
                if ("1".equals(e.getAttribute("RID")) && "6".equals(e.getAttribute("EID"))) {

                    // Ha nincs megjegyzés elem adott tartalommal, hozzáadjuk
                    addChildIfMissingWithText(doc, e, "megjegyzés", "saját kérés");

                    // Ellenőrző kiírás
                    printRendelt(e);
                    break;
                }
            }


            // ============================================================
            // 3) Fizetés (FID=4): fizetési mód módosítása
            // ============================================================
            title("3) Fizetés módosítása (FID=4 → fizetés_módja bankkártyára)");

            Element fizetes4 = byAttr(doc, "fizetés", "FID", "4");
            if (fizetes4 != null) {

                // Fizetés módjának átállítása
                setText(fizetes4, "fizetés_módja", "bankkártya");

                // Ellenőrző kiírás
                printFizetes(fizetes4);
            } else
                System.out.println("Nincs FID=4 fizetés.\n");


            // ============================================================
            // 4) Megrendelő (MID=3): visszatérő vendég státusz átállítása
            // ============================================================
            title("4) Megrendelő módosítása (MID=3 → visszatérő_vendég: igen)");

            Element megrendelo3 = byAttr(doc, "megrendelő", "MID", "3");
            if (megrendelo3 != null) {

                // Aktuális státusz lekérése
                String status = getText(megrendelo3, "visszatérő_vendég");

                // Ha még "nem", akkor átírjuk "igen"-re
                if ("nem".equals(status)) {
                   setText(megrendelo3, "visszatérő_vendég", "igen");
                   printMegrendelo(megrendelo3);
                } else {
                    System.out.println("A megrendelő már visszatérő vendég.\n");
                }
            } else
                System.out.println("Nincs MID=3 megrendelő.\n");


            System.out.println("=== Kész ===");
            
            // Módosított XML mentése
            saveXml(doc, "MOD_OE8BW9XML.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // --------------------------------------------------
    // Segédfüggvények
    // --------------------------------------------------

    // Blokkfejlécek formázott kiírása
    private static void title(String t) {
        System.out.println("-------------- " + t + " --------------");
    }
    
    // XML dokumentum mentése
    private static void saveXml(Document doc, String filename) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // Szépen formázott mentés
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filename));

        transformer.transform(source, result);

        System.out.println("\nMentve: " + filename);
    }

    // --------------------------------------------------
    // Ellenőrző kiíró függvények
    // --------------------------------------------------

    private static void printEtel(Element etel) {
        System.out.println("étel [EID=" + etel.getAttribute("EID") + "]");
        System.out.println("  név: " + getText(etel, "név"));
        System.out.println("  ár: " + getText(etel, "ár"));
        System.out.println();
    }
    
    private static void printRendelt(Element rendelt) {
        System.out.println("rendelt [RID=" + rendelt.getAttribute("RID")
                + ", EID=" + rendelt.getAttribute("EID") + "]");
        System.out.println("  mennyiség: " + getText(rendelt, "mennyiség"));
        System.out.println("  megjegyzés: " + getText(rendelt, "megjegyzés"));
        System.out.println();
    }

    private static void printFizetes(Element fizetes) {
        System.out.println("fizetés [FID=" + fizetes.getAttribute("FID") + "]");
        System.out.println("  fizetés_módja: " + getText(fizetes, "fizetés_módja"));
        System.out.println();
    }
    
    private static void printMegrendelo(Element megrendelo) {
        System.out.println("megrendelő [MID=" + megrendelo.getAttribute("MID") + "]");
        System.out.println("  visszatérő_vendég: " + getText(megrendelo, "visszatérő_vendég"));
        System.out.println();
    }

    // --------------------------------------------------
    // DOM műveletek
    // --------------------------------------------------

    // Elem keresése attribútum alapján
    private static Element byAttr(Document doc, String tag, String attr, String val) {
        NodeList nl = doc.getElementsByTagName(tag);
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                if (val.equals(e.getAttribute(attr)))
                    return e;
            }
        }
        return null;
    }

    // Gyermekelem szövegének lekérése
    private static String getText(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagName(tag);
        return (nl.getLength() > 0 && nl.item(0).getTextContent() != null)
                ? nl.item(0).getTextContent().trim()
                : "";
    }

    // Gyermekelem szövegének módosítása / létrehozása
    private static void setText(Element parent, String tag, String value) {
        NodeList nl = parent.getElementsByTagName(tag);

        if (nl.getLength() == 0) {
            // Ha elem nem létezik → létrehozzuk
            Element child = parent.getOwnerDocument().createElement(tag);
            child.setTextContent(value);
            parent.appendChild(child);
        } else {
            // Ha létezik → átírjuk
            nl.item(0).setTextContent(value);
        }
    }

    // Olyan gyermekelem hozzáadása, ami még nincs meg ilyen tartalommal
    private static void addChildIfMissingWithText(Document doc, Element parent, String tag, String text) {
        NodeList nl = parent.getElementsByTagName(tag);

        // Ha már létezik ugyanolyan tartalom, nem teszünk hozzá semmit
        for (int i = 0; i < nl.getLength(); i++) {
            if (text.equalsIgnoreCase(nl.item(i).getTextContent().trim()))
                return;
        }

        // Létrehozunk egy új elemet
        Element child = doc.createElement(tag);
        child.setTextContent(text);
        parent.appendChild(child);
    }

    // Biztonságos String → int átalakító
    private static int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }
}
