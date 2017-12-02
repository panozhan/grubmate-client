package notification;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.udacity.test.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import objects.NetworkManager;
import objects.Parser;
import objects.UserSingleton;
import profile.RateActivity;

/**
 * Created by Alex Pan on 10/27/2017.
 */

public class NewsFragment extends android.support.v4.app.Fragment {
    Context c ;
    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        c = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b){
        System.out.println("This is calledd");
        View v = inflater.inflate(R.layout.fragment_notifications,container,false);

        MyListener myListener = new MyListener();
        v.findViewById(R.id.news).setOnClickListener(myListener);
        v.findViewById(R.id.requests).setOnClickListener(myListener);
        v.findViewById(R.id.status).setOnClickListener(myListener);

        return v;
    }

    private class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent i = new Intent(c,NewsActivity.class);
            switch (v.getId()){
                case R.id.news:
                    i.putExtra("display","news");
                    break;
                case R.id.status:
                    i.putExtra("display","status");
                    break;
                case R.id.requests:
                    i.putExtra("display","req");
                    break;
            }
            startActivityForResult(i,0);
        }
    }


}
