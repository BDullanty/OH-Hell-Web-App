import org.json.JSONObject;

import java.util.Base64;

public class JsonHandler {
    //Expected String Input:
    //{"action":"something","jwk":"something", "connectionID":"something"}

    public static Player getPlayerFromBody(String body){
        try {
            String[] subNameConnID= JsonHandler.getInfoFromBody(body);
            return Player.getPlayer(subNameConnID[0],subNameConnID[1],subNameConnID[2]);
        } catch (Exception e){
            System.out.println("Error when parsing token...");
            return null;
        }


    }
    private static String[] getInfoFromBody(String body) {
            // Parse the JSON string
            JSONObject jsonObject = new JSONObject(body);
            // Extract the value associated with the "jwk"
            String jwk = jsonObject.getString("jwk");
            String[] returnValues = decodeJWK(jwk);
            returnValues[2] = jsonObject.getString("connectionID");

            return returnValues;
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
        String[] returnValues = new String[3];
        // Extract the value associated with the "username" and "sub" key
        returnValues[0] = jsonObject.getString("sub");
        returnValues[1] = jsonObject.getString("username");
        return returnValues;
    }
}