package post;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.udacity.test.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import objects.Group;
import objects.NetworkManager;
import objects.Post;
import objects.UserSingleton;

/**
 * Created by Alex Pan on 10/18/2017.
 */

public class MakePost extends AppCompatActivity {
    EditText title;
    EditText description;
    EditText price;
    EditText tag;
    EditText category;
    EditText numavail;
    EditText timestart;
    EditText timeend;
    EditText location;
    Button postNow;
    Spinner groupSpinner;
    Spinner kindSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_makepost);
        title = (EditText)findViewById(R.id.edittitle);
        description = (EditText)findViewById(R.id.editdescription);
        price = (EditText)findViewById(R.id.editprice);
        tag = (EditText)findViewById(R.id.edittag);
        category = (EditText)findViewById(R.id.editcategory);
        numavail = (EditText)findViewById(R.id.editnumavail);
        timestart = (EditText)findViewById(R.id.edittimestart);
        timeend = (EditText)findViewById(R.id.edittimeend);
        postNow = (Button)findViewById(R.id.postnow);
        location = (EditText)findViewById(R.id.editlocation);
        groupSpinner = (Spinner)findViewById(R.id.groupspinner);
        kindSpinner = (Spinner)findViewById(R.id.kindspinner);
        ArrayList<Group> gs = UserSingleton.getUserInstance().getGroups();
        ArrayList<String> groupNames = new ArrayList<>();
        for(Group g : gs){
            groupNames.add(g.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groupNames);
        groupSpinner.setAdapter(adapter);

        //groupSpinner.setAdapter(new myAdapter(UserSingleton.getUserInstance().getGroups()));

        postNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post post = new Post();
                post.setTag(tag.getText().toString());
                post.setTitle(title.getText().toString());
                post.setPrice(price.getText().toString());
                post.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                post.setCategory(category.getText().toString());
                post.setNumAvailable(Integer.parseInt(numavail.getText().toString()));
                post.setTimestart(timestart.getText().toString());
                post.setTimeend(timeend.getText().toString());
                post.setDescription(description.getText().toString());
                post.setLocation(location.getText().toString());
                post.getGroups().add(UserSingleton.getUserInstance()
                        .getGroups().get(groupSpinner.getSelectedItemPosition()).getId());
                post.setKind(kindSpinner.getSelectedItem().toString());
                NetworkManager networkManager = new NetworkManager();
                networkManager.postPost(post);

                // updates in profile
                UserSingleton.getUserInstance().addPost(post);

                finish();
            }
        });
    }
}
