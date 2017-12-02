package login_account;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import objects.UserSingleton;


public class BlockedParser {
    private UserSingleton owner;

    public boolean setFriendsForOwner() {
        try {
            owner = UserSingleton.getUserInstance();
            owner.setBlocked();

            ParseUserBlocked parseUserBlocked = new ParseUserBlocked(owner.get_id());
            parseUserBlocked.execute();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("set blocked map failed");
            return false;
        }
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private void parseBlocked(InputStream is) throws IOException {
        String jsonString = convertStreamToString(is);

        try{
            JSONObject mainObject = new JSONObject(jsonString);
            System.out.println(mainObject);

            // sets friends ids and names
            JSONArray jsonBlockedArray = mainObject.getJSONArray("blocked");
            if (jsonBlockedArray != null) {
                int len = jsonBlockedArray.length();
                for (int i=0;i<len;i++){
                    // gets blocked ids
                    String blockedID = jsonBlockedArray.get(i).toString();

                    // use id to get name
                    GetNameOfUser getNameOfUser = new GetNameOfUser(blockedID);
                    getNameOfUser.execute();
                }
            } else {
                System.out.println("jsonBlockedArray null");
            }

        }catch (JSONException e){
            e.printStackTrace();
            System.out.println("failed parsing Blocked under groups");
        }
    }

    private void parseName(InputStream is, String blockedID) throws IOException {
        String jsonString = convertStreamToString(is);
        try{
            JSONObject mainObject = new JSONObject(jsonString);
            String blockedName = mainObject.getString("name");

            // put blocked in map
            owner.addBlocked(blockedID, blockedName);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private class ParseUserBlocked extends AsyncTask<String, Void, Void> {
        private String userid;
        public ParseUserBlocked(String userid){
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

                parseBlocked(is);

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

