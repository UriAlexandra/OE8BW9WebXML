package OE8BW9JSON;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONWriteNeptunkod {
    public static void main(String[] args) {
        String inputFile = "orarendOE8BW9.json";
        String outputFile = "orarendOE8BW9.json";

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

