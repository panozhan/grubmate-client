package subscription;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.udacity.test.R;

import objects.NetworkManager;
import objects.Subscription;
import objects.UserSingleton;

/**
 * Created by ArfanR on 11/10/17.
 */

public class MakeSubActivity extends AppCompatActivity {

    NetworkManager networkManager;
    EditText subCategory, subName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_sub);
        setTitle("Create New Subscription");

        subCategory = (EditText) findViewById(R.id.subCategoryLabel);
        subName = (EditText) findViewById(R.id.subNameLabel);

        networkManager = new NetworkManager();

        // navigating back to sub view
        Button makeSubscription = (Button) findViewById(R.id.makeSubButton);
        makeSubscription.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* send data to server */
                Subscription sub = new Subscription(subCategory.getText().toString(), subName.getText().toString());
                networkManager.addSubscription(UserSingleton.getUserInstance().get_id(), sub);
                //networkManager.searchSubsForUser(sub.getType());
                finish();
            }
        });
    }


}

