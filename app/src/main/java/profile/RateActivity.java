package profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private RatingParser ratingParser;

    public RateActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rate);
        setTitle("Please rate this user");

        Bundle b = getIntent().getExtras();
        if(b != null)
            userID = b.getString("userid");

        ratingParser = new RatingParser();
        ratingParser.getRatingWithID(userID, this);

        UserSingleton owner = UserSingleton.getUserInstance();
        String username = owner.getFriendNameByID(userID);

        // UI stuff
        ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        submit = (Button) findViewById(R.id.submit);
        name = (TextView) findViewById(R.id.userToRate);
        name.setText(username);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                float rating = ratingBar.getRating();
                System.out.println("rated " + rating + " stars");
                //user.addRating(rating);

            }
        });
    }

    public void setRating(float rating) {
        TextView currRating = (TextView) findViewById(R.id.currRating);
        currRating.setText(String.format("%.2f", rating));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (ratingBar != null) {
            // TODO
            // mListener.onFragmentInteraction(uri);
        }
    }


}
