package objects;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import profile.Transaction;


public class Parser {

    private ArrayList<String> parseStrings(JsonReader reader) throws IOException{
        ArrayList<String> result = new ArrayList<String>();
        reader.beginArray();
        while (reader.hasNext()) {
            String x = reader.nextString();
            System.out.println(x);
            result.add(x);
        }
        reader.endArray();
        return result;
    }

    public ArrayList<String> parseStringArrayJson(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
       // System.out.println(convertStreamToString(in));
        return parseStrings(reader);
    }

    public String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public Post parsePost(InputStream in) throws IOException {
        String x = convertStreamToString(in);
        if(x.equals("null")) {
            return null;
        } else {
            System.out.println("fullpostobjfromserver: "+x);
        }
        InputStream stream = new ByteArrayInputStream(x.getBytes(StandardCharsets.UTF_8.name()));

        JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));

        Post result = new Post();
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            switch (name){
                case "numAvailable":
                    result.setNumAvailable(reader.nextInt());
                    break;
                case "kind":
                    result.setKind(reader.nextString());
                    break;
                case "_id":
                    result.set_id(reader.nextString());
                    break;
                case "title":
                    result.setTitle(reader.nextString());
                    break;
                case "category":
                    result.setCategory(reader.nextString());
                    break;
                case "tag":
                    result.setTag(reader.nextString());
                    break;
                case "startTime":
                    result.setTimestart(reader.nextString());
                    break;
                case "endTime":
                    result.setTimeend(reader.nextString());
                    break;
                case "location":
                    result.setLocation(reader.nextString());
                    break;
                case "price":
                    result.setPrice(reader.nextString());
                    break;
                case "description":
                    result.setDescription(reader.nextString());
                    break;
                case "groups":
                    reader.beginArray();
                    String groupID = "";
                    while(reader.hasNext()){
                        groupID = reader.nextString();
                    }
                    //System.out.println("parsergroupid: "+groupID);
                    reader.endArray();
                    result.setGroupByID(groupID);
                    break;
                case "user":
                    User user = new User();
                    reader.beginObject();
                    while(reader.hasNext()){
                        String nestedName = reader.nextName();
                        if(nestedName.equals("id")){
                            user.setId(reader.nextString());
                        }else{
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                    result.setUser(user);
                    break;
                case "transactions":
                    System.out.println("in transact case");
                    result.setEditable(true);
                    reader.beginArray();
                    while(reader.hasNext()){
                        System.out.println("read a transaction rip");
                        reader.skipValue();
                        result.setEditable(false);
                    }
                    reader.endArray();
                    System.out.println(result.isEditable());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();
        return result;
    }

    public User parseUser(JsonReader reader) throws IOException {
        User result = new User();
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            switch (name){
                case "name":
                    result.setName(reader.nextString());
                    break;
                case "_id":
                    result.setId(reader.nextString());
                    break;
                case "posts":
                    result.setPosts(parseStrings(reader));
                    break;
                case "rating":
                    result.setRating(Float.valueOf(reader.nextString()));
            }
        }
        reader.endObject();
        return result;
    }


    public ArrayList<Transaction> parseTransactions(InputStream in){
        ArrayList<Transaction> result = new ArrayList<>();
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(in,"UTF-8"));
            reader.beginArray();
            while(reader.hasNext()){
                Transaction t = new Transaction();
                reader.beginObject();
                while(reader.hasNext()){
                    String name = reader.nextName();
                    switch (name){
                        case "types":
                            t.setType(reader.nextString());
                            break;
                        case "timeConfirmed":
                            t.setTimeConfirmed(reader.nextString());
                            break;
                        case "timeRequested":
                            t.setTimeRequested(reader.nextString());
                            break;
                        case "timeEnded":
                            t.setTimeEnded(reader.nextString());
                            break;
                        case "person":
                            t.setPerson(reader.nextString());
                            break;
                        case "postTitle":
                            t.setPostTitle(reader.nextString());
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
                reader.endObject();
                result.add(t);
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Transaction t : result){
            System.out.println(t.getPostTitle());
            System.out.println(t.getTimeEnded());
        }
        return result;
    }

    public Notification parseNotification(InputStream is) throws IOException{
        String jsonString = convertStreamToString(is);
        System.out.println(jsonString);
        Notification result = new Notification();
        try{
            JSONObject mainObject = new JSONObject(jsonString);
            result.setType(mainObject.getString("type"));
            if(mainObject.has("address")){
                result.setAddress(mainObject.getString("address"));
            }
            if(mainObject.has("status")){
                result.setStatus(mainObject.getString("status"));
            }
            if(mainObject.has("personid")){
                result.setPersonid(mainObject.getString("personid"));
            }
            result.setTitle(mainObject.getString("title"));
            result.setPostId(mainObject.getString("postid"));

        }catch (JSONException e){

        }

        return result;
    }

    public Group parseGroup(InputStream is) throws IOException{
        Group group = new Group();
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        while(reader.hasNext()){
            String name = reader.nextName();
            if(name.equals("name")){
                group.setName(reader.nextString());
            }else if(name.equals("_id")){
                group.setId(reader.nextString());
            }else if(name.equals("users")){
                group.setUsers(parseStrings(reader));
            }else{
                reader.skipValue();
            }
        }

        return group;
    }


    public User parseUser(InputStream is) throws IOException {
        String jsonString = convertStreamToString(is);
        User result = new User();
        try{
            JSONObject mainObject = new JSONObject(jsonString);
            result.setName(mainObject.getString("name"));
            result.setId(mainObject.getString("id"));
            ArrayList<String> array = new ArrayList<String>();
            JSONArray jsonArray = mainObject.getJSONArray("posts");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    array.add(jsonArray.get(i).toString());
                }
            }
            result.setPosts(array);
            result.setRating(Float.valueOf(mainObject.getString("rating")));

        }catch (JSONException e){

        }
        return result;
    }

    public String readFully(InputStream entityResponse) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = entityResponse.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString();
    }


    public ArrayList<Subscription> parseSubscriptions(InputStream inputStream) throws JSONException, IOException {
        ArrayList<Subscription> subs = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String s = "", temp = "";
        while ((temp = bufferedReader.readLine()) != null) {
            s += temp;
        }

        System.out.println(s);
        JSONArray jsonarray = new JSONArray(s);
        for (int i = 0; i < jsonarray.length(); i++) {
            Subscription result = new Subscription();
            try {
                JSONObject mainObject = jsonarray.getJSONObject(i);
                if (mainObject.has("subtype")){
                    result.setType(mainObject.getString("subtype"));
                }
                if (mainObject.has("value")){
                    result.setValue(mainObject.getString("value"));
                }
            } catch (JSONException e){

            }
            subs.add(result);
        }

        return subs;
    }
}

