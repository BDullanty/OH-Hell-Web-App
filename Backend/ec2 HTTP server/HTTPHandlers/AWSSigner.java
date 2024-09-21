//Code refractored from: https://github.com/aws-samples/sigv4-signing-examples/blob/main/no-sdk/java/AWSSigner.java

package HTTPHandlers;

import Secret.AWSACCESSKEY;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AWSSigner {
    private static final String AWS_ACCESS_KEY_ID = AWSACCESSKEY.AWS_ACCESS_KEY_ID;
    private static final String AWS_SECRET_ACCESS_KEY = AWSACCESSKEY.AWS_SECRET_ACCESS_KEY;
    private static final String RESTAPIHOST = AWSACCESSKEY.RESTAPIHOST;
    private static final String RESTAPIPATH = "/production/@connections/";

    private static final String METHOD = "POST";
    private static final String SERVICE = "execute-api";
    private static final String REGION = "us-west-1";
    private static final String ALGORITHM = "AWS4-HMAC-SHA256";

    public static void sendSignedMessage(String requestBody, ArrayList<String> connections) throws IOException, NoSuchAlgorithmException {
        // Create a datetime object for signing
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String amzDate = dateFormat.format(new Date());
        String dateStamp = amzDate.substring(0, 8);

        // Create the canonical request
        String canonicalQuerystring = "";
        String canonicalHeaders = "host:" + RESTAPIHOST + "\n"+"content-type:application/json\n";
        String signedHeaders = "host";
        String payloadHash = sha256Hex(requestBody);

        // Create the string to sign
        String credentialScope = dateStamp + "/" + REGION + "/" + SERVICE + "/" + "aws4_request";


        // Add signing information to the request
        for(int i = 0; i < connections.size(); i++) {

            String canonicalUri = RESTAPIPATH+connections.get(i);
            String canonicalRequest = METHOD + "\n" + canonicalUri + "\n" + canonicalQuerystring + "\n" + canonicalHeaders + "\n" + signedHeaders + "\n" + payloadHash;
            String hashedCanonicalRequest = sha256Hex(canonicalRequest);
            String stringToSign = ALGORITHM + "\n" + amzDate + "\n" + credentialScope + "\n" + hashedCanonicalRequest;

            // Sign the string
            byte[] signingKey = getSignatureKey(AWS_SECRET_ACCESS_KEY, dateStamp, REGION, SERVICE);
            String signature = hmacSha256Hex(signingKey, stringToSign);
            String authorizationHeader = ALGORITHM + " " + "Credential=" + AWS_ACCESS_KEY_ID + "/" + credentialScope + ", " + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature;

            // Make the header
            URL url = new URL("https://" + RESTAPIHOST + canonicalUri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(METHOD);
            con.setRequestProperty("Host", RESTAPIHOST);
            con.setRequestProperty("x-amz-date", amzDate);
            con.setRequestProperty("Content-Type", "application/json");
            // con.setRequestProperty("x-amz-security-token", AWS_SESSION_TOKEN);
            con.setRequestProperty("Authorization", authorizationHeader);
            //Make the body
            con.setDoOutput(true);

            // Write the request body
            try (var os = con.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            //Make request and Print the response
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String responseBody = new String(con.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                System.out.println(responseBody);
            } else {
                System.out.println("Error: " + responseCode + " " + con.getResponseMessage());
            }
        }
    }

    private static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws NoSuchAlgorithmException {
        byte[] kSecret = ("AWS4" + key).getBytes(StandardCharsets.UTF_8);
        byte[] kDate = hmacSha256(kSecret, dateStamp);
        byte[] kRegion = hmacSha256(kDate, regionName);
        byte[] kService = hmacSha256(kRegion, serviceName);
        return hmacSha256(kService, "aws4_request");
    }

    private static String hmacSha256Hex(byte[] key, String data) throws NoSuchAlgorithmException {
        return bytesToHex(hmacSha256(key, data));
    }

    private static byte[] hmacSha256(byte[] key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: HmacSHA256 algorithm not available", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Error: Invalid key for HmacSHA256", e);
        }
    }

    private static String sha256Hex(String data) throws NoSuchAlgorithmException {
        return bytesToHex(MessageDigest.getInstance("SHA-256").digest(data.getBytes(StandardCharsets.UTF_8)));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}