public class Player {
    String sub;
    String name;
    public Player(String sub){
        this.name = resolveNameFromSub(sub);
        this.sub=sub;
    }

    private String resolveNameFromSub(String sub) {
        return "Name from backend";
    }
    public static Player getPlayer(String sub){
        //if they are online, send the existing object
        Player alreadyOnline =returnPlayerIfOnline(sub);
        if(alreadyOnline !=null) return alreadyOnline;
        //otherwise create
        Player newPlayer = new Player(sub);
        return newPlayer;
    }

    private static Player returnPlayerIfOnline(String sub) {
        //check if sub is in player list;
        return new Player(sub);
    }

}
