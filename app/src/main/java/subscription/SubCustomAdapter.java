package subscription;

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

import objects.NetworkManager;

/**
 * Created by ArfanR on 11/10/17.
 */

public class SubCustomAdapter extends ArrayAdapter<SubModel> {

    private ArrayList<SubModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView subName;
        Button deleteButton;
    }

    public SubCustomAdapter(ArrayList<SubModel> data, Context context) {
        super(context, R.layout.sub_item, data);
        this.dataSet = data;
        this.mContext = context;

    }
    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public SubModel getItem(int position) {
        return dataSet.get(position);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item, parent, false);
            viewHolder.subName = (TextView) convertView.findViewById(R.id.subViewName);
            viewHolder.deleteButton = (Button) convertView.findViewById(R.id.deleteViewbutton);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        SubModel item = getItem(position);

        viewHolder.subName.setText(item.name);
        viewHolder.deleteButton.setText(item.deleteTxt);

        final int positionToRemove = position;

        // add listeners
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dataSet.remove(positionToRemove);
                NetworkManager networkManager = new NetworkManager();
                networkManager.deleteSubscription(positionToRemove);
                notifyDataSetChanged();
            }
        });

        return result;
    }
}
