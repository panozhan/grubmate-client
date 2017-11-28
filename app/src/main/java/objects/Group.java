package objects;

import java.util.ArrayList;

/**
 * Created by Alex Pan on 10/17/2017.
 */

public class Group {
    private String name;
    private ArrayList<String> users;
    private String id;

    public Group(){

    }
    public Group(String id, String name, ArrayList<String> users){
        this.id = id;
        this.name = name;
        this.users = users;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public void addUsers(ArrayList<String> users) {
        for (String user: users) {
            this.users.add(user);
        }
    }

    public void removeUsers(ArrayList<String> selected) {
        for (String userID: selected) {
            int userIndex = findUserIndex(userID);
            if (userIndex>=0 && userIndex<this.users.size()) {
                this.users.remove(userIndex);
            }
        }
    }

    private int findUserIndex(String userID) {
        for (int i=0; i<this.users.size(); i++) {
            if (this.users.get(i).equalsIgnoreCase(userID)) {
                return i;
            }
        }
        return -1;
    }
}
