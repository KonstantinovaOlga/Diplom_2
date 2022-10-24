package Order;

import java.util.ArrayList;
import java.util.List;

public class ListHashs {
    private List<String> ingredients;

    public ListHashs() {
        this.ingredients = new ArrayList<String>();
    }

    public ListHashs(List<String>  ingredients) {
        this.ingredients = ingredients;
    }

    public List<String>  getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String>  ingredients) {
        this.ingredients = ingredients;
    }

    public void addHash (String hash) {
        this.ingredients.add(hash);
    }
}
