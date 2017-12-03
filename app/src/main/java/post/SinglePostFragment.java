package post;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udacity.test.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import objects.NetworkManager;
import objects.Parser;
import objects.Post;
import objects.User;
import objects.UserSingleton;
import profile.ProfileActivity;
import profile.RatingParser;


public class SinglePostFragment extends Fragment {
    private static final String TEXT = "postIndex";
    private int PostIndex;
    private Button myButton;
    private UserSingleton user;
    private Post post;
    // private NetworkManager networkManager;

    public SinglePostFragment() {
        // Required empty public constructor
    }

    public static SinglePostFragment newInstance(int PostIndex){
        SinglePostFragment fragment = new SinglePostFragment();
        Bundle args = new Bundle();
        args.putInt(TEXT, PostIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            PostIndex = getArguments().getInt(TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_single_post, container, false);
        user = UserSingleton.getUserInstance();
        post = user.getPosts().get(PostIndex);

        ImageView profilepic = (ImageView) v.findViewById(R.id.profilepic);
        // ImageView picture = (ImageView) findViewById(R.id.picture);

        // all the ui components
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setText(post.getTitle());
        //TextView date = (TextView) v.findViewById(R.id.date);
        //date.setText(post.getTimeend().substring(0, 8));
        TextView price = (TextView) v.findViewById(R.id.price);
        price.setText(String.valueOf(post.getPrice()));
        TextView description = (TextView) v.findViewById(R.id.description);
        description.setText(post.getDescription());
        TextView address = (TextView) v.findViewById(R.id.address);
        address.setText(post.getLocation());
        TextView num = (TextView) v.findViewById(R.id.numAvailable);
        num.setText(String.valueOf(post.getNumAvailable()));

        TextView kindlabel = (TextView) v.findViewById(R.id.kindlabel);
        kindlabel.setText(String.valueOf(post.getKind()));


        TextView starttime = (TextView) v.findViewById(R.id.starttime);
        starttime.setText(String.valueOf(post.getTimestart()));

        TextView endtime = (TextView) v.findViewById(R.id.endtime);
        endtime.setText(String.valueOf(post.getTimeend()));

        TextView categories = (TextView) v.findViewById(R.id.categories);
        categories.setText(String.valueOf(post.getCategory()));
        TextView tags = (TextView) v.findViewById(R.id.tags);
        tags.setText(String.valueOf(post.getTag()));


        TextView groups = (TextView) v.findViewById(R.id.spGroupName);
        groups.setText(post.getGroupName());



        Button request = (Button) v.findViewById(R.id.requestButton);
        if(post.getAvailable() == 0){
            request.setEnabled(false);
        }
        request.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText location = (EditText) v.findViewById(R.id.locationEdit);
                if(location == null){
                    System.out.println("the fuck?");
                }else{
                    String loc = location.getText().toString();
                }
                String loc = "starbucks";
                RequestNetwork rn = new RequestNetwork(UserSingleton.getUserInstance().get_id(),
                        post.get_id(),loc);
                rn.execute();
                TextView tv = (TextView) v.findViewById(R.id.update);
                //tv.setText("request sent");

                // collapse the view
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().finish();
            }
        });

        Button spam = (Button) v.findViewById(R.id.spamButton);
        spam.setOnClickListener (new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO: set logic for spam clicking
            }
        });

//        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);

        profilepic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // checking out op's profile
                Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                User op = post.getUser();
                String userID = op.getId();

                Bundle b = new Bundle();

                b.putString("userid", userID);
                intent.putExtras(b);
                startActivity(intent);
            }

        });


        // use description for rating
        // RatingParser ratingParser = new RatingParser();
        // ratingParser.getRatingWithID(post.getUser().getId(), this);

        return v;
    }

    private class RequestNetwork extends AsyncTask<String,Void,Void>{
        String userid;
        String postid;
        String location;
        public RequestNetwork(String userid, String postid, String location){
            this.userid = userid;
            this.postid = postid;
            this.location = location;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(String... strings) {
            String urlString = String.format(
                    "https://grubmateteam3.herokuapp.com/api/posts?personid=%s&postid=%s&type=request&location=%s",
                    userid, postid, location);
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.connect();
                Parser p = new Parser();
                System.out.println(p.convertStreamToString(connection.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
