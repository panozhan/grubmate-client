package objects;

/**
 * Created by Alex Pan on 10/17/2017.
 */

public class Subscription {
    private String type;
    private String value;

    public Subscription() {
        this.type = "";
        this.value = "";
    }

    public Subscription(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "subtype: " + type + ", " + "value :" + value;
    }

}
