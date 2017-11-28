package content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.udacity.test.R;

import group.GroupViewFragment;
import notification.NewsFragment;
import profile.ProfileFragment;
import subscription.SubViewFragment;

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
    Fragment profile = new ProfileFragment();
    Fragment subs = new SubViewFragment();
    Fragment notifs = new NewsFragment();

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
                    setFrame(2);
                    break;
                case R.id.subs:
                    setFrame(3);
                    break;
                case R.id.notifs:
                    setFrame(4);
                    break;
            }
        }

    }

    public void setFrame(int change){
        System.out.println("SWitching " + change);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

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
                ft.hide(profile);
            case 0:
                ft.hide(notifs);
                ft.hide(profile);
                ft.hide(group);
                ft.hide(subs);
                ft.show(newsfeed);
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
                profile.onResume();
                break;
            case 3:
                ft.hide(notifs);
                ft.hide(newsfeed);
                ft.hide(group);
                ft.hide(profile);
                ft.show(subs);
                subs.onResume();
                break;
            case 4:
                ft.show(notifs);
                ft.hide(profile);
                ft.hide(newsfeed);
                ft.hide(group);
                ft.hide(subs);
                break;
            default:
                break;
        }
        System.out.println("Calling replace");
        System.out.println("Called replace");
        ft.commitNow();
    }
}
