package profile;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import objects.Group;
import objects.Notification;
import objects.Post;
import objects.User;
import objects.UserSingleton;
import post.SinglePostFragment;

public class RatingParser {
    private RateActivity rateActivity;
    private ProfileFragment profileFragment;
    private ProfileActivity profileActivity;
    private SinglePostFragment singlePostFragment;

    public void rearrangePostsByRatings(ArrayList<String> posterIDs) {
        try {
            this.rateActivity = null;
            this.profileFragment = null;
            this.profileActivity = null;
            this.singlePostFragment = null;
            GetAllRatings getAllRatings = new GetAllRatings(posterIDs);
            getAllRatings.execute();
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("get rating parser failed");
        }
    }

    private class GetAllRatings extends AsyncTask<String, Void, Void> {
        private ArrayList<String> posterIDs;
        private HashMap<String, Float> posterRatings;
        public GetAllRatings(ArrayList<String> posterIDs){
            this.posterIDs = posterIDs;
            this.posterRatings = new HashMap<>();
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                for (String userID: posterIDs) {
                    URL url = new URL("https://grubmateteam3.herokuapp.com/api/user?userid="+userID);

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream is = urlConnection.getInputStream();
                    String jsonString = convertStreamToString(is);

                    JSONObject mainObject = new JSONObject(jsonString);
                    float rating = Float.valueOf(mainObject.getString("rating"));

                    posterRatings.put(userID, rating);
                }

            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Map<String, Float> sortedRatings = sortByValue(posterRatings);
            if (sortedRatings!=null && sortedRatings.size()>0) {
                UserSingleton owner = UserSingleton.getUserInstance();
                ArrayList<Post> sortedPosts = new ArrayList<>();

                for (Map.Entry<String, Float> entry: sortedRatings.entrySet()) {
                    //System.out.println("ratingssorted: " + entry.getKey() + " " + entry.getValue());

                    // for every userid aka key, grab all the posts with same poster
                    for (Post p: owner.getPosts()) {
                        if (p.getUser().getId().equalsIgnoreCase(entry.getKey())) {
                            sortedPosts.add(p);
                        }
                    }
                }

                owner.getPosts().clear();
                for (Post p: sortedPosts) {
                    owner.getPosts().add(p);
                }
            }
            super.onPostExecute(aVoid);
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


    public boolean getRatingWithID(String userID, RateActivity rateActivity) {
        try {
            this.rateActivity = rateActivity;
            this.profileFragment = null;
            this.profileActivity = null;
            this.singlePostFragment = null;
            GetRatingOfUser parseUserFriends = new GetRatingOfUser(userID);
            parseUserFriends.execute();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("get rating parser failed");
            return false;
        }
    }

    public boolean getRatingWithID(String userID, ProfileFragment profileFragment) {
        try {
            this.profileFragment = profileFragment;
            this.rateActivity = null;
            this.profileActivity = null;
            this.singlePostFragment = null;
            GetRatingOfUser parseUserFriends = new GetRatingOfUser(userID);
            parseUserFriends.execute();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("get rating parser failed");
            return false;
        }
    }

    public boolean getRatingWithID(String userID, SinglePostFragment singlePostFragment) {
        try {
            this.profileFragment = null;
            this.rateActivity = null;
            this.profileActivity = null;
            this.singlePostFragment = singlePostFragment;
            GetRatingOfUser parseUserFriends = new GetRatingOfUser(userID);
            parseUserFriends.execute();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("get rating parser failed");
            return false;
        }
    }

    public boolean getRatingWithID(String userID, ProfileActivity profileActivity) {
        try {
            this.profileActivity = profileActivity;
            this.profileFragment = null;
            this.rateActivity = null;
            this.singlePostFragment = null;
            GetRatingOfUser parseUserFriends = new GetRatingOfUser(userID);
            parseUserFriends.execute();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("get rating parser failed");
            return false;
        }
    }

    public boolean addRatingByID(String userID, float rating) {
        try {
            AddRatingToUser addRatingToUser = new AddRatingToUser(userID, rating);
            addRatingToUser.execute();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("add rating parser failed");
            return false;
        }
    }

    private class AddRatingToUser extends AsyncTask<String, Void, Void> {
        private String userID;
        private float rating;
        public AddRatingToUser(String userID, float rating){
            this.userID = userID;
            this.rating = rating;
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/user?userid="+userID+"&rate="+rating);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                System.out.println("add rating connect stream: "+convertStreamToString(is));

            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetRatingOfUser extends AsyncTask<String, Void, Void> {
        private String userID;
        public GetRatingOfUser(String userID){
            this.userID = userID;
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/user?userid="+userID);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                parseRating(is, userID);

            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private void parseRating(InputStream is, String friendID) throws IOException {
        String jsonString = convertStreamToString(is);
        try{
            JSONObject mainObject = new JSONObject(jsonString);
            float rating = Float.valueOf(mainObject.getString("rating"));

            // sets curr rating in corresponding view
            if (rateActivity!= null && profileFragment == null && profileActivity == null && singlePostFragment == null) {
                rateActivity.setRating(rating);
            } else if (profileActivity!=null && rateActivity == null && profileFragment == null && singlePostFragment == null)  {
                profileActivity.setRating(rating);
            } else if (profileFragment!=null && rateActivity == null && profileActivity == null && singlePostFragment == null)  {
                profileFragment.setRating(rating);
            } else if (singlePostFragment!=null && rateActivity == null && profileActivity == null && profileFragment == null)  {
                //singlePostFragment.setRateOnDesc(rating);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

