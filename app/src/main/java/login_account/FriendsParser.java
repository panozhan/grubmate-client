package login_account;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import objects.Group;
import objects.Notification;
import objects.Post;
import objects.User;
import objects.UserSingleton;


public class FriendsParser {
    private UserSingleton owner;

    public boolean setFriendsForOwner() {
        try {
            owner = UserSingleton.getUserInstance();
            owner.setFriends();

            ParseUserFriends parseUserFriends = new ParseUserFriends(owner.get_id());
            parseUserFriends.execute();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("set friends map failed");
            return false;
        }
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private void parseFriends(InputStream is) throws IOException {
        String jsonString = convertStreamToString(is);
        try{
            JSONObject mainObject = new JSONObject(jsonString);
            System.out.println(mainObject);

            // sets friends ids and names
            JSONArray jsonFriendArray = mainObject.getJSONArray("friends");
            if (jsonFriendArray != null) {
                int len = jsonFriendArray.length();
                for (int i=0;i<len;i++){
                    // gets friends ids
                    String friendID = jsonFriendArray.get(i).toString();

                    // use id to get name
                    GetNameOfUser getNameOfUser = new GetNameOfUser(friendID);
                    getNameOfUser.execute();
                }
            } else {
                System.out.println("jsonFriendArray null");
            }

        }catch (JSONException e){
            e.printStackTrace();
            System.out.println("failed parsing friends under groups");
        }
    }

    private void parseName(InputStream is, String friendID) throws IOException {
        String jsonString = convertStreamToString(is);
        try{
            JSONObject mainObject = new JSONObject(jsonString);
            String friendName = mainObject.getString("name");

            // put friend in map
            owner.addFriend(friendID, friendName);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private class ParseUserFriends extends AsyncTask<String, Void, Void> {
        private String userid;
        public ParseUserFriends(String userid){
            this.userid = userid;
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/user?userid="+userid);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();

                parseFriends(is);

            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetNameOfUser extends AsyncTask<String, Void, Void> {
        private String userID;
        private String userName;
        public GetNameOfUser(String userID){
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
                parseName(is, userID);

            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        public String getUserName() {
            return userName;
        }
    }
}

