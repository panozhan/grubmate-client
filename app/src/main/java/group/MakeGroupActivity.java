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
import java.util.Map;

import objects.Group;
import objects.NetworkManager;
import objects.UserSingleton;


/**
 * Created by mollyhe on 10/18/17.
 */

public class MakeGroupActivity extends AppCompatActivity {

    private ArrayList<FriendModel> friendModels;
    private ListView listView;
    private CustomAdapter adapter;
    private GroupParser groupParser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupParser = new GroupParser();

        setContentView(R.layout.activity_make_group);
        setTitle("Create New Group");

        listView = (ListView) findViewById(R.id.mgListview);

        friendModels = new ArrayList<>();

        // use friends that was pulled from database in AccountActivity
        final UserSingleton owner = UserSingleton.getUserInstance();
        for (Map.Entry<String, String> friend : owner.getFriends().entrySet()) {
            String nameStr = friend.getValue();
            friendModels.add(new FriendModel(nameStr, false));
        }

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
                        String friendID = owner.getFriendIDByName(fm.name);
                        friends.add(friendID); // adds selected friends to a list
                    }
                }

                // add yourself to group if something is selected
                if (friends!=null && !friends.isEmpty()) {
                    friends.add(owner.get_id());
                    Group group = new Group(null, et.getText().toString(), friends);
                    owner.addGroup(group);

                    groupParser.addGroupForOwner(group);
                }

                finish();
            }
        });
    }
}
