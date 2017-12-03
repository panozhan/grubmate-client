package content;

/**
 * Created by Alex Pan on 10/17/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.udacity.test.R;

import java.util.ArrayList;

import filter.FilterNewsActivity;
import objects.NetworkManager;
import objects.Post;
import objects.UserSingleton;
import post.MakePost;
import post.SinglePostActivity;

public class NewsFeedFragment extends android.support.v4.app.Fragment{
    UserSingleton owner;
    EditText searchfield;
    Button filter;
    Button newpost;
    ListView listposts;
    NetworkManager networkManager;

    myAdapterPost adapter;
    Button refresh;


    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        owner = UserSingleton.getUserInstance();
        networkManager = new NetworkManager(this);

        adapter = new myAdapterPost(owner.getPosts());
        networkManager.getPostsForUser(owner.get_id());

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b){
        System.out.println("This is calledd");
        View v = inflater.inflate(R.layout.fragment_newsfeed,container,false);

        searchfield = (EditText)v.findViewById(R.id.searchfield);
        searchfield.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    owner.getPosts().clear();
                    networkManager.searchPostsForUser(owner.get_id(), searchfield.getText().toString());
                    notifyChange();
                    return true;
                }
                return false;
            }
        });

        filter = (Button)v.findViewById(R.id.filter);
        newpost = (Button)v.findViewById(R.id.newpost);
        listposts = (ListView)v.findViewById(R.id.listofposts);
        listposts.setAdapter(adapter);
        refresh = (Button)v.findViewById(R.id.refresh);

        ButtonHandler myButtonHandler = new ButtonHandler();
        filter.setOnClickListener(myButtonHandler);
        newpost.setOnClickListener(myButtonHandler);
        refresh.setOnClickListener(myButtonHandler);

        return v;
    }

    public class myAdapterPost extends ArrayAdapter<Post> {
        ArrayList<Post> posts;
        public myAdapterPost(ArrayList<Post> posts){
            super(getActivity(),0,posts);
            this.posts = posts;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.single_post,null);
            }

            Post current = posts.get(position);

            ((TextView)convertView.findViewById(R.id.title)).setText(current.getTitle());
            ((TextView)convertView.findViewById(R.id.description)).setText(current.getDescription());
            ((TextView)convertView.findViewById(R.id.price)).setText(current.getPrice());

            // add in date
            String enddate = current.getDate();
            int sepIndex = current.getTimeend().indexOf(":");
            if (sepIndex>0) {
                enddate = current.getTimeend().substring(0, sepIndex);
            }
            ((TextView)convertView.findViewById(R.id.date)).setText(enddate);

            ((TextView)convertView.findViewById(R.id.address)).setText(current.getLocation());

            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    Intent newActivity = new Intent(getActivity(), SinglePostActivity.class);
                    newActivity.putExtra("PostIndex", position);
                    startActivity(newActivity);
                }
            });

            return convertView;
        }

    }

    private class ButtonHandler implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.filter:
                    //owner.getPosts().clear();
                    //notifyChange();
                    Intent intent = new Intent(getActivity(), FilterNewsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.newpost:
                    Intent i = new Intent(getActivity(),MakePost.class);
                    startActivity(i);
                    break;
                case R.id.refresh:
                    if (searchfield.getText().toString().length() > 0) {
                        owner.getPosts().clear();
                        networkManager.searchPostsForUser(owner.get_id(), searchfield.getText().toString());
                        notifyChange();
                    }
                    else {
                        owner.getPosts().clear();
                        networkManager.getPostsForUser(owner.get_id());
                        notifyChange();
                    }
                    break;
            }
        }
    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();

        adapter.notifyDataSetChanged();
    }


    public void notifyChange(){
        adapter.notifyDataSetChanged();
    }

}
