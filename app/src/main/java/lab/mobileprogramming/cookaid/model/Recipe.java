package lab.mobileprogramming.cookaid.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "Recipes")
public class Recipe implements Serializable {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static class Ingredient implements Serializable {
        private String name;
        private String measure;

        public Ingredient(String name, String measure) {
            this.name = name;
            this.measure = measure;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMeasure() {
            return measure;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String mealId;
    private String name;
    private String imageUrl;
    private String category;
    private String cuisine;
    private List<String> tags;
    private List<Ingredient> ingredients;
    private String instructions;
    private boolean isOwn;

    public Recipe(String mealId, String name, String imageUrl, String category, String cuisine, List<String> tags, List<Ingredient> ingredients, String instructions, boolean isOwn) {
        this.mealId = mealId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
        this.cuisine = cuisine;
        this.tags = tags;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.isOwn = isOwn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String id) {
        this.mealId = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = new ArrayList<>(Arrays.asList(tags.split(",")));
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isOwn() {
        return isOwn;
    }

    public void setOwn(boolean own) {
        isOwn = own;
    }
}
