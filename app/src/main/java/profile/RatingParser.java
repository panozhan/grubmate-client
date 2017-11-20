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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import objects.Group;
import objects.Notification;
import objects.Post;
import objects.User;
import objects.UserSingleton;

public class RatingParser {
    private RateActivity rateActivity;

    public boolean getRatingWithID(String userID, RateActivity rateActivity) {
        try {
            this.rateActivity = rateActivity;
            GetRatingOfUser parseUserFriends = new GetRatingOfUser(userID);
            parseUserFriends.execute();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("get rating parser failed");
            return false;
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

            // sets curr rating in activity
            rateActivity.setRating(rating);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

