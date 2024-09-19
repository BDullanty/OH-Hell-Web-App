package HTTPHandlers;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

public class ExchangeHandler {

    public static JSONObject getInfoJsonFromExchange(HttpExchange exchange) throws IOException {

        String body = new String(exchange.getRequestBody().readAllBytes());
        // Parse the JSON string
        JSONObject jsonObject = new JSONObject(body);
        ArrayList<String> returnValues = new ArrayList<>();
        // Extract the value associated with the "jwk"
        String jwk = jsonObject.getString("jwk");
        String[] subAndName = decodeJWK(jwk);

        returnValues.add(subAndName[0]);
        returnValues.add(subAndName[1]);

        jsonObject.remove(jwk);
        jsonObject.put("sub",subAndName[0]);
        jsonObject.put("name",subAndName[1]);

        return jsonObject;
    }

    private static String[] decodeJWK(String jwk) {
        String[] splitJWK = jwk.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(splitJWK[1]));
        //Get values from payload
        String[] subAndName =getValuesFromDecodedJWK(payload);
        return subAndName;
    }

    private static String[] getValuesFromDecodedJWK(String payload) {
        JSONObject jsonObject = new JSONObject(payload);
        String[] returnValues = new String[3];
        // Extract the value associated with the "username" and "sub" key
        returnValues[0] = jsonObject.getString("sub");
        returnValues[1] = jsonObject.getString("username");
        return returnValues;
    }
}

