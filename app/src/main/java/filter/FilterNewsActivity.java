package filter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.udacity.test.R;

import objects.CustomOnItemSelectedListener;
import objects.NetworkManager;
import objects.UserSingleton;

/**
 * Created by Lauren on 10/23/2017.
 */

public class FilterNewsActivity extends AppCompatActivity{

    private Spinner spinner;
    private Button button;
    private EditText totext;
    private EditText fromtext;
    private String totextstring; //place to store the info from the edit text
    private String fromtextstring; //place to store the info from the edit text
    private String currentspinner;
    private UserSingleton owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_news);

        setTitle("Filter");

        owner = UserSingleton.getUserInstance();

        addItemsOnSpinner();
        addListenerOnSpinnerItemSelection();
        addListenerOnButton();
        addListenerOnTextEdit();
    }

    //add the items to the spinner
    public void addItemsOnSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter;
        String[] list = getResources().getStringArray(R.array.categories);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(dataAdapter);
    }

    public void filterNews(){
        //get current value of spinner
        currentspinner = String.valueOf(spinner.getSelectedItem());
        totextstring = totext.getText().toString();
        fromtextstring = fromtext.getText().toString();
        NetworkManager networkManager = new NetworkManager();
        networkManager.getFilteredPostsForUser(owner.get_id(), currentspinner, fromtextstring, totextstring);
    }

    //add listeners
    public void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void addListenerOnButton() {
        spinner = (Spinner) findViewById(R.id.spinner);
        button = (Button) findViewById(R.id.filterbutton);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FilterNewsActivity.this, "OnClickListener :" + "\nSpinner : "+ String.valueOf(spinner.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();

                filterNews();
                finish();
            }

        });
    }

    public void addListenerOnTextEdit() {
        totext = (EditText) findViewById(R.id.todate);
        fromtext = (EditText) findViewById(R.id.fromdate);

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
