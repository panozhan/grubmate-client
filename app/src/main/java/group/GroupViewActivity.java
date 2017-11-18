package group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.udacity.test.R;

import java.util.ArrayList;

import objects.Group;
import objects.UserSingleton;

public class GroupViewActivity extends AppCompatActivity {
    ArrayList<GroupModel> groupModels;
    ListView listView;
    private GroupCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
        setTitle("Groups");

        // list of groups
        groupModels = new ArrayList<>();
        adapter = new GroupCustomAdapter(groupModels, getApplicationContext());

        // add listener to make group button
        Button makeGroupButton = (Button) findViewById(R.id.gvMakeGroupButton);
        makeGroupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(GroupViewActivity.this, MakeGroupActivity.class));
            }
        });
    }


    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();

        //Refresh your stuff here

        UserSingleton owner = UserSingleton.getUserInstance();

        groupModels.clear();
        for (Group g: owner.getGroups()) {
            groupModels.add(new GroupModel(g.getName(), g.getUsers()));
        }

        listView = (ListView) findViewById(R.id.groupViewList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
            }
        });
    }
}
