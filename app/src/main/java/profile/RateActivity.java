package profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.udacity.test.R;

import java.util.ArrayList;

import objects.User;
import objects.UserSingleton;

public class RateActivity extends AppCompatActivity {
    private String userID;

    private RatingBar ratingBar;
    private Button submit;
    private TextView name;
    private RateActivity self;

    private RatingParser ratingParser;
    private TextView currRating;
    private EditText reviewText;

    public RateActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rate);
        setTitle("Please rate this user");

        this.self = this;

        // get userid from intent
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userid");
        }

        // pulls and sets rating on UI
        ratingParser = new RatingParser();
        ratingParser.getRatingWithID(userID, this);

        // finds name of this user
        final UserSingleton owner = UserSingleton.getUserInstance();
        String username = owner.getFriendNameByID(userID);

        // UI stuff
        ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        submit = (Button) findViewById(R.id.submit);
        name = (TextView) findViewById(R.id.userToRate);
        name.setText(username);
        currRating = (TextView) findViewById(R.id.currRating);
        reviewText = (EditText) findViewById(R.id.reviewText);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                float newRating = ratingBar.getRating();
                System.out.println("rated " + newRating + " stars");

                // send new rating to database
                ratingParser.addRatingByID(userID, newRating);

                // add in review
                ReviewParser reviewParser = new ReviewParser();
                reviewParser.addRewviewByID(userID, reviewText.getText().toString());

                finish();
            }
        });
    }

    public void setRating(float rating) {
        currRating.setText(String.format("%.2f", rating));
    }
}
