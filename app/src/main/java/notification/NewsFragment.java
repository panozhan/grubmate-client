package notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.udacity.test.R;

import java.util.ArrayList;

import objects.NetworkManager;
import objects.Notification;
import objects.UserSingleton;

/**
 * Created by Alex Pan on 10/27/2017.
 */

public class NewsFragment extends android.support.v4.app.Fragment {
    ListView requests;
    ListView status;
    ListView news;
    Button refresh;
    UserSingleton owner = UserSingleton.getUserInstance();
    NetworkManager networkManager = new NetworkManager();
    NewsFragment f = this;
    TextView pickup;

    myAdapterPost requesta;
    myAdapterPost statusa;
    myAdapterPost newsa;

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);

    }

    public void notifyChange(){
        requesta.notifyDataSetChanged();
        statusa.notifyDataSetChanged();
        newsa.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b){
        System.out.println("This is calledd");
        View v = inflater.inflate(R.layout.fragment_notifications,container,false);
        requests = (ListView) v.findViewById(R.id.requestlist);
        status = (ListView) v.findViewById(R.id.statuslist);
        news = (ListView) v.findViewById(R.id.newslist);
        refresh = (Button) v.findViewById(R.id.refresh);


        networkManager.getNotifications(owner.get_id(),f);

        requesta = new myAdapterPost(owner.getRequestsNotifications(),this);
        statusa = new myAdapterPost(owner.getStatusNotifications(),this);
        newsa = new myAdapterPost(owner.getNewsNotifications(),this);

        requests.setAdapter(requesta);
        status.setAdapter(statusa);
        news.setAdapter(newsa);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkManager.getNotifications(owner.get_id(),f);
            }
        });

        return v;
    }


    private class myAdapterPost extends ArrayAdapter<Notification> {
        NetworkManager networkManager = new NetworkManager();
        NewsFragment f;
        ArrayList<Notification> notifications;
        public myAdapterPost(ArrayList<Notification> notifications,NewsFragment f){
            super(getActivity(),0,notifications);
            this.notifications = notifications;
            this.f = f;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.single_notification,null);
            }
            final Notification current = notifications.get(position);

            System.out.println(current.getTitle());
            ((TextView)convertView.findViewById(R.id.title)).setText(current.getTitle());
            ((TextView)convertView.findViewById(R.id.address)).setText(current.getAddress());
            Button confirm = (Button)convertView.findViewById(R.id.confirm);
            if(current.getType().equals("request")){
                System.out.println(current.getTitle());
                confirm.setVisibility(View.VISIBLE);
            }else if(current.getType().equals("status")){
                ((TextView)convertView.findViewById(R.id.status)).setText(current.getStatus());
                convertView.findViewById(R.id.pickup).setVisibility(View.GONE);
            }

            convertView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Button)v).setText("End");
                    //
                    //f.notifyChange();
                 //   networkManager.updatePost("confirm",current.getPersonid(),current.getPostId(), "dummy");

                }
            });
            return convertView;
        }

    }
}
