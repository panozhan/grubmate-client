package post;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by ArfanR on 11/10/17.
 */

public class EditPost extends AppCompatActivity {

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
    //Spinner groupSpinner;
    Post post;
    UserSingleton user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_post);

        post = (Post)getIntent().getSerializableExtra("Post");
        user = UserSingleton.getUserInstance();

        title = (EditText)findViewById(R.id.edittitle);
        title.setText(String.valueOf(post.getTitle()));

        description = (EditText)findViewById(R.id.editdescription);
        description.setText(String.valueOf(post.getDescription()));

        location = (EditText)findViewById(R.id.editlocation);
        location.setText(String.valueOf(post.getLocation()));

        category = (EditText)findViewById(R.id.editcategory);
        category.setText(String.valueOf(post.getCategory()));

        tag = (EditText)findViewById(R.id.edittag);
        tag.setText(String.valueOf(post.getTag()));

        numavail = (EditText)findViewById(R.id.editnumavail);
        numavail.setText(String.valueOf(post.getNumAvailable()));

        price = (EditText)findViewById(R.id.editprice);
        price.setText(String.valueOf(post.getPrice()));

        timestart = (EditText)findViewById(R.id.edittimestart);
        timestart.setText(String.valueOf(post.getTimestart()));

        timeend = (EditText)findViewById(R.id.edittimeend);
        timeend.setText(String.valueOf(post.getTimeend()));

        //groupSpinner = (Spinner)findViewById(R.id.groupspinner);
        //groupSpinner.setAdapter(new myAdapter(UserSingleton.getUserInstance().getGroups()));

        editPost = (Button)findViewById(R.id.editpost);
        editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post temp = new Post();
                temp.setTitle(title.getText().toString());
                temp.setDescription(description.getText().toString());
                temp.setLocation(location.getText().toString());
                temp.setCategory(category.getText().toString());
                temp.setTag(tag.getText().toString());
                temp.setNumAvailable(Integer.parseInt(numavail.getText().toString()));
                temp.setPrice(price.getText().toString());
                temp.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                temp.setTimestart(timestart.getText().toString());
                temp.setTimeend(timeend.getText().toString());

                NetworkManager networkManager = new NetworkManager();
                networkManager.editPost("edit", user.get_id(), post.get_id(), temp);

                finish();
            }
        });
    }

    private class myAdapter implements SpinnerAdapter {
        ArrayList<Group> g;
        public myAdapter(ArrayList<Group> g){
            this.g = g;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater()
                        .inflate(R.layout.row_item,null);
            }
            TextView text = (TextView) convertView.findViewById(R.id.txtName);
            text.setText(g.get(position).getName());
            convertView.findViewById(R.id.checkBox).setVisibility(View.GONE);

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater()
                        .inflate(R.layout.row_item,null);
            }
            TextView text = (TextView) convertView.findViewById(R.id.txtName);
            text.setText(g.get(position).getName());
            convertView.findViewById(R.id.checkBox).setVisibility(View.GONE);

            return convertView;
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }
    }
}
