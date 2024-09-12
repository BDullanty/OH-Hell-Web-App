import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    //Static Values
    //TODO:Change the new Arraylist to load methods that load from Database.
    private static HashMap<String,Player> playerList = new HashMap();
    private static ArrayList<Player> onlineList = new ArrayList<>();

    //Per Player Values
    private String sub;
    private String username;


    public String getSub() {
        return sub;
    }

    public String getUsername() {
        return username;
    }

    public Player(String sub,String username){
        this.username = username;
        this.sub=sub;
    }

    //STATIC FUNCTIONS

    //Returns a player object unless one is already made
    public static Player getPlayer(String sub,String username){
        //if the player object exists, send it.
        System.out.println("Finding player with username "+username);
        if(Player.playerList.containsKey(sub)){
        return playerList.get(sub);
        }
        //If not, we create the object
        Player newPlayer = new Player(sub,username);
        playerList.put(newPlayer.getSub(),newPlayer);
        return newPlayer;
    }
    public static void addPlayerOnline(Player p){
        if(p==null) throw new IllegalArgumentException("Player p was null");
     Player.onlineList.add(p);
    }
    public static void removeOnlinePlayer(Player p){
        Player.onlineList.remove(p);
    }

}
