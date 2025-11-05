package domoe8bw91029;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

import java.io.*;

public class DOMWrite1 {

    public static void main(String[] args) {
        String inputPath = "orarendOE8BW9.xml";   
        String outputPath = "orarendOE8BW9.xml";

        try {
            File xmlFile = new File(inputPath);
            if (!xmlFile.exists()) {
                System.err.println("Nem találom a fájlt: " + xmlFile.getAbsolutePath());
                System.exit(1);
            }

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
            doc.getDocumentElement().normalize();

            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
                String header = "[DOCUMENT]";
                writer.write(header + "\n");
                System.out.println(header);

                printNode(doc.getDocumentElement(), "", true, writer);
            }

            System.out.println("\nFastruktúra sikeresen kiírva ide: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printNode(Node node, String indent, boolean isLast, BufferedWriter writer) throws IOException {
        String branch = indent + (indent.isEmpty() ? "└─ " : (isLast ? "└─ " : "├─ "));
        String outputLine = "";

        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                Element el = (Element) node;
                StringBuilder line = new StringBuilder();
                line.append(branch).append("<").append(el.getTagName()).append(">");

                // Attribútumok kiírása
                NamedNodeMap attrs = el.getAttributes();
                if (attrs != null && attrs.getLength() > 0) {
                    line.append(" ");
                    for (int i = 0; i < attrs.getLength(); i++) {
                        Node a = attrs.item(i);
                        line.append("@").append(a.getNodeName())
                            .append("=\"").append(a.getNodeValue()).append("\"");
                        if (i < attrs.getLength() - 1) 
                        	line.append(" ");
                    }
                }

                outputLine = line.toString();
                writer.write(outputLine + "\n");
                System.out.println(outputLine);

                // Gyerekek feldolgozása
                NodeList children = node.getChildNodes();
                int visibleCount = 0;
                for (int i = 0; i < children.getLength(); i++) {
                    Node c = children.item(i);
                    if (isIgnorableText(c)) 
                    	continue;
                    
                    visibleCount++;
                }

                int seen = 0;
                for (int i = 0; i < children.getLength(); i++) {
                    Node c = children.item(i);
                    if (isIgnorableText(c)) 
                    	continue;
                    seen++;
                    printNode(
                        c,
                        indent + (isLast ? "   " : "│  "),
                        seen == visibleCount,
                        writer
                    );
                }
                break;

            case Node.TEXT_NODE:
                String text = node.getNodeValue().trim();
                if (!text.isEmpty()) {
                    outputLine = branch + "\"" + text + "\"";
                    writer.write(outputLine + "\n");
                    System.out.println(outputLine);
                }
                break;

            case Node.COMMENT_NODE:
                outputLine = branch + "<!-- " + node.getNodeValue().trim() + " -->";
                writer.write(outputLine + "\n");
                System.out.println(outputLine);
                break;

            default:
                outputLine = branch + "(nodeType=" + node.getNodeType() + ")";
                writer.write(outputLine + "\n");
                System.out.println(outputLine);
        }
    }

    private static boolean isIgnorableText(Node n) {
        if (n.getNodeType() != Node.TEXT_NODE) return false;
        return n.getNodeValue() == null || n.getNodeValue().trim().isEmpty();
    }
}
