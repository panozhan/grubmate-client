package group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.udacity.test.R;

import java.util.ArrayList;

import objects.Group;
import objects.UserSingleton;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by mollyhe on 10/18/17.
 */

public class GroupCustomAdapter extends ArrayAdapter<GroupModel> {
    private UserSingleton owner;
    private ArrayList<GroupModel> dataSet;
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView grpName;
        Button leaveButton;
        Button addButton;
        Button removeButton;
    }

    public GroupCustomAdapter(ArrayList<GroupModel> data, Context context) {
        super(context, R.layout.group_item, data);
        this.dataSet = data;
        this.mContext = context;
        this.owner = UserSingleton.getUserInstance();
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
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
            viewHolder.grpName = (TextView) convertView.findViewById(R.id.groupViewName);
            viewHolder.leaveButton = (Button) convertView.findViewById(R.id.leaveViewbutton);
            viewHolder.addButton = (Button) convertView.findViewById(R.id.addViewbutton);
            viewHolder.removeButton = (Button) convertView.findViewById(R.id.removeViewbutton);

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        GroupModel item = getItem(position);

        viewHolder.grpName.setText(item.name);
        viewHolder.leaveButton.setText(item.leaveTxt);
        viewHolder.addButton.setText("Add");
        viewHolder.removeButton.setText("Remove");

        final int positionToRemove = position;

        // add listeners
        viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditGroupActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putString("groupname", viewHolder.grpName.getText().toString());
                b.putInt("isadd",1);
                intent.putExtras(b);
                mContext.startActivity(intent);
            }
        });

        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditGroupActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putString("groupname", viewHolder.grpName.getText().toString());
                b.putInt("isadd",0);
                intent.putExtras(b);
                mContext.startActivity(intent);
            }
        });

        viewHolder.leaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GroupParser groupParser = new GroupParser();

                // add yourself to list of user to remove from group
                ArrayList<String> selected = new ArrayList<String>();
                selected.add(owner.get_id());

                // find group id by name
                Group group = owner.findGroupByName(viewHolder.grpName.getText().toString());

                // remove yourself from db
                groupParser.editUsersInGroup(selected, group.getId(), "remove");

                // updating UI
                owner.removeGroup(positionToRemove);
                dataSet.remove(positionToRemove);
                notifyDataSetChanged();
            }
        });

        return result;
    }
}