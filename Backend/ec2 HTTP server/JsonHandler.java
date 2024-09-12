import org.json.JSONObject;

import java.util.Base64;

public class JsonHandler {
    //Expected String Input:
    //{"action":"something","sub":"something", ....}

    public static Player getPlayerFromBody(String body){
        String[] subAndName = JsonHandler.getInfoFromBody(body);
        return Player.getPlayer(subAndName[0],subAndName[1]);

    }
    private static String[] getInfoFromBody(String body) {

        try {
            // Parse the JSON string
            JSONObject jsonObject = new JSONObject(body);

            // Extract the value associated with the "token"
            String token = jsonObject.getString("token");

            System.out.println("Token: " + token);  // Output: something

            return decodeJWK(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("Passed in string was not converted properly");
        }
    }

    private static String[] decodeJWK(String jwk) {
        String[] parts = jwk.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(parts[1]));
        //Get values from payload
        return getValuesFromDecodedJWK(payload);
    }

    private static String[] getValuesFromDecodedJWK(String payload) {
        JSONObject jsonObject = new JSONObject(payload);
        String[] returnValues = new String[2];
        // Extract the value associated with the "username" and "sub" key
        returnValues[0] = jsonObject.getString("sub");
        returnValues[1] = jsonObject.getString("username");
        return returnValues;
    }
}