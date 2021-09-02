package lab.mobileprogramming.cookaid;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lab.mobileprogramming.cookaid.model.Recipe;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.ViewHolder> {

    public Context context;
    private final List<Recipe.Ingredient> ingredients;

    public IngredientsListAdapter(Context context, List<Recipe.Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientItem = (TextView) itemView;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = new TextView(parent.getContext());
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsListAdapter.ViewHolder holder, int position) {
        Recipe.Ingredient ingredient = ingredients.get(position);
        holder.ingredientItem.setText(ingredient.getName() + " | " + ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
