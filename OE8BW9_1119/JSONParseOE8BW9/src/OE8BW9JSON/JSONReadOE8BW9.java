package OE8BW9_1119.JSONParseOE8BW9.src.OE8BW9JSON;

import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONReadOE8BW9 {
    public static void main(String[] args) {

        try (FileReader reader = new FileReader("OE8BW9_orarend.json")) {
            JSONParser json = new JSONParser();
            JSONObject jsonObject = (JSONObject) json.parse(reader);

            JSONObject root = (JSONObject) jsonObject.get("UAN_orarend");
            JSONArray lessons = (JSONArray) root.get("ora");

            System.out.println("OE8BW9 Órarend 2025 ősz: \n");

            for (int i = 0; i < lessons.size(); i++) {
                JSONObject lesson = (JSONObject) lessons.get(i);
                JSONObject time = (JSONObject) lesson.get("idopont");

                System.out.println("Tárgy: " + lesson.get("targy"));
                System.out.println("Időpont: " + time.get("nap") + ", " + time.get("tol") + " - " + time.get("ig"));
                System.out.println("Helyszín: " + lesson.get("helyszin"));
                System.out.println("Oktató: " + lesson.get("oktato"));
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
