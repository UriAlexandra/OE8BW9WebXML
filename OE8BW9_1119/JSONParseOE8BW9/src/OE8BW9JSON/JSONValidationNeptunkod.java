package OE8BW9_1119.JSONParseOE8BW9.src.OE8BW9JSON;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.InputStream;


public class JSONValidationNeptunkod {
    public static void main(String[] args) {
        try {
            // JSON betöltése
            InputStream jsonInput = new FileInputStream("orarendOE8BW9.json");
            JSONObject jsonObject = new JSONObject(new JSONTokener(jsonInput));

            // JSON Schema betöltése
            InputStream schemaInput = new FileInputStream("orarendOE8BW9Schema.json");
            JSONObject jsonSchema = new JSONObject(new JSONTokener(schemaInput));

            // Schema létrehozása
            Schema schema = SchemaLoader.load(jsonSchema);

            // Validáció
            schema.validate(jsonObject);

            System.out.println("Validation successful!");
        } catch (Exception e) {
            System.out.println("Validation failed: " + e.getMessage());
        }
    }
}

}
