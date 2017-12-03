package objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alex Pan on 10/17/2017.
 */

public class Post implements Serializable {
    private String _id;
    private String location;
    private String title;
    private User user;
    private ArrayList<String> images;
    private Boolean status;
    private String category;
    private String tag;
    private String kind = "Restaurant";
    private String groupName = "everyone";
    private String groupID;
    private boolean editable;

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setGroupByID(String groupID) {
        if (groupID.length()<1) {
            return;
        }

        this.groupID = groupID;
        for (Group g: UserSingleton.getUserInstance().getGroups()){
            if (g.getId().equalsIgnoreCase(groupID)) {
                groupName = g.getName();
            }
        }
    }

    public String getGroupName(){
        return groupName;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        if (kind!=null) this.kind = kind;
    }

    public int getAvailable(){return numAvailable;}
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;
    private int numAvailable;
    private ArrayList<String> pending = new ArrayList<>();
    private ArrayList<String> confirmed = new ArrayList<>();
    private ArrayList<String> groups = new ArrayList<>();
    private boolean readyToShow;
    private String description;
    private String price;


    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    private String timestart;
    private String timeend;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public boolean isReadyToShow() {
        return readyToShow;
    }

    public void setReadyToShow(boolean readyToShow) {
        this.readyToShow = readyToShow;
    }

    public int getNumAvailable() {
        return numAvailable;
    }

    public void setNumAvailable(int numAvailable) {
        this.numAvailable = numAvailable;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setLocation(String location) {
        System.out.println("my locaiton"); this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(User userSingleton) {
        this.user = userSingleton;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public void setPending(ArrayList<String> pending) {
        this.pending = pending;
    }

    public void setConfirmed(ArrayList<String> confirmed) {
        this.confirmed = confirmed;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    public String get_id() {
        return _id;
    }

    public String getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getCategory() {
        return category;
    }

    public String getTag() {
        return tag;
    }

    public ArrayList<String> getPending() {
        return pending;
    }

    public ArrayList<String> getConfirmed() {
        return confirmed;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

}
