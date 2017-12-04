package notification;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
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
 * Created by mollyhe on 10/18/17.
 */

public class NewsActivity extends AppCompatActivity {
    private String displayInstruction;

    private Button refresh;
    private TextView title;
    private ListView list;
    private NotifStore store = NotifStore.getInstance();

    private myRequestsPost requesta;
    private MyStatusAdapter statusa;
    private MyNewsAdapter newsa;

    private final NewsActivity myactivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if(b != null){
            displayInstruction = b.getString("display");
        }
        setContentView(R.layout.notification_activity);

        refresh = (Button) findViewById(R.id.refresh);
        title = (TextView) findViewById(R.id.mytext);
        list = (ListView) findViewById(R.id.list);


        requesta = new myRequestsPost(store.getRequests());
        statusa = new MyStatusAdapter(store.getStatuses());
        newsa = new MyNewsAdapter(store.getNews());

        if(displayInstruction.equals("news")){
            list.setAdapter(newsa);
            title.setText("News");
        }else if(displayInstruction.equals("req")){
            list.setAdapter(requesta);
            title.setText("My Requests");
        }else if(displayInstruction.equals("status")){
            list.setAdapter(statusa);
            title.setText("My Status");
        }

        getNotifs();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNotifs();
            }
        });
    }

    public void getNotifs(){
        NotifStore.getInstance().clear();
        GetNotif getNotif = new GetNotif(this);
        getNotif.execute();
    }

    private class GetNotif extends AsyncTask<String, Void, Void> {
        NewsActivity f;
        public GetNotif(NewsActivity f){
            this.f = f;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                // This is getting the url from the string we passed in
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/notifications?userid=" + UserSingleton.getUserInstance().get_id());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream is = urlConnection.getInputStream();
                JsonReader reader = new JsonReader(new InputStreamReader(is,"UTF-8"));
                reader.beginArray();
                //news
                reader.beginArray();
                while(reader.hasNext()){
                    reader.beginObject();
                    News n = new News();
                    while(reader.hasNext()){
                        String name = reader.nextName();
                        System.out.println(name);
                        switch (name){
                            case "title":
                                n.setTitle(reader.nextString());
                                break;
                            case "address":
                                n.setAddress(reader.nextString());
                                break;
                            case "postid":
                                n.setPostid(reader.nextString());
                                break;
                            default:
                                reader.skipValue();
                                break;
                        }
                    }
                    NotifStore.getInstance().getNews().add(n);
                    reader.endObject();
                }
                reader.endArray();
                //requests
                reader.beginArray();
                while(reader.hasNext()){
                    reader.beginObject();
                    Requests r = new Requests();
                    while(reader.hasNext()){
                        String name = reader.nextName();
                        switch (name){
                            case "title":
                                r.setTitle(reader.nextString());
                                break;
                            case "address":
                                r.setAddress(reader.nextString());
                                break;
                            case "postid":
                                r.setPostid(reader.nextString());
                                break;
                            case "personid":
                                r.setPersonid(reader.nextString());
                                break;
                            case "status":
                                r.setStatus(reader.nextString());
                                break;
                            default:
                                reader.skipValue();
                                break;
                        }
                    }
                    NotifStore.getInstance().getRequests().add(r);
                    reader.endObject();
                }
                reader.endArray();
                //status
                reader.beginArray();
                while(reader.hasNext()){
                    reader.beginObject();
                    Statusz s = new Statusz();
                    while(reader.hasNext()){
                        String name = reader.nextName();
                        switch (name){
                            case "title":
                                s.setTitle(reader.nextString());
                                break;
                            case "status":
                                s.setStatus(reader.nextString());
                                break;
                            case "postid":
                                s.setPostid(reader.nextString());
                                break;
                            case "personid":
                                s.setPersonId(reader.nextString());
                                break;
                            default:
                                reader.skipValue();
                                break;
                        }
                    }
                    NotifStore.getInstance().getStatuses().add(s);
                    reader.endObject();
                }
                reader.endArray();
                //end
                reader.endArray();

            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            f.notifyChange();
        }
    }

    public void notifyChange(){
        requesta.notifyDataSetChanged();
        statusa.notifyDataSetChanged();
        newsa.notifyDataSetChanged();
    }

    private class MyNewsAdapter extends ArrayAdapter<News> {
        ArrayList<News> news;
        NewsFragment f;
        public MyNewsAdapter(ArrayList<News> news){
            super(c,0,news);
            this.news = news;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getLayoutInflater()
                        .inflate(R.layout.single_news,null);
            }
            final News current = news.get(position);

            System.out.println(current.getTitle());
            ((TextView)convertView.findViewById(R.id.title)).setText(current.getTitle());
            ((TextView)convertView.findViewById(R.id.status)).setText(current.getAddress());

            return convertView;
        }

    }

    private class MyStatusAdapter extends ArrayAdapter<Statusz>{
        ArrayList<Statusz> statuses;
        NewsFragment f;
        public MyStatusAdapter(ArrayList<Statusz> status){
            super(c,0,status);
            this.statuses = status;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getLayoutInflater()
                        .inflate(R.layout.single_sta,null);
            }
            final Statusz current = statuses.get(position);

            System.out.println(current.getTitle());
            ((TextView)convertView.findViewById(R.id.title)).setText(current.getTitle());
            ((TextView)convertView.findViewById(R.id.status)).setText(current.getStatus());
            // ((TextView)convertView.findViewById(R.id.time)).setText(current.getStatus());

            Button confirm = (Button)convertView.findViewById(R.id.transact);
            System.out.println(current.getTitle());
            if(current.getStatus().equals("ended") || current.getStatus().equals("rejected")){
                confirm.setVisibility(View.VISIBLE);
                confirm.setText("Got It!");

                if(current.getStatus().equals("ended")){
                    convertView.findViewById(R.id.transact).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmend(UserSingleton.getUserInstance().get_id(),current.getPostid(),"remove",null);

                            Intent intent = new Intent(getApplicationContext(), RateActivity.class);
                            Bundle b = new Bundle();

                            // TODO: SUB IN POSTER/REQUESTERS IDS INSTEAD OF OWNER'S
                            b.putString("userid", current.getPersonId());
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    });
                } else if(current.getStatus().equals("rejected")){
                    convertView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmend(UserSingleton.getUserInstance().get_id(),current.getPostid(),"remove",null);
                        }
                    });
                }
            }
            return convertView;
        }

    }

    public void confirmend(String userid, String postid, String type, String location){
        ConfirmEnd ce = new ConfirmEnd(this,userid,postid,type,location);
        ce.execute();
    }

    private class ConfirmEnd extends AsyncTask<String, Void, Void>{
        NewsActivity f;
        String userid;
        String postid;
        String type;
        String location;
        public ConfirmEnd(NewsActivity f, String userid, String postid, String type, String location){
            this.f = f;
            this.userid = userid;
            this.postid = postid;
            this.type = type;
            this.location = location;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(type.equals("end") || type.equals("reject") || type.equals("remove")){
                getNotifs();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/posts?personid="
                        + userid + "&postid=" + postid + "&type=" + type + (location == null? "" : "&location=" + location) );

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.connect();
                Parser p = new Parser();
                System.out.println(p.convertStreamToString(urlConnection.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private final Context c = this;

    private class myRequestsPost extends ArrayAdapter<Requests> {
        NetworkManager networkManager = new NetworkManager();
        ArrayList<Requests> requests;
        public myRequestsPost(ArrayList<Requests> requests){
            super(c,0,requests);
            this.requests = requests;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getLayoutInflater()
                        .inflate(R.layout.single_req,null);
            }
            final Requests current = requests.get(position);

            System.out.println(current.getTitle());
            ((TextView)convertView.findViewById(R.id.title)).setText(current.getTitle());
            ((TextView)convertView.findViewById(R.id.status)).setText(current.getAddress());
            Button confirm = (Button)convertView.findViewById(R.id.transact);
            System.out.println(current.getTitle());

            Button reject = (Button)convertView.findViewById(R.id.reject);

            if(current.getStatus().equals("confirmed")){
                reject.setVisibility(View.GONE);
                confirm.setText("End");
            }

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(current.getStatus().equals("requested")){
                        ((Button)v).setText("End");
                        confirmend(current.getPersonid(),current.getPostid(),"confirm",null);
                        current.setStatus("confirmed");
                    }else if(current.getStatus().equals("confirmed")){
                        confirmend(current.getPersonid(),current.getPostid(),"end",null);

                        Intent intent = new Intent(getApplicationContext(), RateActivity.class);
                        Bundle b = new Bundle();

                        //SUB IN POSTER/REQUESTERS IDS INSTEAD OF OWNER'S
                        b.putString("userid", current.getPersonid());
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                }
            });

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmend(current.getPersonid(),current.getPostid(),"reject",null);
                }
            });



            return convertView;
        }

    }
}
