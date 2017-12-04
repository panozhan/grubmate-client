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

import static android.R.attr.type;

public class NetworkManager extends Thread {
    private Parser parser = new Parser();
    private NewsFeedFragment newsfeed;

    public NetworkManager(NewsFeedFragment f) {
        System.out.println("setting fragment");
        newsfeed = f;
        if (newsfeed == null) {
            System.out.println("My Fragment is null =(");
        }
    }

    public NetworkManager() {
    }

    public void getGroups() {
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

        public SendGroup(Group group) {
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
                groupJson.put("name", group.getName());
                groupJson.put("users", friendsJson);

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
    public void postPost(Post post) {
        PostPost postPostObj = new PostPost(post);
        postPostObj.execute();
    }

    //gets a string of post ids for the user with the id
    public void getPostsForUser(String userId) {
        UserSingleton.getUserInstance().getPosts().clear();
        System.out.println("Calling1");
        GetPostsForUsers task = new GetPostsForUsers(newsfeed);
        task.execute();
    }

    //gets a single post from the server
    public void getPost(String postId) {
        if (this.newsfeed == null) {
            System.out.println("FUCK NEWSFEED NULL SDFEWREQWRRQWERWQEWQ");
        }
        GetSinglePost getSinglePost = new GetSinglePost(postId, newsfeed);
        getSinglePost.execute();

    }

    public void getNotifications(String userid, NewsFragment f) {
        GetNotifications myGetNotification = new GetNotifications(userid, f);
        myGetNotification.execute();
    }

    public void getUser(ProfileFragment f, String id) {
        GetUser myuser = new GetUser(f, id);
        myuser.execute();
    }

    private class GetUser extends AsyncTask<String, Void, Void> {
        ProfileFragment f;
        String userid;

        public GetUser(ProfileFragment f, String userid) {
            this.f = f;
            this.userid = userid;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                URL url = new URL("https://grubmateteam3.herokuapp.com/api/user?userid=" + userid);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                System.out.println("Convert" + parser.convertStreamToString(is));

                JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
                User user = parser.parseUser(reader);


                // f.generate(user);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }


    private class GetNotifications extends AsyncTask<String, Void, Void> {
        String userid;
        Parser parser = new Parser();
        NewsFragment f;

        public GetNotifications(String userid, NewsFragment f) {
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

                for (String id : owner.getNotificationIds()) {
                    System.out.println(id);
                    URL url1 = new URL("https://grubmateteam3.herokuapp.com/api/singlenotif?notifid=" + id);
                    HttpURLConnection urlConnection1 = (HttpURLConnection) url1.openConnection();
                    urlConnection1.setRequestMethod("GET");
                    urlConnection1.connect();

                    notifications.add(parser.parseNotification(urlConnection1.getInputStream()));
                }

                owner.setAllNotifications(notifications);
                //f.notifyChange();

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

        public PostPost(Post post) {
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
                postJson.put("tag",post.getTag());
                postJson.put("numAvailable",post.getNumAvailable());
                postJson.put("user",userJson);
                postJson.put("startTime",post.getTimestart());
                postJson.put("endTime",post.getTimeend());
                postJson.put("description",post.getDescription());
                postJson.put("price",post.getPrice());
                postJson.put("kind",post.getKind());
                userJson.put("id",owner.get_id());

                if (post.getGroups() != null) {
                    postJson.put("groups", new JSONArray(post.getGroups()));
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
                Post newPost = new Parser().parsePost(is);
                post.set_id(newPost.get_id());
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

        public GetSinglePost(String id, NewsFeedFragment f) {
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
                Post p = parser.parsePost(is);
                if(p != null){
                    owner.getPosts().add(p);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetGroups extends AsyncTask<String, Void, Void> {
        String userid;

        public GetGroups(String userid) {
            this.userid = userid;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
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

        public GetPostsForUsers(NewsFeedFragment f) {
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
                for (int i = 0; i < postIds.size(); ++i) {
                    networkManager.getPost(postIds.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    //type = "request", "confirm", "end"
    public void editPost(String type, String personid, String postid, Post post) {
        EditPost myUpdatePost = new EditPost(type, personid, postid, post);
        myUpdatePost.execute();
    }

    private class EditPost extends AsyncTask<String, Void, Void> {
        String userid;
        String type;
        String postid;
        Post post;

        public EditPost(String type, String personid, String postid, Post post) {
            this.type = type;
            this.userid = personid;
            this.postid = postid;
            this.post = post;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                // This is getting the url from the string we passed in
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/posts?type=" + type + "&postid=" + postid + "&userid=" + userid);
                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("PUT");
                urlConnection.connect();

                JSONObject postJson = new JSONObject();
                postJson.put("title", post.getTitle());
                postJson.put("description", post.getDescription());
                postJson.put("location", post.getLocation());
                postJson.put("category", post.getCategory());
                postJson.put("tag", post.getDescription());
                postJson.put("numAvailable", post.getNumAvailable());
                JSONObject userJson = new JSONObject();
                userJson.put("id", userid);
                postJson.put("user", userJson);
                postJson.put("price", post.getPrice());
                postJson.put("timestart", post.getTimestart());
                postJson.put("timeend", post.getTimeend());

                if (post.getGroups() != null) {
                    postJson.put("groups", new JSONArray(post.getGroups()));
                }

                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                String jsonString = postJson.toString();
                writer.write(jsonString);
                System.out.println(jsonString);
                writer.flush();
                writer.close();

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
                bufferedReader.close();
                urlConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void addSubscription(String userid, Subscription sub) {
        AddSubscription addSub = new AddSubscription(userid, sub);
        addSub.execute();
    }

    private class AddSubscription extends AsyncTask<String, Void, Void> {
        Subscription sub;
        String userid;
        Parser parser = new Parser();
        SubViewFragment subView;

        public AddSubscription(String userid, Subscription sub) {
            this.userid = userid;
            this.sub = sub;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String address = "https://grubmateteam3.herokuapp.com/api/subs?userid=" + userid;
                JSONObject subJson = new JSONObject();
                subJson.put("subtype", sub.getType());
                subJson.put("value", sub.getValue());
                String requestBody = subJson.toString();

                URL url = new URL(address);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                if (!sub.getType().equals("") && !sub.getValue().equals("")) {
                    OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
                    writer.write(requestBody);
                    writer.flush();
                    writer.close();
                    outputStream.close();
                }

                InputStream inputStream;
                // get stream
                if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = urlConnection.getInputStream();
                } else {
                    inputStream = urlConnection.getErrorStream();
                }

                ArrayList<Subscription> updates = parser.parseSubscriptions(inputStream);
                ArrayList<Subscription> newSubs = new ArrayList<>();
                for (Subscription s : updates) {
                    if (!s.getType().equals("") && !s.getValue().equals("")) {
                        newSubs.add(s);
                    }
                }
                UserSingleton.getUserInstance().setSubscriptions(newSubs);
                for (int i = 0; i < newSubs.size(); i++) {
                    System.out.println(newSubs.get(i));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void deleteSubscription(int index) {
        DeleteSubscription deleteSub = new DeleteSubscription(index);
        deleteSub.execute();
    }

    private class DeleteSubscription extends AsyncTask<String, Void, Void> {
        UserSingleton owner = UserSingleton.getUserInstance();
        Parser parser = new Parser();
        int index;

        public DeleteSubscription(int index) {
            this.index = index;
            System.out.println(index);
            //owner.removeSubscription(index);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url2 = new URL("https://grubmateteam3.herokuapp.com/api/subs?userid=" + owner.get_id() + "&index=" + String.valueOf(index));
                // Create the urlConnection
                HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
                urlConnection2.setDoOutput(true);
                urlConnection2.setRequestMethod("PUT");
                urlConnection2.connect();

                InputStream is = urlConnection2.getInputStream();
                System.out.println(type + " delete subs with put: " + parser.convertStreamToString(is));
                is.close();
                urlConnection2.connect();
                /*

                String address = "https://grubmateteam3.herokuapp.com/api/subs?userid=" + owner.get_id();
                URL url = new URL(address);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");

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

                ArrayList<Subscription> updates = parser.parseSubscriptions(inputStream);
                ArrayList<Subscription> newSubs = new ArrayList<>();
                int index = 0;
                for (Subscription sub : updates) {
                    if (index != this.index) {
                        newSubs.add(sub);
                    }
                    index++;
                }
                owner.setSubscriptions(newSubs);
                for (int i = 0; i < newSubs.size(); i++) {
                    System.out.println(newSubs.get(i));
                }
                */

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public void searchPostsForUser(String userId, String keyword) {
        UserSingleton.getUserInstance().getPosts().clear();
        SearchPostsForUser task = new SearchPostsForUser(newsfeed, keyword);
        task.execute();
    }

    public void getSearchPost(String postId, String keyword) {
        GetSearchPost getSinglePost = new GetSearchPost(postId, newsfeed, keyword);
        getSinglePost.execute();
    }

    private class GetSearchPost extends AsyncTask<String, Void, Post> {
        NewsFeedFragment newsfeed;
        Parser parser = new Parser();
        UserSingleton owner = UserSingleton.getUserInstance();
        String postId, category;
        String keyword;
        Post post;

        public GetSearchPost(String id, NewsFeedFragment f, String keyword) {
            postId = id;
            newsfeed = f;
            this.keyword = keyword;
        }

        @Override
        protected Post doInBackground(String... params) {
            try {
                // This is getting the url from the string we passed in
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/singlepost?postid=" + postId);
                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                post = parser.parsePost(is);

                return post;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Post post) {
            super.onPostExecute(post);
            if (post == null) {}
            else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(post.getTitle());
                list.add(post.getDescription());
                list.add(post.getCategory());
                list.add(post.getTag());
                list.add(post.getLocation());
                list.add(post.getTimestart());
                list.add(post.getTimeend());
                list.add(post.getGroupName());
                list.add(post.getKind());

                for (String s : list) {
                    if (s == null) {

                    } else {
                        if (s.toLowerCase().contains(keyword.toLowerCase())) {
                            System.out.println(s);

                            // TODO CHECK FOR DUPLICATED POST
                            owner.getPosts().add(post);
                            if (newsfeed != null) newsfeed.notifyChange();
                        }
                    }
                }
            }
        }
    }

    private class SearchPostsForUser extends AsyncTask<String, Void, Void> {
        Parser parser = new Parser();
        UserSingleton owner = UserSingleton.getUserInstance();
        NewsFeedFragment newsfeed;
        NetworkManager networkManager;
        String key;

        public SearchPostsForUser(NewsFeedFragment f, String keyword) {
            UserSingleton.getUserInstance().getPosts().clear();
            newsfeed = f;
            networkManager = new NetworkManager(newsfeed);
            key = keyword;
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
                for (int i = 0; i < postIds.size(); ++i) {
                    networkManager.getSearchPost(postIds.get(i), key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    //gets a string of post ids for the user with the id
    public void getFilteredPostsForUser(String category,
                                        int fromYear, int fromMonth, int fromDay, int fromHour, int fromMin,
                                        int toYear, int toMonth, int toDay, int toHour, int toMin) {

        UserSingleton.getUserInstance().getPosts().clear();
        FilterPostsForUsers task = new FilterPostsForUsers(newsfeed, category,
                fromYear,  fromMonth,  fromDay,  fromHour,  fromMin,
                toYear,  toMonth,  toDay,  toHour,  toMin);
        task.execute();
    }

    //gets a single post from the server
    public void getFilteredPost(String postId, String category,
                                int fromYear, int fromMonth, int fromDay, int fromHour, int fromMin,
                                int toYear, int toMonth, int toDay, int toHour, int toMin) {
        if (this.newsfeed == null) {
            System.out.println("FUCK NEWSFEED NULL SDFEWREQWRRQWERWQEWQ");
        }
        GetSingleFilteredPost getSinglePost = new GetSingleFilteredPost(postId, newsfeed, category,
                fromYear,  fromMonth,  fromDay,  fromHour,  fromMin,
                toYear,  toMonth,  toDay,  toHour,  toMin);
        getSinglePost.execute();

    }

    private class GetSingleFilteredPost extends AsyncTask<String, Void, Post> {
        NewsFeedFragment newsfeed;
        Parser parser = new Parser();
        UserSingleton owner = UserSingleton.getUserInstance();
        String postId, category;
        int fromYear,  fromMonth,  fromDay,  fromHour,  fromMinute;
        int toYear,  toMonth,  toDay,  toHour,  toMinute;
        Post post;

        public GetSingleFilteredPost(String id, NewsFeedFragment f, String category,
                                     int fromYear, int fromMonth, int fromDay, int fromHour, int fromMin,
                                     int toYear, int toMonth, int toDay, int toHour, int toMin) {
            postId = id;
            newsfeed = f;
            this.category = category;

            // from
            this.fromYear = fromYear;
            this.fromMonth = fromMonth;
            this.fromDay = fromDay;
            this.fromHour = fromHour;
            this.fromMinute = fromMin;

            // to
            this.toYear = toYear;
            this.toMonth = toMonth;
            this.toDay = toDay;
            this.toHour = toHour;
            this.toMinute = toMin;
        }

        @Override
        protected Post doInBackground(String... params) {
            try {
                // This is getting the url from the string we passed in
                URL url = new URL("https://grubmateteam3.herokuapp.com/api/singlepost?postid=" + postId);
                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream is = urlConnection.getInputStream();
                post = parser.parsePost(is);
                return post;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Post post) {
            super.onPostExecute(post);

            if (post == null) { }
            else {
                // filter by category and time
                //System.out.println("fromfiltertime: ");
                //System.out.println("tofiltertime: ");
                if (fromHour != -1 && fromMinute != -1 && toHour != -1 && toMinute != -1) {
                    if ("".equals(post.getTimestart()) || "".equals(post.getTimeend())) {
                    /*
                    if (post.getCategory() == null) {}
                    else {
                        if (post.getCategory().toLowerCase().equals(category.toLowerCase())) {
                            owner.getPosts().add(post);
                            if (newsfeed != null) {
                                newsfeed.notifyChange();
                            }
                        }
                    }
                    */
                    }
                    else {
                        //String[] dateSplitStart = post.getTimestart().split("/");
                        System.out.println("post.getTimestart() " +  post.getTimestart());
                        System.out.println("post.getTimeend() " +  post.getTimeend());
                        int mStart = Integer.valueOf(post.getTimestart().substring(0,2));
                        int dStart = Integer.valueOf(post.getTimestart().substring(3,5));
                        int yStart = Integer.valueOf(post.getTimestart().substring(6,8)) + 2000;

                        //String[] dateSplitEnd = post.getTimeend().split("/");
                        int mEnd = Integer.valueOf(post.getTimeend().substring(0,2));
                        int dEnd = Integer.valueOf(post.getTimeend().substring(3,5));
                        int yEnd = Integer.valueOf(post.getTimeend().substring(6,8)) + 2000;

                        String[] splitStart = post.getTimestart().split(":");
                        int hourStart = Integer.valueOf(splitStart[1]);
                        int minStart = Integer.valueOf(splitStart[2]);

                        String[] splitEnd = post.getTimeend().split(":");
                        int hourEnd = Integer.valueOf(splitEnd[1]);
                        int minEnd = Integer.valueOf(splitEnd[2]);

                        if (post.getCategory() == null || "".equals(post.getCategory())) {}
                        else {
                            if (post.getCategory().toLowerCase().equals(category.toLowerCase())) {
                                // conditions
                                System.out.println("filtertimestartNWWWW: " + yStart  + " year "+ mStart + " month "+ dStart + " day "
                                        + hourStart  + " hour "+ minStart + " minute ");
                                System.out.println("filtertimetoNWWWW: " + yEnd  + " year "+ mEnd + " month "+ dEnd + " day "
                                        + hourEnd  + " hour "+ minEnd + " minute ");

                                long postStart = (long)yStart*100000000 + (long)mStart*1000000 + (long)dStart*10000
                                        + (long)hourStart*100 + (long)minStart;
                                long postEnd = (long)yEnd*100000000 + (long)mEnd*1000000 + (long)dEnd*10000 +
                                        (long)hourEnd*100 + (long)minEnd;

                                long rangeStart = (long)fromYear*100000000 + (long)fromMonth*1000000 + (long)fromDay*10000
                                        + (long)fromHour*100 + (long)fromMinute;
                                long rangeEnd = (long)toYear*100000000 + (long)toMonth*1000000 + (long)toDay*10000
                                        + (long)toHour*100 + (long)toMinute;

                                System.out.println("rangeStart: " + rangeStart);
                                System.out.println("rangeEnd: " + rangeEnd);
                                System.out.println("postStart: " + postStart);
                                System.out.println("postEnd: " + postEnd);

                                boolean startWithinRange = (postStart>=rangeStart && postStart<=rangeEnd);
                                boolean endWithinRange = (postEnd>=rangeStart && postEnd<=rangeEnd);

                                System.out.println("startWithinRange: " + startWithinRange);
                                System.out.println("endWithinRange: " + endWithinRange);

                                // include if either start of end time of post lines within range
                                if (startWithinRange || endWithinRange) {

                                    owner.getPosts().add(post);
                                    if (newsfeed != null) {
                                        newsfeed.notifyChange();
                                    }
                                }
                            }
                        }
                    }
                }
                // filter by category only if there's no time
                else {
                    if (post.getCategory() == null) {}
                    else {
                        if (post.getCategory().toLowerCase().equals(category.toLowerCase())) {
                            System.out.println(post.getCategory());
                            owner.getPosts().add(post);
                            if (newsfeed != null) {
                                newsfeed.notifyChange();
                            }
                        }
                    }
                }
            }

        }

    }

    private class FilterPostsForUsers extends AsyncTask<String, Void, Void> {
        Parser parser = new Parser();
        UserSingleton owner = UserSingleton.getUserInstance();
        NewsFeedFragment newsfeed;
        NetworkManager networkManager;
        String category;
        int fromYear,  fromMonth,  fromDay,  fromHour,  fromMinute;
        int toYear,  toMonth,  toDay,  toHour,  toMinute;

        public FilterPostsForUsers(NewsFeedFragment f, String category,
                                   int fromYear, int fromMonth, int fromDay, int fromHour, int fromMin,
                                   int toYear, int toMonth, int toDay, int toHour, int toMin) {
            newsfeed = f;
            networkManager = new NetworkManager(newsfeed);
            this.category = category;

            // from
            this.fromYear = fromYear;
            this.fromMonth = fromMonth;
            this.fromDay = fromDay;
            this.fromHour = fromHour;
            this.fromMinute = fromMin;

            // to
            this.toYear = toYear;
            this.toMonth = toMonth;
            this.toDay = toDay;
            this.toHour = toHour;
            this.toMinute = toMin;
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
                for (int i = 0; i < postIds.size(); ++i) {
                    networkManager.getFilteredPost(postIds.get(i), category,
                            fromYear,  fromMonth,  fromDay,  fromHour,  fromMinute,
                            toYear,  toMonth,  toDay,  toHour,  toMinute);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
