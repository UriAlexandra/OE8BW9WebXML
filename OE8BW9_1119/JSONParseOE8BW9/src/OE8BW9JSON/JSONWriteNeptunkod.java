package OE8BW9_1119.JSONParseOE8BW9.src.OE8BW9JSON;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class JSONWriteNeptunkod {
    public static void main(String[] args) {
        String inputFile = "orarendNeptunkod.json";
        String outputFile = "orarendNeptunkod1.json";

        try {
            String content = new String(Files.readAllBytes(Paths.get(inputFile)));

            JSONObject jsonObject = new JSONObject(content);

            System.out.println(jsonObject.toString(4)); 

            Files.write(Paths.get(outputFile), jsonObject.toString(4).getBytes());

            System.out.println("\nJSON has been written to " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

