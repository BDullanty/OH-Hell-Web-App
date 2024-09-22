package HTTPHandlers;

import GameHandlers.User;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class PostAllUsersToLobby {
    
    public static void postAllUsersToLobby(){
        ArrayList<String> lobbyConnections = User.getLobbyConnections();
        String message = "{\"users\" : "+ User.getUsers()+"}";
        try {
            AWSSigner.sendSignedMessage(message,lobbyConnections);
            System.out.println("Sent "+message+" to lobby players");
        } catch (Exception e) {
            System.out.println("Failed to send message : "+e);

        }
    }
    
}
