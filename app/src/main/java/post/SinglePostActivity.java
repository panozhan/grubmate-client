package post;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import profile.ProfileFragment;
import com.example.udacity.test.R;

import objects.Post;
import objects.UserSingleton;

public class SinglePostActivity extends AppCompatActivity {

    String userID;
    ProfileFragment profileFragment;
    SinglePostFragment postFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        int PostIndex = getIntent().getIntExtra("PostIndex", -1);
        Post post = UserSingleton.getUserInstance().getPosts().get(PostIndex);
        userID = post.get_id();
        profileFragment = new ProfileFragment();


        postFragment = SinglePostFragment.newInstance(PostIndex);

        change(true);


    }

    public void change(boolean singlepost){
        if (singlepost){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.postlay,postFragment);
            transaction.commit();
        }
        else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.postlay,profileFragment);
            transaction.commit();
        }
    }


}
