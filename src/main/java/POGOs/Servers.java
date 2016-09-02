package POGOs;

import Objects.BlackListedPhrase;
import Objects.ServerObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Vaerys on 14/08/2016.
 */
public class Servers {
    public boolean properlyInit = false;
    ArrayList<BlackListedPhrase> BlackList = new ArrayList<>();
    ArrayList<ServerObject> servers = new ArrayList<>();

    public String addServer(String userID, String serverName, String serverIP, String serverPort){
        Random random = new Random();
        if (servers.size() > 0){
            for (ServerObject s : servers){
                if (s.getName().equalsIgnoreCase(serverName)){
                    return "> A Server with that name already Exists.";
                }
                // TODO: 02/09/2016 add Mention blacklist.
            }
        }

        return "Server added";
    }
}
