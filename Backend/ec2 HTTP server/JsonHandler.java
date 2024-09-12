import org.json.JSONObject;
public class JsonHandler {
    //Expected String Input:
    //{"action":"something","sub":"something", ....}
    public static String getJWKFromBody(String body){

        try {
            // Parse the JSON string
            JSONObject jsonObject = new JSONObject(body);

            // Extract the value associated with the "sub" key
            String token= jsonObject.getString("token");

            System.out.println("Token: "+token);  // Output: something
            return token;
        }
        catch (Exception e){
            throw new IllegalArgumentException("Passed in string was not converted properly");
        }
    }
    public static String getNameFromJWK(String jwk) {
        String username = "";
        return username;
    }
}
