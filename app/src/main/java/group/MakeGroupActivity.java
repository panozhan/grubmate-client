package group;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.udacity.test.R;

import java.util.ArrayList;

import objects.Group;
import objects.NetworkManager;


/**
 * Created by mollyhe on 10/18/17.
 */

public class MakeGroupActivity extends AppCompatActivity {

    ArrayList<FriendModel> friendModels;
    ListView listView;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_group);
        setTitle("Create New Group");

        listView = (ListView) findViewById(R.id.mgListview);

        friendModels = new ArrayList<>();

        /************* TODO: GET LIST OF FRIENDS HERE ***************/
        /*
        for (int i=0; i<20; i++) {
            String nameStr = "name" + i;
            friendModels.add(new FriendModel(nameStr, false));
        }*/
        friendModels.add(new FriendModel("Molly He", false));
        friendModels.add(new FriendModel("Katherine Chen", false));
        friendModels.add(new FriendModel("Lauren Nelson", false));
        friendModels.add(new FriendModel("Arfan Rehab", false));

        /************* GET LIST OF FRIENDS HERE ***************/

        adapter = new CustomAdapter(friendModels, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FriendModel friendModel = friendModels.get(position);
                friendModel.checked = !friendModel.checked;
                adapter.notifyDataSetChanged();
            }
        });

        // navigating back to group view
        Button doneButton = (Button) findViewById(R.id.mgDoneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* send data to server */
                EditText et = (EditText) findViewById(R.id.grpNameTextview);

                ArrayList<String>friends = new ArrayList<String>();
                for (FriendModel fm: friendModels) {
                    if (fm.checked) {
                        friends.add(fm.name); // adds selected friends to a list
                    }
                }

                Group group = new Group(et.getText().toString(), friends);
                NetworkManager networkManager = new NetworkManager();
                networkManager.sendGroup(group);
                finish();
            }
        });
    }
}
