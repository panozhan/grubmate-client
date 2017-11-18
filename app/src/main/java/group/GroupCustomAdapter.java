package group;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.udacity.test.R;

import java.util.ArrayList;

import objects.UserSingleton;

/**
 * Created by mollyhe on 10/18/17.
 */

public class GroupCustomAdapter extends ArrayAdapter<GroupModel> {

    private ArrayList<GroupModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView grpName;
        Button leaveButton;
        Button editButton;
    }

    public GroupCustomAdapter(ArrayList<GroupModel> data, Context context) {
        super(context, R.layout.group_item, data);
        this.dataSet = data;
        this.mContext = context;

    }
    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public GroupModel getItem(int position) {
        return dataSet.get(position);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
            viewHolder.grpName = (TextView) convertView.findViewById(R.id.groupViewName);
            viewHolder.leaveButton = (Button) convertView.findViewById(R.id.leaveViewbutton);
            viewHolder.editButton = (Button) convertView.findViewById(R.id.editViewbutton);

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        GroupModel item = getItem(position);

        viewHolder.grpName.setText(item.name);
        viewHolder.leaveButton.setText(item.leaveTxt);
        viewHolder.editButton.setText(item.editTxt);
        final int positionToRemove = position;

        // add listeners
        viewHolder.leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // removes group

                dataSet.remove(positionToRemove);
                UserSingleton owner = UserSingleton.getUserInstance();
                owner.removeGroup(positionToRemove);
                notifyDataSetChanged();

                /*
                // TODO: figure out how to add alert
                AlertDialog.Builder adb=new AlertDialog.Builder(mContext.getApplicationContext());
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to leave " + viewHolder.grpName.getText() + "?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dataSet.remove(positionToRemove);
                        notifyDataSetChanged();
                    }});
                adb.show();
                */
            }
        });

        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: Add whatever logic is necessary to make sure the groupviewedit has the proper info from the server
                //Intent myIntent = new Intent(mContext.getApplicationContext(), GroupViewEditActivity.class);
                //myIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                //myIntent.putExtra("groupName", viewHolder.grpName.getText()); // (key, val)
                //mContext.getApplicationContext().startActivity(myIntent);


            }
        });

        return result;
    }
}