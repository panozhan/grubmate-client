package profile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.udacity.test.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import objects.Parser;
import objects.Post;
import objects.UserSingleton;

public class ReviewActivity extends AppCompatActivity {
    private ListView reviewList;
    private MyAdapterReview adapter;
    private String userID;
    private UserSingleton owner;
    private Button backButton;
    private ArrayList<String> randomReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // review
        setContentView(R.layout.activity_review);
        owner = UserSingleton.getUserInstance();

        // get userid from intent
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userid");
        }
        // get name
        String username = owner.getFriendNameByID(userID);
        // set title
        setTitle(username + "'s Reviews");

        // UI stuff
        backButton = (Button) findViewById(R.id.reviewBack);
        reviewList = (ListView) findViewById(R.id.reviewList);

        // listeners
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go back
                finish();
            }
        });

        // populate reviews
        ArrayList<String> reviews = new ArrayList<String>();
        if (userID.equalsIgnoreCase("2")) {
            // lauren
            reviews.add("super sweet");
            reviews.add("nice person");
        } else if (userID.equalsIgnoreCase("907460246074182")) {
            // katherine
            reviews.add("good food");
            reviews.add("5 stars");
        } else if (userID.equalsIgnoreCase("1583333221705311")) {
            // alex
            reviews.add("food is ok");
            reviews.add("meh");
        } else if (userID.equalsIgnoreCase("1687165621302390")) {
            // arfan
            reviews.add("ok food");
            reviews.add("cool person");
        } else if (userID.equalsIgnoreCase("1032002353603911")) {
            // molly
            reviews.add("food looks great");
            reviews.add("nice person");
        }


        // add a review after rating
        if (owner.hasRated() && userID.equalsIgnoreCase(owner.get_id())) {
            if (userID.equalsIgnoreCase("2")) {
                // lauren
                reviews.add("great");
            } else if (userID.equalsIgnoreCase("907460246074182")) {
                // katherine
                reviews.add("great");
            } else if (userID.equalsIgnoreCase("1583333221705311")) {
                // alex
                reviews.add("cool");
            } else if (userID.equalsIgnoreCase("1687165621302390")) {
                // arfan
                reviews.add("nice");
            } else if (userID.equalsIgnoreCase("1032002353603911")) {
                // molly
                reviews.add("wonderful");
            }

            owner.setHasRated(false);
        }

        adapter = new MyAdapterReview(this, reviews);
        reviewList.setAdapter(adapter);
    }

    private class MyAdapterReview extends ArrayAdapter<String> {
        private ArrayList<String> reviews;
        private ReviewActivity ra;
        public MyAdapterReview(ReviewActivity ra, ArrayList<String> reviews){
            super(ra, 0, reviews);
            this.reviews = reviews;
            this.ra = ra;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = ra.getLayoutInflater().inflate(R.layout.single_review, null);
            }

            String review = String.valueOf(position+1) + ") " + reviews.get(position);
            ((TextView)convertView.findViewById(R.id.singleReviewText)).setText(review);

            return convertView;
        }
    }
}
