package GameHandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class User extends Player{
    //Per GameHandler.Player Values

    //Static Values
    //TODO:Change the new Arraylist to load methods that load from Database.
    protected static HashMap<String,User> connectionList = new HashMap<>();
    protected  static Stack<User> onlineList = new Stack<>();
    protected String sub;
    public Enum state;
    protected static HashMap<String,User> userList = new HashMap<>();
    protected String connectionID;

    protected ArrayList<Card> hand;
    protected int bet;


    public User(String sub,String username, String connectionID){

        super(username);
        this.sub=sub;
        this.connectionID = connectionID;
        User.connectionList.put(connectionID,this);
        this.state = State.LOBBY;
    }

    public static User getUser(String connectionID) {
        return User.connectionList.get(connectionID);
    }

    private User setConnection(String connectionID) {

        System.out.println("ConnectionID changed from " + this.connectionID+" to "+connectionID);
        User.connectionList.put(connectionID,this);
        this.connectionID = connectionID;
        return this;
    }
    public String getSub() {
        return sub;
    }

    public String getUsername() {
        return username;
    }



    //STATIC FUNCTIONS

    //Returns a player object unless one is already made
    public static User getUser(String sub, String username, String connectionID){
        //if the player object exists, send it.
        System.out.println("Finding player with username "+username);
        if(User.userList.containsKey(sub)){
            return User.userList.get(sub).setConnection(connectionID);
        }
        //If not, we create the object
        User newPlayer = new User(sub,username,connectionID);
        userList.put(newPlayer.getSub(),newPlayer);
        return newPlayer;
    }


    public static void addUserOnline(User p){
        if(p==null) throw new IllegalArgumentException("Player was null");
        if(onlineList.contains(p)) System.out.println("Player was already online? This is a bug.");
        p.state= State.LOBBY;
        User.onlineList.add(p);
    }
    public static User removeOnlineUser(String connectionID){
        User offlineUser = User.connectionList.get(connectionID);
        System.out.println("Player " +offlineUser.getUsername()+ " has gone offline.");
        offlineUser.state= State.FINISHED;
        User.onlineList.remove(offlineUser);
        return offlineUser;
    }
    public static String getUsers(){
        String returnString = "";
        for(int i =0; i < onlineList.size();i++){
            returnString+=onlineList.elementAt(i);
        }
        return returnString;
    }

}
