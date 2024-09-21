package GameHandlers;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class User extends Player{
    //Per GameHandler.Player Values

    //Static Values
    //TODO:Change the new Arraylist to load methods that load from Database.
    //Expects the string to be conectionID
    protected static HashMap<String,User> connectionList = new HashMap<>();
    protected static Stack<User> onlineList = new Stack<>();
    //Userlist is the entire list of users offline or online, String is sub
    protected static HashMap<String,User> userList = new HashMap<>();
    protected String sub;
    public Enum state;
    protected ArrayList<String> connectionID;

    protected ArrayList<Card> hand;
    protected int bet;


    public User(String sub,String username, String connectionID){
        super(username);
        this.connectionID = new ArrayList<>();
        this.sub=sub;
        this.connectionID.add(connectionID);
        User.connectionList.put(connectionID,this);
        this.state = State.LOBBY;
    }

    public static User getUser(String connectionID) {
        return User.connectionList.get(connectionID);
    }

    private User addConnection(String connectionID) {
        User.connectionList.put(connectionID,this);
        this.connectionID.add(connectionID);
        System.out.println("ConnectionID added from " + this.connectionID+" to "+connectionID);
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
            return User.userList.get(sub).addConnection(connectionID);
        }
        //If not, we create the object
        User newPlayer = new User(sub,username,connectionID);
        userList.put(newPlayer.getSub(),newPlayer);
        return newPlayer;
    }


    public static void addUserOnline(User p){
        if(p==null) throw new IllegalArgumentException("Player was null");
        if(onlineList.contains(p)) System.out.println("Player was already online.");
        else{
            p.state= State.LOBBY;
            User.onlineList.add(p);
        }

    }
    public static User removeConnection(String connectionID){
        User user = User.connectionList.get(connectionID);
        user.connectionID.remove(connectionID);
        if(user.connectionID.size()==0){
            user.state= State.OFFLINE;
            User.onlineList.remove(user);
            System.out.println("Player " +user.getUsername()+ " has gone offline.");

        }
        else{

            System.out.println("Player " +user.getUsername()+ " now has "+(user.connectionID.size()-1) + "live connections");
        }
        connectionList.remove(connectionID);
        return user;
    }
    public static String getUsers(){
        JSONObject users = new JSONObject();
        System.out.println("Checking for players...");
        for(int i =0; i < onlineList.size();i++){
            User u = onlineList.elementAt(i);
            System.out.println("Found player "+u.getUsername());
            users.put(u.getUsername(),u.getState().toString());
        }
       return users.toString();
    }

    private Enum getState() {
        return this.state;
    }

}
