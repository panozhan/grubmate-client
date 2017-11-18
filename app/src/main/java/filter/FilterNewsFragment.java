package filter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.udacity.test.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterNewsFragment extends android.support.v4.app.Fragment {
    // TODO: Add additional parameter arguments as necessary
    private static final String ARG_CATEGORIES = "categories";

    // TODO: Add any additional parameters necessary
    private ArrayList<String> mCategories;
    private OnFragmentInteractionListener mListener;
    private Spinner spinner;
    private Button button;
    private EditText totext;
    private EditText fromtext;
    private String totextstring; //place to store the info from the edit text
    private String fromtextstring; //place to store the info from the edit text

    public FilterNewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categories List of categories to select from.
     * @return A new instance of fragment FilterNewsFragment.
     */
    // TODO: If additional parameters were added, add them to the bundle args.
    public static FilterNewsFragment newInstance(ArrayList<String> categories) {
        FilterNewsFragment fragment = new FilterNewsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_CATEGORIES, categories);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategories = getArguments().getStringArrayList(ARG_CATEGORIES);
            //TODO: IF additional parameters are added, get them.
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_news, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    // TODO: IDK how to do this, please make it so that the button filterbutton does shit.
    // TODO: Like you're probably going to need the get the current selection of spinner
    // TODO: and the text from both fromdate and todate and use those to do whatever filter logic
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void addListenerOnButton() {
        spinner = (Spinner) getView().findViewById(R.id.spinner);
        button = (Button) getView().findViewById(R.id.filterbutton);


        //TODO: idk if you should use this function I made earlier or the other one.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "OnClickListener :" +
                                "\nSpinner : "+ String.valueOf(spinner.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }
        });
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
        //TODO: HOnestly idk what the fuck to do with this.
        void onFragmentInteraction(Uri uri);
    }

    //add the items to the spinner
    public void addItemsOnSpinner() {
        spinner = (Spinner) getView().findViewById(R.id.spinner);
        
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, mCategories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(dataAdapter);
    }

    //add listeners
    public void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner) getView().findViewById(R.id.spinner);
       // spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    public void addListenerOnTextEdit() {
        totext = (EditText) getView().findViewById(R.id.todate);
        fromtext = (EditText) getView().findViewById(R.id.fromdate);

        totext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //TODO: Figure out logic for what should be done when text is edited
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //TODO: Figure out logic for what should be done when text is edited
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //TODO: Figure out logic for what should be done when text is edited
            }
        });

        fromtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //TODO: Figure out logic for what should be done when text is edited
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //TODO: Figure out logic for what should be done when text is edited
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //TODO: Figure out logic for what should be done when text is edited
            }
        });
        }
}
