package notification;

import java.util.ArrayList;

/**
 * Created by Alex Pan on 11/18/2017.
 */

public class NotifStore {

    private static NotifStore notifStore = null;

    public static NotifStore getInstance(){
        if(notifStore == null){
            notifStore = new NotifStore();
        }

        return notifStore;
    }

    private NotifStore(){

    }

    public void clear(){
        news.clear();
        requests.clear();
        statuses.clear();
    }

    private ArrayList<News> news = new ArrayList<>();
    private ArrayList<Requests> requests = new ArrayList<>();
    private ArrayList<Statusz> statuses = new ArrayList<>();

    public ArrayList<News> getNews() {
        return news;
    }

    public void setNews(ArrayList<News> news) {
        this.news = news;
    }

    public ArrayList<Requests> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Requests> requests) {
        this.requests = requests;
    }

    public ArrayList<Statusz> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Statusz> statuses) {
        this.statuses = statuses;
    }
}
