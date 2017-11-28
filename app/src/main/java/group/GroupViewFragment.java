package group;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.udacity.test.R;

import java.util.ArrayList;

import objects.Group;
import objects.UserSingleton;
import profile.ProfileActivity;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

public class GroupViewFragment extends android.support.v4.app.Fragment {
    ArrayList<GroupModel> groupModels;
    ListView listView;
    private GroupCustomAdapter adapter;
    private Button makeGroupButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // setTitle("Groups");
        View v = inflater.inflate(R.layout.fragment_group_view, container, false);

        //GroupParser groupParser = new GroupParser(this);
        //groupParser.getGroupForOwner();

        // list of groups
        groupModels = new ArrayList<>();
        adapter = new GroupCustomAdapter(groupModels, getApplicationContext());

        // add listener to make group button
        makeGroupButton = (Button) v.findViewById(R.id.gvMakeGroupButton);
        makeGroupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MakeGroupActivity.class));

                /*
                Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                Bundle b = new Bundle();

                // testing purpose
                b.putString("userid", "2");
                intent.putExtras(b);
                startActivity(intent);
                */
            }
        });

        return v;
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();

        //Refresh your stuff here

        UserSingleton owner = UserSingleton.getUserInstance();

        // TODO change gm to take id, and position, move reset to adapter
        groupModels.clear();
        for (Group g: owner.getGroups()) {
            groupModels.add(new GroupModel(g.getName(), g.getUsers()));
        }

        // then remove this part to oncreate
        listView = (ListView) getView().findViewById(R.id.groupViewList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
            }
        });
    }
}
