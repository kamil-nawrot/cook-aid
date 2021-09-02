package lab.mobileprogramming.cookaid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.room.Room;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lab.mobileprogramming.cookaid.db.AppDatabase;
import lab.mobileprogramming.cookaid.db.RecipesDao;
import lab.mobileprogramming.cookaid.model.Recipe;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_RECIPE = 393;

    private RecyclerView rvSuggestedRecipes;
    private RecyclerView rvOwnRecipes;
    private RecyclerView.Adapter adapter;

    private List<Recipe> recipes;
    private List<Recipe> ownRecipes;

    AppDatabase db;
    RecipesDao recipesDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvSuggestedRecipes = (RecyclerView) findViewById(R.id.suggestedList);
        SnapHelper suggestedListHelper = new LinearSnapHelper();
        suggestedListHelper.attachToRecyclerView(rvSuggestedRecipes);
        rvSuggestedRecipes.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        rvOwnRecipes = (RecyclerView) findViewById(R.id.ownList);
        SnapHelper ownListHelper = new LinearSnapHelper();
        ownListHelper.attachToRecyclerView(rvOwnRecipes);
        rvOwnRecipes.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        //////////////////////////// getApplicationContext().deleteDatabase("database");

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").allowMainThreadQueries().build();
        recipesDao = db.recipesDao();

        recipes = new ArrayList<>();
        ownRecipes = recipesDao.getAllOwn();

        TextView emptyList = findViewById(R.id.emptyOwnListInfo);
        if (ownRecipes.size() == 0) {
            emptyList.setVisibility(View.VISIBLE);
            rvOwnRecipes.setVisibility(View.GONE);
        }
        else {
            emptyList.setVisibility(View.GONE);
            rvOwnRecipes.setVisibility(View.VISIBLE);
        }

        adapter = new SuggestedRecipeAdapter(getApplicationContext(), recipes);
        rvSuggestedRecipes.setAdapter(adapter);

        if (recipesDao.getAllRandom().size() < 5) {
            getRandomRecipes(5 - recipesDao.getAllRandom().size());
        }
        else {
            recipes = recipesDao.getAllRandom();
            adapter = new SuggestedRecipeAdapter(getApplicationContext(), recipes);
            rvSuggestedRecipes.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new OwnRecipeAdapter(getApplicationContext(), ownRecipes);
        rvOwnRecipes.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (recipesDao.getAllRandom().size() < 5) {
            getRandomRecipes(5 - recipesDao.getAllRandom().size());
            adapter = new SuggestedRecipeAdapter(getApplicationContext(), recipes);
            rvSuggestedRecipes.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onDestroy() {
        recipesDao.deleteAllRandom();
        super.onDestroy();
    }

    public void getRandomRecipes(int count) {
        String baseUrl = "https://themealdb.com/api/json/v1/1/random.php";
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Preparing...");
        progressDialog.show();

        for (int i = 0; i <= count; i++) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null,
                    response -> {
                        try {
                            JSONObject meal = new JSONObject(new JSONArray(response.getString("meals")).getString(0));
                            String id = meal.getString("idMeal");
                            String imageUrl = meal.getString("strMealThumb");
                            String mealName = meal.getString("strMeal");
                            String category = meal.getString("strCategory");
                            String cuisine = meal.getString("strArea");
                            List<String> tags = new ArrayList<>(Arrays.asList(meal.getString("strTags").split(",")));
                            String instructions = meal.getString("strInstructions");
                            List<Recipe.Ingredient> ingredients = new ArrayList<>();
                            for (int j = 1; j <= 20; j++) {
                                String ingredient = meal.getString("strIngredient" + String.valueOf(j));
                                String measure = meal.getString("strMeasure" + String.valueOf(j));
                                if (!ingredient.equals("") && !measure.equals("")) {
                                    ingredients.add(new Recipe.Ingredient(ingredient, measure));
                                }
                            }
                            Recipe newRecipe = new Recipe(id, mealName, imageUrl, category, cuisine, tags, ingredients, instructions, false);
                            recipesDao.insertRecipe(newRecipe);
                            recipes.add(newRecipe);
                            Log.println(Log.INFO, "ADDED", "Recipe added!");

                            adapter = new SuggestedRecipeAdapter(getApplicationContext(), recipes);
                            rvSuggestedRecipes.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> error.printStackTrace()
            );

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(jsonObjectRequest);
        }

        progressDialog.dismiss();
    }

    public void startAddNewRecipeActivity(View view) {
        Intent addNewActivityIntent = new Intent(getApplicationContext(), AddRecipeActivity.class);
        startActivityForResult(addNewActivityIntent, ADD_RECIPE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_RECIPE && resultCode == Activity.RESULT_OK) {
            Recipe recipe = (Recipe) data.getExtras().get("recipeToAdd");
            ownRecipes.add(recipe);
            recipesDao.insertRecipe(recipe);
            adapter = new OwnRecipeAdapter(getApplicationContext(), ownRecipes);
            rvOwnRecipes.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "New recipe successfully added!", Toast.LENGTH_LONG);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}