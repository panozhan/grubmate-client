package objects;

import android.os.AsyncTask;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import content.NewsFeedFragment;
import notification.NewsFragment;
import profile.ProfileFragment;
import subscription.SubViewFragment;

public class NetworkManager extends Thread {
    private Parser parser = new Parser();
    private NewsFeedFragment newsfeed;

    public NetworkManager(NewsFeedFragment f){
        System.out.println("setting fragment");
        newsfeed = f;
        if(newsfeed == null){
            System.out.println("My Fragment is null =(");
        }
    }

    public NetworkManager(){
    }

    public void getGroups(){
        GetGroups myclass = new GetGroups(UserSingleton.getUserInstance().get_id());
        myclass.execute();
    }
    
    public void sendGroup(Group group) {
        SendGroup sendGroupObj = new SendGroup(group);
        sendGroupObj.execute();
    }
    
    private class SendGroup extends AsyncTask<String, Void, Void> {
        Group group;
        UserSingleton owner = UserSingleton.getUserInstance();
        private int groupId;
        public SendGroup(Group group){
            this.group = group;
            owner.addGroup(group);
        }
        
        @Override
        protected Void doInBackground(String... params) {
            try {
                // This is getting the url from the string we passed in
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/group");
                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("GET");
                // urlConnection.setRequestMethod("POST");
                urlConnection.connect();
                
                // Send the post body
                JSONObject groupJson = new JSONObject();
                //JSONObject userJson = new JSONObject();
                JSONArray friendsJson = new JSONArray(group.getUsers());
                
                // making json object
                groupJson.put("name",group.getName());
                groupJson.put("users",friendsJson);
                
                // write to server
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                String jsonString = groupJson.toString();
                writer.write(jsonString);
                System.out.println(jsonString);
                writer.flush();
                writer.close();
                
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

    //packages post object into a json file, and uses POST method to send to server
    //receives no response
    public void postPost(Post post){
        PostPost postPostObj = new PostPost(post);
        postPostObj.execute();
    }

    //gets a string of post ids for the user with the id
    public void getPostsForUser(String userId){
        UserSingleton.getUserInstance().getPosts().clear();
        System.out.println("Calling1");
        GetPostsForUsers task = new GetPostsForUsers(newsfeed);
        task.execute();
    }

    //gets a single post from the server
    public void getPost(String postId){
        if(this.newsfeed == null){
            System.out.println("FUCK NEWSFEED NULL SDFEWREQWRRQWERWQEWQ");
        }
        GetSinglePost getSinglePost = new GetSinglePost(postId,newsfeed);
        getSinglePost.execute();

    }

    //type = "request", "confirm", "end"
    public void updatePost(String type, String personid,String postid, String loc){
        System.out.println("hello");
        UpdatePost myUpdatePost = new UpdatePost(type,personid,postid, loc);
        myUpdatePost.execute();
    }

    public void getNotifications(String userid,NewsFragment f){
        GetNotifications myGetNotification = new GetNotifications(userid,f);
        myGetNotification.execute();
    }
    public void getUser(ProfileFragment f, String id){
        GetUser myuser = new GetUser(f, id);
        myuser.execute();
    }

    private class GetUser extends AsyncTask<String, Void, Void>{
        ProfileFragment f;
        String userid;
        public GetUser(ProfileFragment f, String userid){
            this.f = f;
            this.userid = userid;
        }

        @Override
        protected Void doInBackground(String... params) {
            try{

                URL url = new URL("https://grubmateteam3.herokuapp.com/api/user?userid="+userid);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                 System.out.println("Convert"+parser.convertStreamToString(is));

                JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
                User user = parser.parseUser(reader);


               // f.generate(user);

            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

    }


    private class GetNotifications extends AsyncTask<String,Void,Void>{
        String userid;
        Parser parser = new Parser();
        NewsFragment f;
        public GetNotifications(String userid, NewsFragment f){
            this.userid = userid;
            this.f = f;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                // This is getting the url from the string we passed in
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/notifications?userid=" + userid);
                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                UserSingleton owner = UserSingleton.getUserInstance();
                owner.setNotificationIds(parser.parseStringArrayJson(is));

                ArrayList<Notification> notifications = new ArrayList<Notification>();

                for(String id : owner.getNotificationIds()){
                    System.out.println(id);
                    URL url1 = new URL("https://grubmateteam3.herokuapp.com/api/singlenotif?notifid=" + id);
                    HttpURLConnection urlConnection1 = (HttpURLConnection) url1.openConnection();
                    urlConnection1.setRequestMethod("GET");
                    urlConnection1.connect();

                    notifications.add(parser.parseNotification(urlConnection1.getInputStream()));
                }

                owner.setAllNotifications(notifications);
                f.notifyChange();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class PostPost extends AsyncTask<String, Void, Void> {
        Post post;
        UserSingleton owner = UserSingleton.getUserInstance();
        Parser parser = new Parser();
        private String postId;
        public PostPost(Post post){
            this.post = post;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                // This is getting the url from the string we passed in
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/posts");
                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("GET");
               // urlConnection.setRequestMethod("POST");
                urlConnection.connect();
                // Send the post body
                JSONObject postJson = new JSONObject();
                JSONObject userJson = new JSONObject();
                postJson.put("location",post.getLocation());
                postJson.put("title",post.getTitle());
                postJson.put("category",post.getCategory());
                postJson.put("tag",post.getDescription());
                postJson.put("numAvailable",post.getNumAvailable());
                postJson.put("user",userJson);
                postJson.put("location",post.getDescription());
                postJson.put("price",post.getPrice());

                userJson.put("id",owner.get_id());

                if(post.getGroups() != null){
                    postJson.put("groups",new JSONArray(post.getGroups()));
                }


                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                String jsonString = postJson.toString();
                writer.write(jsonString);
                System.out.println(jsonString);
                writer.flush();
                writer.close();

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

    private class GetSinglePost extends AsyncTask<String, Void, Void> {
        NewsFeedFragment newsfeed;
        Parser parser = new Parser();
        UserSingleton owner = UserSingleton.getUserInstance();
        private String postId;
        public GetSinglePost(String id,NewsFeedFragment f){
            postId = id;
            newsfeed = f;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                // This is getting the url from the string we passed in
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/singlepost?postid=" + postId);
                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                owner.getPosts().add(parser.parsePost(is));
                if(newsfeed != null){
                    newsfeed.notifyChange();
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetGroups extends  AsyncTask<String,Void,Void>{
        String userid;
        public GetGroups(String userid){
            this.userid = userid;
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
        /*        URL url = new URL("https://grubmateteam3.herokuapp.com/api/group?userid=" + userid);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                ArrayList<String> groupids = parser.parseStringArrayJson(is);
                UserSingleton owner = UserSingleton.getUserInstance();

                for(int i = 0; i < groupids.size(); ++i){
                    URL url2 = new URL("https://grubmateteam3.herokuapp.com/api/singlegroup?groupid=" + groupids.get(i));
                    HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
                    urlConnection2.connect();
                    owner.getGroups().add(parser.parseGroup(urlConnection2.getInputStream()));
                }
*/
            }catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class UpdatePost extends AsyncTask<String,Void,Void>{
        String personid;
        String type;
        String postid;
        String loc;
        public UpdatePost(String type,String personid, String postid, String loc){
            this.type = type;
            this.personid = personid;
            this.postid = postid;
            this.loc = loc;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                // This is getting the url from the string we passed in
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/posts?type=" + type + "&personid=" + 2 + "&postid=" + postid + "&location=" + loc);
                // Create the urlConnection
                System.out.println("https://grubmateteam3.herokuapp.com/api/posts?type=" + type + "&personid=" + personid + "&postid=" + postid + "&location=" + loc);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                System.out.println(parser.convertStreamToString(is));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetPostsForUsers extends AsyncTask<String, Void, Void> {
        Parser parser = new Parser();
        UserSingleton owner = UserSingleton.getUserInstance();
        NewsFeedFragment newsfeed;
        NetworkManager networkManager;
        public GetPostsForUsers(NewsFeedFragment f){
            UserSingleton.getUserInstance().getPosts().clear();
            newsfeed = f;
            networkManager = new NetworkManager(newsfeed);
        }
        @Override
        protected Void doInBackground(String... params) {
            try {
                System.out.println("Calling");
                // This is getting the url from the string we passed in
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/posts?userid=" + owner.get_id());
                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                ArrayList<String> postIds = parser.parseStringArrayJson(is);
                owner.setPostIds(postIds);
                for(int i = 0; i < postIds.size(); ++i){
                    networkManager.getPost(postIds.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
    
    public void addSubscription(String userid, Subscription sub){
        AddSubscription addSub = new AddSubscription(userid, sub);
        addSub.execute();
    }
    
    private class AddSubscription extends AsyncTask<String, Void, Void> {
        Subscription sub;
        String userid;
        Parser parser = new Parser();
        SubViewFragment subView;

        public AddSubscription(String userid, Subscription sub){
            this.userid = userid;
            this.sub = sub;
            //UserSingleton.getUserInstance().addSubscription(sub);
        }
        
        @Override
        protected Void doInBackground(String... params) {
            try {
                String address = "https://grubmateteam3.herokuapp.com/api/subs?userid=" + userid;
                JSONObject subJson = new JSONObject();
                subJson.put("subtype",sub.getType());
                subJson.put("value",sub.getValue());
                String requestBody = subJson.toString();

                URL url = new URL(address);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
                writer.write(requestBody);
                writer.flush();
                writer.close();
                outputStream.close();

                InputStream inputStream;
                // get stream
                if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = urlConnection.getInputStream();
                } else {
                    inputStream = urlConnection.getErrorStream();
                }

                UserSingleton owner = UserSingleton.getUserInstance();
                ArrayList<Subscription> updates = parser.parseSubscriptions(inputStream);
                ArrayList<Subscription> newSubs = new ArrayList<>();
                for (Subscription s : updates) {
                    if (!s.getType().equals("") && !s.getValue().equals("")) {
                        newSubs.add(s);
                    }
                }
                owner.setSubscriptions(newSubs);
                for (int i = 0; i < newSubs.size(); i++) {
                    System.out.println(newSubs.get(i));
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    
    public void deleteSubscription(int index){
        DeleteSubscription deleteSub = new DeleteSubscription(index);
        deleteSub.execute();
    }
    
    private class DeleteSubscription extends AsyncTask<String, Void, Void> {
        UserSingleton owner = UserSingleton.getUserInstance();
        Parser parser = new Parser();
        int index;
        
        public DeleteSubscription(int index){
            this.index = index;
            owner.removeSubscription(index);
        }
        
        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/subs?index=" + index + "&userid=" + owner.get_id());
                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream;
                // get stream
                if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = urlConnection.getInputStream();
                } else {
                    inputStream = urlConnection.getErrorStream();
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = "", temp = "";
                while ((temp = bufferedReader.readLine()) != null) {
                    s += temp;
                }
                System.out.println(s);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        
    }

}
