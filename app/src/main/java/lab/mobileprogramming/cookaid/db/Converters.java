package lab.mobileprogramming.cookaid.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lab.mobileprogramming.cookaid.model.Recipe;

public class Converters {
    @TypeConverter
    public static List<String> fromString(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static List<Recipe.Ingredient> fromIngredientsString(String value) {
        Type listType = new TypeToken<List<Recipe.Ingredient>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromIngredientsList(List<Recipe.Ingredient> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}