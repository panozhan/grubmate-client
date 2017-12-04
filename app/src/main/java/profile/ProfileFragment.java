package profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udacity.test.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import objects.NetworkManager;
import objects.Parser;
import objects.Post;
import objects.UserSingleton;
import post.EditPost;

/*****
 *
 * ProfileFragment is for owner, use ProfileActivity for poster
 * find and fix SinglePostActivity ProfileActivity
 *
 * *****/

public class ProfileFragment extends Fragment {
    private static final String TEXT = "text";
    private RatingBar ratingBar;
    private TextView name;
    private ListView postList;
    private UserSingleton owner;
    private MyAdapterPost adapter;
    private RatingParser ratingParser;
    private TextView currRating;
    private int numPosts;
    private ArrayList<Post> posts = new ArrayList<>();
    private final ProfileFragment f = this;
    private Context context;
    private ListView historyList;
    private MyHistoryAdapter historyAdapter;
    private Button button;

    public ProfileFragment(){
        this.owner = UserSingleton.getUserInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    public void crap(){
        System.out.println(posts.size());

    }
    private class PostParser extends AsyncTask<String,Void,Void>{
        ProfileFragment f;
        ArrayList<Post> result;
        public PostParser(ProfileFragment f, ArrayList<Post> result){
            System.out.println("CALLING POST SHIT");
            this.f = f;
            this.result = result;
            result.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            f.setPosts();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try{
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/user?userid="
                        + UserSingleton.getUserInstance().get_id());

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

                    try {
                        System.out.println(id);
                        URL url2 = new URL("https://grubmateteam3.herokuapp.com/api/singlepost?postid="
                                + id);

                        HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                        conn2.setRequestMethod("GET");
                        conn2.connect();


                        InputStream is2 = conn2.getInputStream();
                        Parser p = new Parser();

                        Post post = p.parsePost(is2);

                        if(post != null){
                            System.out.println("added");
                            result.add(post);
                            f.crap();
                        }
                    }catch(FileNotFoundException e){
                        System.out.println("couldn't find file with id " + id);
                    }

                }
                owner.setPostObjectsOfUser(result);
                numPosts = owner.getNumOwnPosts();

            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_profile,container,false);
        context = getActivity();
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        name = (TextView) v.findViewById(R.id.profilename);
        name.setText(owner.getName());
        currRating = (TextView) v.findViewById(R.id.profileRating);
        numPosts = 0;

        // get rating
        ratingParser = new RatingParser();
        ratingParser.getRatingWithID(owner.get_id(), this);

        PostParser pp = new PostParser(this,posts);
        pp.execute();

        postList = (ListView) v.findViewById(R.id.posts);
        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newActivity = new Intent(getActivity(), EditPost.class);
                newActivity.putExtra("Post", (Post)parent.getAdapter().getItem(position));
                startActivity(newActivity);
            }
        });

        // for text reviews, click on number rating
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ReviewActivity.class);
                Bundle b = new Bundle();
                b.putString("userid", owner.get_id());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        postList = (ListView) v.findViewById(R.id.posts);
        adapter = new MyAdapterPost(posts);
        postList.setAdapter(adapter);


        historyList = (ListView) v.findViewById(R.id.history);
        ArrayList<Transaction> myTransactions = new ArrayList<Transaction>();
        historyAdapter = new MyHistoryAdapter(myTransactions);
        historyList.setAdapter(historyAdapter);
        GetTransactions t = new GetTransactions(this, myTransactions);
        t.execute();
        //myTransactions = t.transactions;
        System.out.println("Transaction Size!!!!!: = "+ myTransactions.size());

        System.out.println("aaaaaaaaaaaaa");


        System.out.println("bbbbbbbbbbbbbbb");

        // for text reviews, click on number rating
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ReviewActivity.class);
                Bundle b = new Bundle();
                b.putString("userid", owner.get_id());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        button = (Button)v.findViewById(R.id.switchview);
        button.setText("Transaction History");
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (historyList.getVisibility() == View.INVISIBLE){  // show History
                    button.setText("Show My Posts");
                    historyList.setVisibility(View.VISIBLE);
                    postList.setVisibility(View.INVISIBLE);
                }
                else{   // show posts
                    button.setText("Transaction History");
                    historyList.setVisibility(View.INVISIBLE);
                    postList.setVisibility(View.VISIBLE);
                }
            }
        });


        return v;
    }

    public void refresh(){
        historyAdapter.notifyDataSetChanged();
    }


    private class GetTransactions extends AsyncTask<String,Void,Void>{
        private ProfileFragment f;
        private ArrayList<Transaction> transactions;
        public GetTransactions(ProfileFragment f, ArrayList<Transaction> transactions){
            this.f = f;
            this.transactions = transactions;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            f.refresh();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try{
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/getTransactions?userid="
                        + UserSingleton.getUserInstance().get_id());

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                ArrayList<Transaction> transactions1 = new Parser().parseTransactions(is);
                transactions.clear();
                for(Transaction t : transactions1){
                    transactions.add(t);
                }

                return null;
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }


    private class MyHistoryAdapter extends ArrayAdapter<Transaction> {
        ArrayList<Transaction> transcations;
        public MyHistoryAdapter(ArrayList<Transaction> transcations){
            super(getActivity(),0,transcations);
            this.transcations = transcations;
            System.out.println("iiiiiiiinnnnnnnn");
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.single_trans,null);
                System.out.println("iiiiinflate");
            }
            final Transaction current = transcations.get(position);
            if (current!=null) {
                System.out.println("traaaaaaaans");
                ((TextView) convertView.findViewById(R.id.title)).setText(current.getPostTitle());
                ((TextView) convertView.findViewById(R.id.rtime)).setText(current.getTimeRequested());
                ((TextView) convertView.findViewById(R.id.ctime)).setText(current.getTimeConfirmed());
                ((TextView) convertView.findViewById(R.id.etime)).setText(current.getTimeEnded());
                ((TextView) convertView.findViewById(R.id.t5)).setText("You are " + current.getType());
            }
            return convertView;
        }
    }


    public void setRating(float rating) {
        ratingBar.setRating(rating);
        currRating.setText(String.format("%.2f", rating));
    }

    private void setPosts(){
        adapter.notifyDataSetChanged();
        System.out.println("done");
    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();

        // refresh rating
        System.out.println("in on resume");
        ratingParser = new RatingParser();
        ratingParser.getRatingWithID(owner.get_id(), this);

        // updates num posts
        numPosts = posts.size();
        if (numPosts<owner.getNumOwnPosts()) {
            adapter.addPost(owner.getPosts().get(owner.getNumOwnPosts()-1));
        }
    }

    private class MyAdapterPost extends ArrayAdapter<Post> {
        ArrayList<Post> posts;
        public MyAdapterPost(ArrayList<Post> posts){
            super(getActivity(),0,posts);
            this.posts = posts;
        }

        public void addPost(Post post) {
            if (post!=null) {
                this.posts.add(post);
                notifyDataSetChanged();
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.single_post,null);
            }
            final Post current = posts.get(position);

            if (current!=null) {
                ((TextView)convertView.findViewById(R.id.title)).setText(current.getTitle());
                ((TextView)convertView.findViewById(R.id.description)).setText(current.getDescription());
                ((TextView)convertView.findViewById(R.id.price)).setText(current.getPrice());

                String enddate = current.getDate();
                int sepIndex = current.getTimeend().indexOf(":");
                if (sepIndex>0) {
                    enddate = current.getTimeend().substring(0, sepIndex);
                }
                ((TextView)convertView.findViewById(R.id.date)).setText(enddate);

                ((TextView)convertView.findViewById(R.id.address)).setText(current.getLocation());
                Button removeButton = (Button)convertView.findViewById(R.id.removepostbutton);
                removeButton.setVisibility(View.VISIBLE);
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new RemovePostNetwork(position,current.get_id()).execute();
                    }
                });


                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(current.isEditable()){
                            Intent newActivity = new Intent(getActivity(), EditPost.class);
                            newActivity.putExtra("Post", current);
                            startActivity(newActivity);
                        }else{
                            Toast toast = Toast.makeText(context, "You can't edit this post because there have been confirmed requests already!", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }
                });
            }

            return convertView;
        }
    }

    public void removeAPostFromCurrentPostsArray(int index){
        posts.remove(index);
        adapter.notifyDataSetChanged();
    }

    private class RemovePostNetwork extends AsyncTask<String,Void,String>{
        int index;
        String postid;
        public RemovePostNetwork(int index, String postid){
            this.index = index;
            this.postid = postid;
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/posts?postid="
                        + postid);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                String x = new Parser().convertStreamToString(is);

                return x;
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String b) {
            if(b != null && b.equals("0")){
                Toast toast = Toast.makeText(context, "You can't remove this post because there have been confirmed requests already!", Toast.LENGTH_SHORT);
                toast.show();
            }else if(b != null && b.equals("1")){
                f.removeAPostFromCurrentPostsArray(index);
            }else{
                if(b == null){
                    System.out.println("fucked up");
                }else{
                    System.out.println(b);
                }

                Toast toast = Toast.makeText(context, "A networking error has occured. Try again.", Toast.LENGTH_SHORT);
                toast.show();
            }
            super.onPostExecute(b);
        }
    }
}