package profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.udacity.test.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import objects.NetworkManager;
import objects.Post;
import objects.User;
import objects.UserSingleton;
import post.EditPost;
import post.SinglePostActivity;

/**
 * Created by Alex Pan on 10/17/2017.
 */

public class ProfileFragment extends Fragment {
    private static final String TEXT = "text";
    private String userID;
    RatingBar ratingBar;
    String userid;
    TextView name;
    ListView postList;
    UserSingleton owner;
    NetworkManager networkManager = new NetworkManager();
    // ProfileFragment.OnFragmentInteractionListener mListener;

    public ProfileFragment(){
    }


    public static ProfileFragment newInstance(String userID){
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, userID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            userID = getArguments().getString(TEXT);
        }
        owner = UserSingleton.getUserInstance();
        networkManager.getUser(this, userID);
        networkManager.getPostsForUser(userID);
    }



    public void generate (User user){
        name.setText(user.getName());
        ratingBar.setRating(user.getRating());
        userid = user.getId();
        ArrayList<Post> posts = owner.getPosts();
        ArrayList<Post> postsToShow = new ArrayList<Post>();
        for (int i=0; i<posts.size(); i++){
            Post thispost = posts.get(i);
            String temp = thispost.getUser().getId();
            if (temp.equals(userid) ){
                postsToShow.add(thispost);
            }
        }

        postList.setAdapter(new ProfileFragment.myAdapterPost(postsToShow));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_profile,container,false);

        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        name = (TextView) v.findViewById(R.id.name);


//        Button goBack = (Button) v.findViewById(R.id.goBack);
//        goBack.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                ((SinglePostActivity) getActivity()).change(true);
//            }
//        });

        postList = (ListView) v.findViewById(R.id.posts);

        return v;
    }


    private class myAdapterPost extends ArrayAdapter<Post> {
        ArrayList<Post> posts;
        public myAdapterPost(ArrayList<Post> posts){
            super(getActivity(),0,posts);
            this.posts = posts;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.single_post,null);
            }
            final Post current = posts.get(position);

            ((TextView)convertView.findViewById(R.id.title)).setText(current.getTitle());
            ((TextView)convertView.findViewById(R.id.description)).setText(current.getDescription());
            ((TextView)convertView.findViewById(R.id.price)).setText(current.getPrice());
            ((TextView)convertView.findViewById(R.id.date)).setText(current.getDate());
            ((TextView)convertView.findViewById(R.id.address)).setText(current.getLocation());

//            Button editPost = (Button) convertView.findViewById(R.id.editpost);
//            editPost.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v){
//                    Intent newActivity = new Intent(getActivity(), EditPost.class);
//                    newActivity.putExtra("PostIndex", position);
//                    startActivity(newActivity);
//                }
//            });

            return convertView;
        }
    }
}