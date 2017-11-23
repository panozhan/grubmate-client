package subscription;

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

import objects.Subscription;
import objects.UserSingleton;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

/**
 * Created by ArfanR on 11/10/17.
 */

public class SubViewFragment extends android.support.v4.app.Fragment {

    ArrayList<SubModel> subModels;
    ListView listView;
    private SubCustomAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_sub_view, container, false);

        // list of subscriptions
        subModels = new ArrayList<>();
        adapter = new SubCustomAdapter(subModels, getApplicationContext());

        // add listener to make group button
        Button makeSubButton = (Button) v.findViewById(R.id.gvMakeSubButton);
        makeSubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MakeSubActivity.class));
            }
        });

        return v;
    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();

        UserSingleton owner = UserSingleton.getUserInstance();
        subModels.clear();
        for (Subscription s: owner.getSubscriptions()) {
            subModels.add(new SubModel(s.getType(), s.getValue()));
        }

        listView = (ListView) getView().findViewById(R.id.subViewList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void notifyChange(){
        adapter.notifyDataSetChanged();
    }
}
