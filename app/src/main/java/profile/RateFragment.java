package profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.udacity.test.R;

import java.util.ArrayList;

import objects.NetworkManager;
import objects.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RateFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private OnFragmentInteractionListener mListener;

    private static final String TEXT = "text";
    private String userID;

    RatingBar ratingBar;
    Button submit;
    TextView name;
    User user;
    NetworkManager networkManager = new NetworkManager();

    public RateFragment() {
        // Required empty public constructor
    }

    public static RateFragment newInstance(String userID){
        RateFragment fragment = new RateFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            userID = getArguments().getString(TEXT);
        }

        user = new User();
        user.setId(userID);
        ArrayList<User> users = new ArrayList<User>();
        users.add(user);
        networkManager.getUser(users);
        user = users.get(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,container,false);

        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar2);
        submit = (Button) v.findViewById(R.id.submit);
        name = (TextView) v.findViewById(R.id.userToRate);
        name.setText(user.getName());


        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                float rating = ratingBar.getRating();
                user.addRating(rating);

            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
