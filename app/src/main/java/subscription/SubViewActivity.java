package subscription;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.udacity.test.R;

import java.util.ArrayList;

import objects.Subscription;
import objects.UserSingleton;

/**
 * Created by ArfanR on 11/10/17.
 */

public class SubViewActivity extends AppCompatActivity {

    ArrayList<SubModel> subModels;
    ListView listView;
    private SubCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_view);
        setTitle("Subscriptions");

        // list of subscriptions
        subModels = new ArrayList<>();
        adapter = new SubCustomAdapter(subModels, getApplicationContext());

        // add listener to make group button
        Button makeSubButton = (Button) findViewById(R.id.gvMakeSubButton);
        makeSubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SubViewActivity.this, MakeSubActivity.class));
            }
        });
    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();

        UserSingleton owner = UserSingleton.getUserInstance();
        subModels.clear();
        for (Subscription s: owner.getSubscriptions()) {
            subModels.add(new SubModel(s.getType(), s.getValue()));
        }

        listView = (ListView) findViewById(R.id.subViewList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
            }
        });
    }
}
