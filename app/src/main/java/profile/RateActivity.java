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

public class RateActivity extends AppCompatActivity {

    private static final String TEXT = "text";
    private String userID;

    private RatingBar ratingBar;
    private Button submit;
    private TextView name;

    public RateActivity(String userID) {
        this.userID = userID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = new User();
        user.setId(userID);
        ArrayList<User> users = new ArrayList<User>();
        users.add(user);
        //networkManager.getUser(users);
        user = users.get(0);

        // UI stuff
        ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        submit = (Button) findViewById(R.id.submit);
        name = (TextView) findViewById(R.id.userToRate);
        name.setText("name");


        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                float rating = ratingBar.getRating();
                //user.addRating(rating);

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (ratingBar != null) {
            // TODO
            // mListener.onFragmentInteraction(uri);
        }
    }


}
