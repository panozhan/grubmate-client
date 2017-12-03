package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UserSingleton {
    private static UserSingleton userSingleton;
    private String name;
    private String _id;
    private float rating;
    private ArrayList<String> postIds = new ArrayList<String>();
    private ArrayList<Post> posts = new ArrayList<Post>();
    private ArrayList<String> requests = new ArrayList<String>();
    private ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();
    private ArrayList<Group> groups = new ArrayList<Group>();
    private ArrayList<Post> postObjectsOfUser = new ArrayList<Post>();
    private ArrayList<String> postOfUser = new ArrayList<String>();
    private ArrayList<Notification> requestsNotifications = new ArrayList<Notification>();
    private ArrayList<Notification> statusNotifications = new ArrayList<Notification>();
    private ArrayList<Notification> newsNotifications = new ArrayList<Notification>();
    private ArrayList<String> notificationIds = new ArrayList<String>();
    private HashMap<String, String> friendsMap; // <name, id>
    private HashMap<String, String> blockedMap; //<name, id>



    public ArrayList<String> getNotificationIds() {
        return notificationIds;
    }

    public void setNotificationIds(ArrayList<String> notificationIds) {
        this.notificationIds = notificationIds;
    }

    private UserSingleton(){

    }

    public void setAllNotifications(ArrayList<Notification> notifs){
        for(Notification n : notifs){
            switch (n.getType()){
                case "request":
                    requestsNotifications.add(n);
                    break;
                case "status":
                    statusNotifications.add(n);
                    break;
                case "news":
                    newsNotifications.add(n);
                    break;
            }
        }
    }

    public ArrayList<Notification> getRequestsNotifications() {
        return requestsNotifications;
    }

    public void setRequestsNotifications(ArrayList<Notification> requestsNotifications) {
        this.requestsNotifications = requestsNotifications;
    }

    public ArrayList<Notification> getStatusNotifications() {
        return statusNotifications;
    }

    public void setStatusNotifications(ArrayList<Notification> statusNotifications) {
        this.statusNotifications = statusNotifications;
    }

    public ArrayList<Notification> getNewsNotifications() {
        return newsNotifications;
    }

    public void setNewsNotifications(ArrayList<Notification> newsNotifications) {
        this.newsNotifications = newsNotifications;
    }

    public static UserSingleton getUserInstance(){
        if(userSingleton == null){
            userSingleton = new UserSingleton();
        }
        return userSingleton;
    }

    public ArrayList<Post> getPostObjectsOfUser() {
        return postObjectsOfUser;
    }

    public void setPostObjectsOfUser(ArrayList<Post> postObjectsOfUser) {
        this.postObjectsOfUser.clear();
        this.postObjectsOfUser = postObjectsOfUser;
    }
    public int getNumOwnPosts(){
        return this.postObjectsOfUser.size();
    }
    public void addPost(Post post) {
        if (post!=null) {
            this.postObjectsOfUser.add(post);
        }
    }

    public ArrayList<String> getPostIds() {
        return postIds;
    }

    public void setPostIds(ArrayList<String> postIds) {
        this.postIds = postIds;
    }

    public ArrayList<String> getPostOfUser() {
        return postOfUser;
    }

    public void setPostOfUser(ArrayList<String> postOfUser) {
        this.postOfUser = postOfUser;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public void setRequests(ArrayList<String> requests) {
        this.requests = requests;
    }

    public void setSubscriptions(ArrayList<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public String getName() {
        return name;
    }

    public String get_id() {
        return _id;
    }

    public float getRating() {
        return rating;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

    public Post getPostByID(String id) {
        for (int i = 0; i < this.posts.size(); i++) {
            if (posts.get(i).get_id().equals(id)) {
                return posts.get(i);
            }
        }
        return null;
    }

    /********* subscriptions *********/
    public ArrayList<Subscription> getSubscriptions() {
        return subscriptions;
    }
    public void addSubscription(Subscription sub) { subscriptions.add(sub); }
    public void removeSubscription(int index) { subscriptions.remove(index); }
    public int getNumSubscriptions() { return subscriptions.size(); }


    /********* groups *********/
    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }
    public ArrayList<Group> getGroups() {
        return groups;
    }
    public void addGroup(Group group) {
        groups.add(group);
    }
    public void removeGroup(int index) {
        groups.remove(index);
    }
    public void clearGroups() {
        groups.clear();
    }
    public int getNumGroups() {
        return groups.size();
    }
    public Group findGroupByName(String groupName) {
        for (Group g: this.groups) {
            if (g.getName().equalsIgnoreCase(groupName)) {
                return g;
            }
        }
        return null;
    }
    public boolean isFriendInGroup(String friendID, Group group) {
        for (String userID: group.getUsers()) {
            if (userID.equalsIgnoreCase(friendID)) {
                return true;
            }
        }
        return false;
    }
    public void addUsersToGroup(String groupID, ArrayList<String> selected) {
        int groupIndex = 0;
        for (int i = 0; i<this.groups.size(); i++) {
            if (this.groups.get(i).getId().equalsIgnoreCase(groupID)) {
                groupIndex = i;
                break;
            }
        }

        this.groups.get(groupIndex).addUsers(selected);
    }
    public void removeUsersFromGroup(String groupID, ArrayList<String> selected) {
        int groupIndex = 0;
        for (int i = 0; i<this.groups.size(); i++) {
            if (this.groups.get(i).getId().equalsIgnoreCase(groupID)) {
                groupIndex = i;
                break;
            }
        }

        this.groups.get(groupIndex).removeUsers(selected);
    }


    /********* friends *********/
    public HashMap<String, String> getFriends() {
        return friendsMap;
    }
    public void setFriends() {
        this.friendsMap = new HashMap<String, String>();
    }
    public void addFriend(String friendID, String friendName) {
        friendsMap.put(friendID, friendName);
    }
    public String getFriendNameByID(String id){
        String fname = friendsMap.get(id);
        if (fname!=null) {
            return fname;
        }
        return this.name;
    }
    public String getFriendIDByName(String name){
        for (Map.Entry<String, String> entry: friendsMap.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(name)) {
                return entry.getKey();
            }
        }
        return null;
    }

    //~~ Blocked Users ~~
    public HashMap<String, String> getBlocked() { return blockedMap; }
    public void setBlocked() {this.blockedMap = new HashMap<String, String>(); }
    public void addBlocked(String blockID, String blockName) { blockedMap.put(blockID, blockName);}
    public String getBlockedNameByID(String id) {
        String bname = blockedMap.get(id);
        if (bname!=null) {
            return bname;
        }
        return this.name;
    }
    public String getBlockedIDByName(String name) {
        for (Map.Entry<String, String> entry: blockedMap.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(name)) {
                return entry.getKey();
            }
        }
        return null;
    }

    // for reviews
    private boolean hasRated = false;
    public void setHasRated(boolean hasRated) {
        this.hasRated = hasRated;
    }

    public boolean hasRated() {
        return hasRated;
    }
}
