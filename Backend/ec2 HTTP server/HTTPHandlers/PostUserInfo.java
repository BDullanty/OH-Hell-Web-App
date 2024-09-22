package HTTPHandlers;

import GameHandlers.GameHandler;
import GameHandlers.State;
import GameHandlers.User;
import org.json.JSONObject;

public class PostUserInfo {
    public static void postUserInfo(User user){
            try{

                JSONObject returnJson = new JSONObject();
                returnJson.put("returnType","userInfo");
                returnJson.put("username",user.getUsername());
                returnJson.put("state",user.state);
                returnJson.put("gameID", user.getGameID());
                String message = returnJson.toString();
                AWSSigner.sendSignedMessage(message,user.getConnections());
                System.out.println("Posted user info");
            } catch (Exception e){
                System.out.println("Failed to post user info "+e);
            }
    }
}
