package objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alex Pan on 10/17/2017.
 */

public class User implements Serializable {
    private String name;
    private String id;
    private ArrayList<String> posts;
    private ArrayList<Post> realPosts;
    private String profilepic;
    private float rating;
    private int countRating;

    public void addRating(float newRating){
        countRating++;
        rating = (rating+newRating)/countRating;
    }

    public ArrayList<Post> getRealPosts(){return realPosts;}

    public float getRating(){return rating;}

    public void setRating(float rating){this.rating = rating;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<String> posts) {
        this.posts = posts;
    }

}
