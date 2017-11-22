package post;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private class myAdapter implements SpinnerAdapter{
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
        groupSpinner.setAdapter(new myAdapter(UserSingleton.getUserInstance().getGroups()));

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
                NetworkManager networkManager = new NetworkManager();
                networkManager.postPost(post);

                finish();
            }
        });
    }
}
