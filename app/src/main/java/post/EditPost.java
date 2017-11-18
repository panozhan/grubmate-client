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
import objects.UserSingleton;

/**
 * Created by ArfanR on 11/10/17.
 */

public class EditPost extends AppCompatActivity {

    UserSingleton user;
    Post post;
    EditText title;
    EditText description;
    EditText price;
    EditText tag;
    EditText category;
    EditText numavail;
    EditText timestart;
    EditText timeend;
    EditText location;
    Button editPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_post);

        int PostIndex = getIntent().getIntExtra("PostIndex", -1);
        post = UserSingleton.getUserInstance().getPosts().get(PostIndex);
        user = UserSingleton.getUserInstance();

        title = (EditText)findViewById(R.id.edittitle);
        title.setText(post.getTitle());

        description = (EditText)findViewById(R.id.editdescription);
        description.setText(post.getDescription());

        price = (EditText)findViewById(R.id.editprice);
        price.setText(post.getPrice());

        tag = (EditText)findViewById(R.id.edittag);
        tag.setText(post.getTag());

        category = (EditText)findViewById(R.id.editcategory);
        category.setText(post.getCategory());

        numavail = (EditText)findViewById(R.id.editnumavail);
        numavail.setText(post.getNumAvailable());

        timestart = (EditText)findViewById(R.id.edittimestart);
        timestart.setText(post.getTimestart());

        timeend = (EditText)findViewById(R.id.edittimeend);
        timeend.setText(post.getTimeend());

        location = (EditText)findViewById(R.id.editlocation);
        location.setText(post.getLocation());

        editPost = (Button)findViewById(R.id.editpost);
        editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                networkManager.updatePost("Edit Post", user.get_id(), post.get_id(), post.getLocation());

                finish();
            }
        });
    }
}
