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
import group.GroupViewFragment;
import group.MakeGroupActivity;
import notification.NewsFragment;
import post.MakePostFragment;
import profile.ProfileFragment;
import com.example.udacity.test.R;

import objects.Group;
import objects.UserSingleton;
import profile.RateActivity;
import subscription.SubViewFragment;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

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
    Fragment group = new GroupViewFragment();
    Fragment profile = ProfileFragment.newInstance((UserSingleton.getUserInstance().get_id()));
    Fragment subs = new SubViewFragment();
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

        //UserSingleton.getUserInstance().addGroup(new Group("Team 3",null));
        //UserSingleton.getUserInstance().addGroup(new Group("Team 2",null));

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
                    setFrame(1);
                    break;
                case R.id.profile:
                    //setFrame(2);
                    Intent intent = new Intent(getApplicationContext(), RateActivity.class);
                    //RateActivity rateActivity = new RateActivity(UserSingleton.getUserInstance().get_id());
                    Bundle b = new Bundle();
                    b.putString("userid", UserSingleton.getUserInstance().get_id()); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                    break;
                case R.id.subs:
                    setFrame(3);
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
                ft.add(R.id.contentframe,group);
                ft.add(R.id.contentframe,subs);
                ft.hide(notifs);
                ft.hide(group);
                ft.hide(subs);
            case 0:
                ft.hide(notifs);
                ft.hide(profile);
                ft.hide(group);
                ft.hide(subs);
                ft.show(newsfeed);
                //switchto = newsfeed;
                break;
            case 1:
                ft.hide(newsfeed);
                ft.hide(notifs);
                ft.hide(profile);
                ft.hide(subs);
                ft.show(group);
                break;
            case 2:
                ft.hide(notifs);
                ft.hide(newsfeed);
                ft.hide(group);
                ft.hide(subs);
                ft.show(profile);
                switchto = profile;
                break;
            case 3:
                ft.hide(notifs);
                ft.hide(newsfeed);
                ft.hide(group);
                ft.hide(profile);
                ft.show(subs);
                break;
            case 4:
                ft.show(notifs);
                ft.hide(profile);
                ft.hide(newsfeed);
                ft.hide(group);
                ft.hide(subs);
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
