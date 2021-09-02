package lab.mobileprogramming.cookaid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.room.Room;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lab.mobileprogramming.cookaid.db.AppDatabase;
import lab.mobileprogramming.cookaid.db.RecipesDao;
import lab.mobileprogramming.cookaid.model.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity {

    private RecyclerView rvIngredientsList;
    private RecyclerView.Adapter adapter;

    private List<Recipe.Ingredient> ingredients;

    public Recipe recipe;
    AppDatabase db;
    RecipesDao recipesDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").allowMainThreadQueries().build();
        recipesDao = db.recipesDao();

        TextView nameText = findViewById(R.id.recipeName);
        ImageView image = findViewById(R.id.recipeImage);
        TextView categoryText = findViewById(R.id.recipeCategory);
        TextView cuisineText = findViewById(R.id.recipeCuisine);
        TextView instructionsText = findViewById(R.id.recipeInstructionsContent);

        nameText.setText(recipe.getName());
        Picasso.get().load(recipe.getImageUrl()).fit().centerCrop().into(image);
        categoryText.setText(recipe.getCategory());
        cuisineText.setText(recipe.getCuisine());
        instructionsText.setText(recipe.getInstructions());

        ImageButton favoriteBtn = (ImageButton) findViewById(R.id.addToFavoritesBtn);

        if(recipe.isOwn()) {
            Resources res = getResources();
            favoriteBtn.setImageDrawable(ResourcesCompat.getDrawable(res, android.R.drawable.star_big_on, null));
        }

        rvIngredientsList = (RecyclerView) findViewById(R.id.ingredientsList);
        rvIngredientsList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        ingredients = recipe.getIngredients();

        adapter = new IngredientsListAdapter(getApplicationContext(), ingredients);
        rvIngredientsList.setAdapter(adapter);

    }

    public void backToDashboard(View view) {
        Intent backToDashboardIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backToDashboardIntent);
    }

    public void toggleFavorites(View view) {

        ImageButton favoriteBtn = (ImageButton) view.findViewById(R.id.addToFavoritesBtn);
        Resources res = getResources();

        if(recipe.isOwn()) {
            recipe.setOwn(false);
            recipesDao.deleteById(recipe);
            Toast.makeText(getApplicationContext(), "Recipe removed from favorites!", Toast.LENGTH_LONG).show();
            favoriteBtn.setImageDrawable(ResourcesCompat.getDrawable(res, android.R.drawable.star_big_off, null));

        }
        else {
            recipe.setOwn(true);
            Toast.makeText(getApplicationContext(), "Recipe added to favorites!", Toast.LENGTH_LONG).show();
            favoriteBtn.setImageDrawable(ResourcesCompat.getDrawable(res, android.R.drawable.star_big_on, null));
            recipesDao.updateRecipe(recipe);
        }


    }

    public void startCookingProcedure(View view) {
        Intent cookingProcedureActivity = new Intent(getApplicationContext(), CookingProcedureActivity.class);
        cookingProcedureActivity.putExtra("recipe", recipe);
        startActivity(cookingProcedureActivity);
    }
}