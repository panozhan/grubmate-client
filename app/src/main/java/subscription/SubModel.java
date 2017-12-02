package subscription;

/**
 * Created by ArfanR on 11/10/17.
 */

public class SubModel {

    public String category, name, deleteTxt;
    public int position;

    SubModel(String category, String name) {
        this.category = category;
        this.name = name;
        this.deleteTxt = "Delete";
    }
}

