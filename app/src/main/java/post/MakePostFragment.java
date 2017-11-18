package post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.udacity.test.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import objects.Group;
import objects.Post;
import objects.UserSingleton;

/**
 * Created by Alex Pan on 10/17/2017.
 */

public class MakePostFragment extends android.support.v4.app.Fragment{
    EditText title;
    EditText description;
    EditText price;
    EditText tag;
    EditText category;
    EditText numavail;
    EditText timestart;
    EditText timeend;
    Button postNow;
    Spinner spinner;

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b){
        View v = inflater.inflate(R.layout.fragment_profile,container,false);
        title = (EditText)v.findViewById(R.id.edittitle);
        description = (EditText)v.findViewById(R.id.editdescription);
        price = (EditText)v.findViewById(R.id.editprice);
        tag = (EditText)v.findViewById(R.id.edittag);
        category = (EditText)v.findViewById(R.id.editcategory);
        numavail = (EditText)v.findViewById(R.id.editnumavail);
        timestart = (EditText)v.findViewById(R.id.edittimestart);
        timeend = (EditText)v.findViewById(R.id.edittimeend);
        postNow = (Button)v.findViewById(R.id.postnow);
        spinner = (Spinner)v.findViewById(R.id.groupspinner);

        spinner.setAdapter(new SpinnerAdapter(UserSingleton.getUserInstance().getGroups()));

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
            }
        });
        return v;
    }

    private class SpinnerAdapter extends ArrayAdapter<Group>{
        private ArrayList<Group> group;
        public SpinnerAdapter(ArrayList<Group> group){
            super(getActivity(),0,group);
            this.group = group;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.groupspinner,null);
            }
            final Group current = group.get(position);

            ((TextView)convertView.findViewById(R.id.name)).setText(current.getName());
            return convertView;
        }
    }
}
