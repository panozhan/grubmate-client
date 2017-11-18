package objects;

/**
 * Created by Alex Pan on 10/17/2017.
 */

public class Notification {
    private String type;
    private String title;
    private String address;
    private String status;
    private String postId;
    private String personid;

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostId(String id){
        this.postId = id;
    }

    public String getPostId() {
        return postId;
    }
}
