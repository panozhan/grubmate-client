package group;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.udacity.test.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

import objects.Group;
import objects.NetworkManager;
import objects.UserSingleton;

public class EditGroupActivity extends AppCompatActivity {

    private ArrayList<FriendModel> friendModels;
    private ListView geListView;
    private CustomAdapter adapter;
    private GroupParser groupParser;
    private UserSingleton owner;
    private Group group;
    private Button button;
    private TextView nameLabel;
    private int isAdd;
    private String groupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //groupParser = new GroupParser();

        setContentView(R.layout.activity_edit_group);
        owner = UserSingleton.getUserInstance();

        // UI stuff
        geListView = (ListView) findViewById(R.id.geListview);
        button = (Button) findViewById(R.id.geButton);
        nameLabel = (TextView) findViewById(R.id.geNameLabel);

        // get group name and int from intent
        Bundle b = getIntent().getExtras();
        if(b != null) {
            groupName = b.getString("groupname");
            isAdd = b.getInt("isadd");
        }
        nameLabel.setText(groupName);

        // find selected group
        group = owner.findGroupByName(groupName);

        // set up models and adapters
        friendModels = new ArrayList<FriendModel>();


        // set title according to activity type
        // if add, show list of people NOT in group
        // if remove, show list of people IN group
        if (isAdd==1) {
            setTitle("Add Friends to Your Group");

            for (Map.Entry<String, String> friend: owner.getFriends().entrySet()) {
                if (!owner.isFriendInGroup(friend.getKey(), group)) {
                    friendModels.add(new FriendModel(friend.getValue(), false));
                }
            }
        }
        else {
            setTitle("Remove Users From Your Group");

            for (String friendID: group.getUsers()) {
                String nameStr = owner.getFriendNameByID(friendID);

                // add everyone but yourself
                if (!nameStr.equalsIgnoreCase(owner.getName())) {
                    friendModels.add(new FriendModel(nameStr, false));
                }
            }
        }


        adapter = new CustomAdapter(friendModels, getApplicationContext());

        geListView.setAdapter(adapter);
        geListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FriendModel friendModel = friendModels.get(position);
                friendModel.checked = !friendModel.checked;
                adapter.notifyDataSetChanged();
            }
        });


        // add listeners
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.grpNameTextview);

                ArrayList<String> selected = new ArrayList<String>();
                for (FriendModel fm: friendModels) {
                    if (fm.checked) {
                        String friendID = owner.getFriendIDByName(fm.name);
                        System.out.println("selected: "+friendID+" "+fm.name);
                        selected.add(friendID); // adds selected users to a list
                    }
                }

                // add yourself to group
                //friends.add(owner.get_id());

                //Group group = new Group(et.getText().toString(), friends);
                //owner.addGroup(group);

                //groupParser.addGroupForOwner(group);

                finish();
            }
        });
    }
}
