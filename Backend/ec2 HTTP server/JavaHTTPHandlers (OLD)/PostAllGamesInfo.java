package HTTPHandlers;

import GameHandlers.GameHandler;
import GameHandlers.User;

import java.util.ArrayList;

public class PostAllGamesInfo {
    public static void postAllGamesToLobby() {
        try {
            ArrayList<String> lobbyConnections = User.getLobbyConnections();
            String message = "{\"returnType\": \"gameList\", \"games\" : " + GameHandler.getLobbyGamesJson() + "}";

            AWSSigner.sendSignedMessage(message, lobbyConnections);
            String names = "";
            for(int i = 0; i < lobbyConnections.size();i++){
                names+= User.getUser(lobbyConnections.get(i)).getUsername()+" ";
            }
            System.out.println("Sent " + message + " to lobby players named: "+names);
        } catch (Exception e) {
            System.out.println("Failed to postAllGamesToLobby : " + e);

        }


    }

    public static void postAllGamesToUser(User user) {
        try {
            ArrayList<String> lobbyConnections = user.getConnections();
            String message = "{\"returnType\": \"gameList\", \"games\" : " + GameHandler.getLobbyGamesJson() + "}";

            AWSSigner.sendSignedMessage(message, lobbyConnections);
            System.out.println("Sent " + message + " to lobby players");
        } catch (Exception e) {
            System.out.println("Failed to postAllGamesToLobby : " + e);

        }

    }
}
