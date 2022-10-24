package Order;

import java.util.List;

public class ListIngredients {
    private boolean success;
    private List<Ingredient> data;

    public ListIngredients() {
    }

    public ListIngredients(boolean success, List<Ingredient> data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Ingredient> getIngredients() {
        return data;
    }

    public void setIngredients(List<Ingredient> data) {
        this.data = data;
    }
}
