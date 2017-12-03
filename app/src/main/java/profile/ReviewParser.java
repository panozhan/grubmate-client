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
import post.SinglePostFragment;

public class ReviewParser {
    private ReviewActivity reviewActivity;

    public boolean getReviewWithID(String userID, ReviewActivity reviewActivity) {
        try {
            this.reviewActivity = reviewActivity;
            GetReviewOfUser getReviewOfUser = new GetReviewOfUser(userID);
            getReviewOfUser.execute();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("get review parser failed");
            return false;
        }
    }

    public boolean addRewviewByID(String userID, String review) {
        try {
            AddReviewToUser addRatingToUser = new AddReviewToUser(userID, review);
            addRatingToUser.execute();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("add review parser failed");
            return false;
        }
    }

    private class AddReviewToUser extends AsyncTask<String, Void, Void> {
        private String userID;
        private String review;
        public AddReviewToUser(String userID, String review){
            this.userID = userID;
            this.review = review;
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/user?userid="+userID+"&review="+review);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                System.out.println("add review connect stream: "+convertStreamToString(is));

            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetReviewOfUser extends AsyncTask<String, Void, Void> {
        private String userID;
        private ArrayList<String> reviews;
        public GetReviewOfUser(String userID){
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

                // parsing
                String jsonString = convertStreamToString(is);
                JSONObject mainObject = new JSONObject(jsonString);

                JSONArray reviewsJArray = mainObject.getJSONArray("reviews");

                reviews = new ArrayList<>();
                if (reviewsJArray != null) {
                    for (int i=0; i<reviewsJArray.length(); i++) {
                        String r = reviewsJArray.get(i).toString();
                        if (r!=null && !"".equalsIgnoreCase(r) && !r.equalsIgnoreCase("null")) {
                            reviews.add(r);
                        }
                    }
                }

            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            reviewActivity.setReviews(reviews);
            super.onPostExecute(aVoid);
        }
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

