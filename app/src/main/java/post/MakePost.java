package post;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.udacity.test.R;

import java.text.DateFormat;
import java.util.Date;

import objects.NetworkManager;
import objects.Post;

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
                NetworkManager networkManager = new NetworkManager();
                networkManager.postPost(post);

                finish();
            }
        });
    }
}
