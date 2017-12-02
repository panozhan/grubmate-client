package subscription;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.udacity.test.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import objects.Parser;
import objects.Subscription;
import objects.UserSingleton;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

/**
 * Created by ArfanR on 11/10/17.
 */

public class SubViewFragment extends android.support.v4.app.Fragment {

    ArrayList<SubModel> subModels = new ArrayList<>();
    ListView listView;
    private SubCustomAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private class GetSubscriptions extends AsyncTask<String, Void, Void> {
        String userid;
        Parser parser = new Parser();
        ArrayList<SubModel> subs;
        public GetSubscriptions(String userid, ArrayList<SubModel> subs) {
            this.userid = userid;
            this.subs = subs;
            this.subs.clear();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String address = "https://grubmateteam3.herokuapp.com/api/subs?userid=" + userid;
                URL url = new URL(address);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                InputStream inputStream;
                // get stream
                if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = urlConnection.getInputStream();
                } else {
                    inputStream = urlConnection.getErrorStream();
                }

                ArrayList<Subscription> updates = parser.parseSubscriptions(inputStream);
                UserSingleton.getUserInstance().setSubscriptions(updates);

                for (Subscription s:  updates) {
                    System.out.println(s);
                    subs.add(new SubModel(s.getType(), s.getValue()));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_sub_view, container, false);

        GetSubscriptions addSub = new GetSubscriptions(UserSingleton.getUserInstance().get_id(), subModels);
        addSub.execute();

        listView = (ListView) v.findViewById(R.id.subViewList);
        adapter = new SubCustomAdapter(subModels, getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        // add listener to make group button
        Button addSubButton = (Button) v.findViewById(R.id.gvMakeSubButton);
        addSubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MakeSubActivity.class));
            }
        });

        return v;
    }


    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();

        GetSubscriptions addSub = new GetSubscriptions(UserSingleton.getUserInstance().get_id(), subModels);
        addSub.execute();
        notifyChange();
    }

    public void notifyChange(){
        adapter.notifyDataSetChanged();
    }
}
