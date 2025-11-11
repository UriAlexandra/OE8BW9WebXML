package domoe8bw91105;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomQuery1OE8BW9 {
    

    public static void main(String[] args) {
        try {
            // --- Bemeneti/Kimeneti fájlok ---
            File inputFile = new File("orarendOE8BW9.xml");
            File firstOraOut = new File("orarendQuery1FirstOra.txt"); // 2.) kimeneti fájl

            // --- DOM beolvasás ---
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputFile);
            doc.getDocumentElement().normalize();

            // ========== 1.) Kurzusnevek listába, majd konzolra ==========
            List<String> kurzusok = new ArrayList<>();
            NodeList targyak = doc.getElementsByTagName("targy");
            for (int i = 0; i < targyak.getLength(); i++) {
                Node n = targyak.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    kurzusok.add(n.getTextContent().trim());
                }
            }
            System.out.println("Kurzusnév: " + kurzusok);

            // ========== 2.) Az első <ora> példány strukturált kiírása ==========
            NodeList orak = doc.getElementsByTagName("ora");
            if (orak.getLength() > 0) {
                Node first = orak.item(0);
                String structured = strukturaltOra((Element) first);

                // Konzolra
                System.out.println();
                System.out.println("--- Első <ora> strukturált kiírása ---");
                System.out.println(structured);

                // Fájlba
                try (PrintWriter pw = new PrintWriter(firstOraOut, "UTF-8")) {
                    pw.println(structured);
                }
                System.out.println("--- Mentve fájlba: " + firstOraOut.getName() + " ---");
            }

            // ========== 3.) Oktatók nevei listába, majd konzolra ==========
            // LinkedHashSet: megőrzi a talált sorrendet és kiszűri a duplikátumokat
            Set<String> oktatok = new LinkedHashSet<>();
            NodeList oktatoNodes = doc.getElementsByTagName("oktato");
            for (int i = 0; i < oktatoNodes.getLength(); i++) {
                Node n = oktatoNodes.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    oktatok.add(n.getTextContent().trim());
                }
            }
            System.out.println();
            System.out.println("Oktatók: " + new ArrayList<>(oktatok));

            // ========== 4.) Összetett lekérdezés ==========
            // Feltétel: helyszin "L..."-lel kezdődik ÉS <tol> >= 14
            System.out.println();
            System.out.println("--- Összetett lekérdezés (helyszin=L*, tol>=14) ---");
            for (int i = 0; i < orak.getLength(); i++) {
                Element eOra = (Element) orak.item(i);
                String helyszin = getText(eOra, "helyszin");
                String tolStr = getText((Element) eOra.getElementsByTagName("idopont").item(0), "tol");
                int tol = parseIntSafe(tolStr, -1);

                if (helyszin != null && helyszin.startsWith("L") && tol >= 14) {
                    String targy = getText(eOra, "targy");
                    String nap = getText((Element) eOra.getElementsByTagName("idopont").item(0), "nap");
                    String igStr = getText((Element) eOra.getElementsByTagName("idopont").item(0), "ig");
                    System.out.println(
                            targy + " | " + nap + " " + tol + "-" + igStr + " | helyszín: " + helyszin);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------- Segédfüggvények ---------

    // Egy <ora> elem ember-olvasható, strukturált szövegét építi fel
    private static String strukturaltOra(Element eOra) {
        StringBuilder sb = new StringBuilder();
        String id = eOra.hasAttribute("id") ? eOra.getAttribute("id") : "";
        String tipus = eOra.hasAttribute("tipus") ? eOra.getAttribute("tipus") : "";

        sb.append("Ora [id=").append(id).append(", tipus=").append(tipus).append("]\n");
        sb.append("  targy: ").append(getText(eOra, "targy")).append("\n");

        Element idopont = (Element) eOra.getElementsByTagName("idopont").item(0);
        if (idopont != null) {
            sb.append("  idopont/nap: ").append(getText(idopont, "nap")).append("\n");
            sb.append("  idopont/tol: ").append(getText(idopont, "tol")).append("\n");
            sb.append("  idopont/ig: ").append(getText(idopont, "ig")).append("\n");
        }

        sb.append("  helyszin: ").append(getText(eOra, "helyszin")).append("\n");
        sb.append("  oktato: ").append(getText(eOra, "oktato")).append("\n");
        sb.append("  szak: ").append(getText(eOra, "szak")).append("\n");

        // opcionális extra elem, ha előző feladatból már bekerült
        String oraado = getText(eOra, "oraado");
        if (oraado != null && !oraado.isEmpty()) {
            sb.append("  oraado: ").append(oraado).append("\n");
        }
        return sb.toString();
    }

    // Visszaadja az adott szülő elem első "tagName" gyermekének a szövegét
    private static String getText(Element parent, String tagName) {
        if (parent == null)
            return "";
        NodeList nl = parent.getElementsByTagName(tagName);
        if (nl == null || nl.getLength() == 0)
            return "";
        Node n = nl.item(0);
        return n.getTextContent().trim();
    }

    private static int parseIntSafe(String s, int def) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }
}