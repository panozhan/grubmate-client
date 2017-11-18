package content;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import filter.FilterNewsFragment;
import group.GroupViewActivity;
import post.MakePostFragment;
import profile.ProfileFragment;
import com.example.udacity.test.R;
import subscription.SubViewActivity;

import objects.Group;
import objects.UserSingleton;

/**
 * Created by Alex Pan on 10/17/2017.
 */

public class ContentActivity  extends AppCompatActivity {
    TextView navnewsfeed;
    TextView navgroup;
    TextView navprofile;
    TextView navsubs;
    TextView navnotifs;

    Fragment newsfeed = new NewsFeedFragment();
    Fragment group = null;
    Fragment profile = ProfileFragment.newInstance((UserSingleton.getUserInstance().get_id()));
    Fragment subs = null;
    Fragment notifs = new NewsFragment();
    Fragment makepost = new MakePostFragment();
    Fragment filter = new FilterNewsFragment();

    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        navnewsfeed = (TextView)findViewById(R.id.newsfeed);
        navgroup = (TextView)findViewById(R.id.groups);
        navprofile = (TextView)findViewById(R.id.profile);
        navsubs = (TextView)findViewById(R.id.subs);
        navnotifs = (TextView) findViewById(R.id.notifs);

        setFrame(-1);

        NavListener myNavListener = new NavListener();
        navnewsfeed.setOnClickListener(myNavListener);
        navgroup.setOnClickListener(myNavListener);
        navprofile.setOnClickListener(myNavListener);
        navsubs.setOnClickListener(myNavListener);
        navnotifs.setOnClickListener(myNavListener);

        UserSingleton.getUserInstance().addGroup(new Group("Team 3",null));
        UserSingleton.getUserInstance().addGroup(new Group("Team 2",null));

        //NetworkManager nm = new NetworkManager();
        //nm.getGroups();
    }

    private class NavListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.newsfeed:
                    setFrame(0);
                    break;
                case R.id.groups:
                    Intent i = new Intent(getApplicationContext(),GroupViewActivity.class);
                    startActivity(i);
                    break;
                case R.id.profile:
                    setFrame(2);
                    break;
                case R.id.subs:
                    Intent sub = new Intent(getApplicationContext(), SubViewActivity.class);
                    startActivity(sub);
                    break;
                case R.id.notifs:
                    setFrame(4);
                    break;
                case R.id.filter:
                    setFrame(6);
                    break;
            }
        }

    }

    public void setFrame(int change){
        System.out.println("SWitching " + change);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment switchto;
       // ft.remove(newsfeed);
       // ft.remove(notifs);
        switch (change){
            case -1:
                ft.add(R.id.contentframe,newsfeed);
                ft.add(R.id.contentframe,notifs);
                ft.add(R.id.contentframe,profile);
                ft.hide(notifs);
            case 0:
                ft.hide(notifs);
                ft.hide(profile);
                ft.show(newsfeed);
                //switchto = newsfeed;
                break;
            case 1:
                switchto = group;
                break;
            case 2:
                ft.hide(notifs);
                ft.hide(newsfeed);
                ft.show(profile);
                switchto = profile;
                break;
            case 3:
                switchto = subs;
                break;
            case 4:
                ft.show(notifs);
                ft.hide(profile);
                ft.hide(newsfeed);
                break;
            default:
                switchto = filter;
                break;
        }
        System.out.println("Calling replace");
        //ft.replace(R.id.contentframe,switchto);
        System.out.println("Called replace");
        ft.commitNow();
    }
}
