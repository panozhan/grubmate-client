package group;

import java.util.ArrayList;

/**
 * Created by mollyhe on 10/18/17.
 */

public class GroupModel {
    public String name, leaveTxt, editTxt;
    public int position;
    public ArrayList<String> friends;

    GroupModel(String name, ArrayList<String> f) {
        this.name = name;
        this.leaveTxt = "Leave";
        this.editTxt = "View/Edit";
        this.friends = f;
    }
}
