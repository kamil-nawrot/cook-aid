package lab.mobileprogramming.cookaid;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lab.mobileprogramming.cookaid.model.Recipe;

public class SuggestedRecipeAdapter extends RecyclerView.Adapter<SuggestedRecipeAdapter.ViewHolder> {

    private final List<Recipe> recipes;

    public Context context;
    public SuggestedRecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView foodImageView;
        public TextView foodNameView;
        public TextView foodCategoryView;
        public TextView foodCuisineView;
        public Button detailsButtonView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImageView = (ImageView) itemView.findViewById(R.id.foodImage);
            foodNameView = (TextView) itemView.findViewById(R.id.foodName);
            foodCategoryView = (TextView) itemView.findViewById(R.id.foodCategory);
            foodCuisineView = (TextView) itemView.findViewById(R.id.foodCuisine);
            detailsButtonView = (Button) itemView.findViewById(R.id.foodDetailsBtn);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View recipeItemView = inflater.inflate(R.layout.recipe_item, parent, false);

        return new ViewHolder(recipeItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        TextView nameText = holder.foodNameView;
        TextView categoryText = holder.foodCategoryView;
        TextView cuisineText = holder.foodCuisineView;
        ImageView imageView = holder.foodImageView;

        Log.d("INFO: ", String.valueOf(this.getItemCount()));

        nameText.setText(recipe.getName());
        categoryText.setText(recipe.getCategory());
        cuisineText.setText(recipe.getCuisine());
        Picasso.get().load(recipe.getImageUrl()).fit().centerCrop().into(imageView);

        Button deleteButton = holder.detailsButtonView;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToDetailsIntent = new Intent(context, RecipeDetailsActivity.class);
                goToDetailsIntent.putExtra("recipe", recipe);
                context.startActivity(goToDetailsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
