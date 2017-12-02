package login_account;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import content.ContentActivity;
import group.GroupParser;
import others.FontHelper;
import com.example.udacity.test.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import objects.UserSingleton;

public class AccountActivity extends AppCompatActivity {

    private static final String TAG = "AccountActivity";

    ProfileTracker profileTracker;
    ImageView profilePic;
    TextView id;
    TextView infoLabel;
    TextView info;
    TextView locationLabel;
    TextView location;
    Button gotonewsFeed;
    Context context = this;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        FontHelper.setCustomTypeface(findViewById(R.id.view_root));

        profilePic = (ImageView) findViewById(R.id.profile_image);
        id = (TextView) findViewById(R.id.id);
        infoLabel = (TextView) findViewById(R.id.info_label);
        info = (TextView) findViewById(R.id.info);
        locationLabel = (TextView) findViewById(R.id.location_label);
        location = (TextView) findViewById(R.id.location);
        callbackManager = CallbackManager.Factory.create();
        gotonewsFeed = (Button)findViewById(R.id.gotoapp);

        // register a receiver for the onCurrentProfileChanged event
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged (Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    displayProfileInfo(currentProfile);
                }
            }
        };

        gotonewsFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,ContentActivity.class);
                startActivity(i);
            }
        });
        if (AccessToken.getCurrentAccessToken() != null) {
            // If there is an access token then Login Button was used
            // Check if the profile has already been fetched
            Profile currentProfile = Profile.getCurrentProfile();
            if (currentProfile != null) {
                displayProfileInfo(currentProfile);
            }
            else {
                // Fetch the profile, which will trigger the onCurrentProfileChanged receiver
                Profile.fetchProfileForCurrentAccessToken();
            }
        }
        else {
            // Otherwise, get Account Kit login information
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    // get Account Kit ID
                    String accountKitId = account.getId();
                    id.setText(accountKitId);

                    PhoneNumber phoneNumber = account.getPhoneNumber();
                    if (account.getPhoneNumber() != null) {
                        // if the phone number is available, display it
                        String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                        info.setText(formattedPhoneNumber);
                        infoLabel.setText(R.string.phone_label);
                    }
                    else {
                        // if the email address is available, display it
                        String emailString = account.getEmail();
                        info.setText(emailString);
                        infoLabel.setText(R.string.email_label);
                    }

                }

                @Override
                public void onError(final AccountKitError error) {
                    String toastMessage = error.getErrorType().getMessage();
                    Toast.makeText(AccountActivity.this, toastMessage, Toast.LENGTH_LONG).show();
                }
            });
        }

        FriendsParser friendsParser = new FriendsParser();
        friendsParser.setFriendsForOwner();

        //TODO: Uncomment this when blocked user works properly in the server shit
        //BlockedParser blockedParser = new BlockedParser();
        //blockedParser.setFriendsForOwner();

        GroupParser groupParser = new GroupParser();
        groupParser.getGroupForOwner();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // unregister the profile tracker receiver
        profileTracker.stopTracking();
    }

    public void onLogout(View view) {
        // logout of Account Kit
        AccountKit.logOut();
        // logout of Login Button
        LoginManager.getInstance().logOut();

        launchLoginActivity();
    }

    private void displayProfileInfo(Profile profile) {
        // check for required permissions
        Set permissions = AccessToken.getCurrentAccessToken().getPermissions();
        if (permissions.contains("user_friends")) {
            fetchFriends();
        }
        else {
            // prompt user to grant permissions
            LoginManager loginManager = LoginManager.getInstance();
            loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    fetchFriends();
                }

                @Override
                public void onCancel() {
                    // inform user that permission is required
                    String permissionMsg = getResources().getString(R.string.permission_message);
                    Toast.makeText(AccountActivity.this, permissionMsg, Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onError(FacebookException error) {}
            });
            loginManager.logInWithReadPermissions(this, Arrays.asList("user_friends"));
        }

        if (permissions.contains("user_location")){
            fetchLocation();
        } else{
            LoginManager loginManager = LoginManager.getInstance();
            loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    fetchLocation();
                }

                @Override
                public void onCancel() {
                    String permissionMsg = getResources().getString(R.string.location_permission_message);
                    Toast.makeText(AccountActivity.this, permissionMsg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException error) {

                }
            });
            loginManager.logInWithReadPermissions(this, Arrays.asList("user_location"));
        }

        UserSingleton owner = UserSingleton.getUserInstance();

        //get Profile ID
        String profileId = profile.getId();
        id.setText(profileId);
        owner.set_id(profileId);

        //display the Profile name
        String name = profile.getName();
        info.setText(name);
        infoLabel.setText(R.string.name_label);
        owner.setName(name);

        //display the profile picture
        Uri profilePicUri = profile.getProfilePictureUri(100, 100);
        displayProfilePic(profilePicUri);

    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private String formatPhoneNumber(String phoneNumber) {
        // helper method to format the phone number for display
        try {
            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn = pnu.parse(phoneNumber, Locale.getDefault().getCountry());
            phoneNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        }
        catch (NumberParseException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

    private void displayProfilePic(Uri uri) {
        // helper method to load the profile pic in a circular imageview
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(AccountActivity.this)
                .load(uri)
                .transform(transformation)
                .into(profilePic);
    }

    private void fetchFriends() {
        // make the API call to fetch friends list
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,friends");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me", parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if (response.getError() != null) {
                            // display error message
                            Toast.makeText(AccountActivity.this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        // parse json data
                        JSONObject jsonResponse = response.getJSONObject();
                        //System.out.println(jsonResponse.toString());

                        try {
                            JSONObject post = new JSONObject();
                            post.put("name", jsonResponse.getString("name"));
                            post.put("_id", jsonResponse.getString("id"));

                            JSONObject friends = jsonResponse.getJSONObject("friends");
                            JSONArray data = friends.getJSONArray("data");
                            ArrayList<String> test2 = new ArrayList<String>();
                            test2.add("2");
                            JSONArray test = new JSONArray(test2);

                            post.put("friends", test);

                            PostJSONFromAPI task = new PostJSONFromAPI(post.toString());
                            task.execute("https://grubmateteam3.herokuapp.com/api/user");
                            System.out.println(post.toString());
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void fetchLocation(){
        Bundle parameters = new Bundle();
        parameters.putString("fields", "location");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response.getError() != null) {
                            // display error message
                            Toast.makeText(AccountActivity.this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }

                        JSONObject jsonResponse = response.getJSONObject();
                        try {
                            JSONObject locationObj = jsonResponse.getJSONObject("location");
                            String locationStr = locationObj.getString("name");
                            System.out.println(locationStr);
                            location.setText(locationStr);

                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    public void launch(){

    }

    private class PostJSONFromAPI extends AsyncTask<String, Void, Void> {
        String postData;
        public PostJSONFromAPI(String json) {
            if (json != null) {
                this.postData = json;
            }
        }
        @Override
        protected Void doInBackground(String... params) {
            try {
                // This is getting the url from the string we passed in
                URL url = new URL(params[0]);
                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Sets an authorization header
                //urlConnection.setRequestProperty("Authorization", "someAuthString");
                // Send the post body
                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write(postData);
                    System.out.println(postData);
                    writer.flush();
                    writer.close();
                }

                //Provides a readable source of bytes
                InputStream is = urlConnection.getInputStream();
                //Wrap InputStream with InputStreamReader
                //Input stream of bytes is converted to stream of characters
                //Buffer reading operation to improve efficiency
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                //Read all characters into String data
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                System.out.println(response.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
