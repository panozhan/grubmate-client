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
}
