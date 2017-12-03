package profile;

/**
 * Created by Alex on 12/3/2017.
 */

public class Transaction {
    private String type;
    private String timeRequested;
    private String timeConfirmed;
    private String timeEnded;
    private String person;
    private String postTitle;

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimeRequested() {
        return timeRequested;
    }

    public void setTimeRequested(String timeRequested) {
        this.timeRequested = timeRequested;
    }

    public String getTimeConfirmed() {
        return timeConfirmed;
    }

    public void setTimeConfirmed(String timeConfirmed) {
        this.timeConfirmed = timeConfirmed;
    }

    public String getTimeEnded() {
        return timeEnded;
    }

    public void setTimeEnded(String timeEnded) {
        this.timeEnded = timeEnded;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }
}
