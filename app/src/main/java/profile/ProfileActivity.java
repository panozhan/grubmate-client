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

import objects.Parser;
import objects.Post;
import objects.UserSingleton;
import post.EditPost;

public class ProfileActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private TextView name;
    private ListView postList;
    private MyAdapterPost adapter;
    private RatingParser ratingParser;
    private TextView currRating;
    private String userID;
    private UserSingleton owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // same view as fragment
        setContentView(R.layout.fragment_profile);

        // get userid from intent
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userid");
        }
        System.out.println("wtfid "+userID);

        owner = UserSingleton.getUserInstance();

        // UI stuff
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        name = (TextView) findViewById(R.id.profilename);
        currRating = (TextView) findViewById(R.id.profileRating);

        // pulls and sets rating on UI
        ratingParser = new RatingParser();
        ratingParser.getRatingWithID(userID, this);
        String username = owner.getFriendNameByID(userID);
        name.setText(username);

        // set title
        setTitle(username + "'s Profile");

        // get posts
        PostParser pp = new PostParser(this, userID);
        pp.execute();

        postList = (ListView) findViewById(R.id.posts);
        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newActivity = new Intent(getApplicationContext(), EditPost.class);
                newActivity.putExtra("Post", (Post)parent.getAdapter().getItem(position));
                startActivity(newActivity);
            }
        });

        // see reviews
        // for text reviews, click on number rating
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                Bundle b = new Bundle();
                b.putString("userid", userID);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    public void setRating(float rating) {
        ratingBar.setRating(rating);
        currRating.setText(String.format("%.2f", rating));
    }

    public void setPosts(ArrayList<Post> p){
        adapter = new MyAdapterPost(this, p);
        postList.setAdapter(adapter);
    }

    private class PostParser extends AsyncTask<String,Void,Void> {
        private ProfileActivity pa;
        private ArrayList<Post> result = new ArrayList<>();
        private String posterID;

        public PostParser(ProfileActivity pa, String posterID){
            this.pa = pa;
            this.posterID = posterID;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pa.setPosts(result);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try{
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/user?userid="
                        + posterID);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                JsonReader reader = new JsonReader(new InputStreamReader(is,"UTF-8"));
                reader.beginObject();
                ArrayList<String> postsIds = new ArrayList<>();
                while(reader.hasNext()){
                    String name = reader.nextName();
                    if(!name.equals("postsOfUser")){
                        reader.skipValue();
                    }else{
                        reader.beginArray();
                        while(reader.hasNext()) {
                            postsIds.add(reader.nextString());
                        }
                        reader.endArray();
                    }
                }
                reader.endObject();

                for(String id : postsIds){
                    URL url2 = new URL("https://grubmateteam3.herokuapp.com/api/singlepost?postid="
                            + id);

                    HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                    conn2.setRequestMethod("GET");
                    conn2.connect();

                    InputStream is2 = conn2.getInputStream();

                    Parser p = new Parser();
                    Post post = p.parsePost(is2);

                    result.add(post);
                }

            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }
    }


    private class MyAdapterPost extends ArrayAdapter<Post> {
        private ArrayList<Post> posts;
        private ProfileActivity pa;
        public MyAdapterPost(ProfileActivity pa, ArrayList<Post> posts){
            super(pa, 0, posts);
            this.posts = posts;
            this.pa = pa;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = pa.getLayoutInflater().inflate(R.layout.single_post, null);
            }
            final Post current = posts.get(position);

            if (current!=null) {
                ((TextView)convertView.findViewById(R.id.title)).setText(current.getTitle());
                ((TextView)convertView.findViewById(R.id.description)).setText(current.getDescription());
                ((TextView)convertView.findViewById(R.id.price)).setText(current.getPrice());
                ((TextView)convertView.findViewById(R.id.date)).setText(current.getDate());
                ((TextView)convertView.findViewById(R.id.address)).setText(current.getLocation());
            }
            
            return convertView;
        }
    }
}
