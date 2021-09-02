package lab.mobileprogramming.cookaid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lab.mobileprogramming.cookaid.model.Recipe;

public class AddRecipeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1717;
    private static final int ADD_RECIPE = 393;


    EditText nameInput;
    EditText cuisineInput;
    EditText categoryInput;
    EditText tagsInput;
    ImageView thumbnailImageView;
    Uri thumbnailImageUri;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            thumbnailImageUri = selectedImage;
            Picasso.get().load(selectedImage).fit().centerCrop().into(thumbnailImageView);
        }
        else {
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    EditText addInstructionInput;
    TableLayout instructionsTable;
    String instructionsString = "";

    EditText addIngredientNameInput;
    EditText addIngredientMeasureInput;
    TableLayout ingredientsTable;
    List<Recipe.Ingredient> ingredientsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        addInstructionInput = findViewById(R.id.addInstructionInput);
        instructionsTable = findViewById(R.id.instructionsTable);

        addIngredientNameInput = findViewById(R.id.addIngredientNameInput);
        addIngredientMeasureInput = findViewById(R.id.addIngredientMeasureInput);
        ingredientsTable = findViewById(R.id.ingredientsTable);

        nameInput = (EditText) findViewById(R.id.nameInput);
        categoryInput = (EditText) findViewById(R.id.categoryInput);
        cuisineInput = (EditText) findViewById(R.id.cuisineInput);
        tagsInput = (EditText) findViewById(R.id.tagsInput);
        thumbnailImageView = (ImageView) findViewById(R.id.imagePreview);

        Uri blankImage = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.blank_image);
        thumbnailImageUri = blankImage;
        Picasso.get().load(blankImage).fit().centerCrop().into(thumbnailImageView);
    }

    public void addThumbnail(View view) {
        Intent pickThumbnailIntent = new Intent();
        pickThumbnailIntent.setType("image/*");
        pickThumbnailIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(pickThumbnailIntent, "Select Picture"), PICK_IMAGE);
    }

    public void addInstruction(View view) {
        String instruction = addInstructionInput.getText().toString();
        TableRow instructionRow = (TableRow) getLayoutInflater().inflate(R.layout.instruction_tablerow, null);
        TextView instructionRowText = instructionRow.findViewById(R.id.instructionRowText);

        instructionRowText.setText(String.valueOf(instructionsTable.getChildCount()) + ". " + instruction);
        instructionsTable.addView(instructionRow, instructionsTable.getChildCount() - 1);
        instructionsString += instruction + "\n";

        addInstructionInput.setText("");
    }

    public void addIngredient(View view) {
        String ingredientName = addIngredientNameInput.getText().toString();
        String ingredientMeasure = addIngredientMeasureInput.getText().toString();

        TableRow ingredientRow = (TableRow) getLayoutInflater().inflate(R.layout.ingredient_tablerow, null);
        TextView ingredientNameText = ingredientRow.findViewById(R.id.ingredientNameText);
        TextView ingredientMeasureText = ingredientRow.findViewById(R.id.ingredientMeasureText);

        ingredientNameText.setText(ingredientName);
        ingredientMeasureText.setText(ingredientMeasure);

        ingredientsList.add(new Recipe.Ingredient(ingredientName, ingredientMeasure));
        ingredientsTable.addView(ingredientRow, ingredientsTable.getChildCount() - 1);

        addIngredientNameInput.setText("");
        addIngredientMeasureInput.setText("");
    }

    public void addRecipe(View view) {
        if (nameInput.getText().toString().isEmpty() ||
                cuisineInput.getText().toString().isEmpty() ||
                categoryInput.getText().toString().isEmpty() ||
                tagsInput.getText().toString().isEmpty()
        ) {
            Toast.makeText(getApplicationContext(), "All fields must be filled to add the recipe!", Toast.LENGTH_LONG).show();
        }
        else {
            Recipe newRecipe = new Recipe(
                    "own_recipe_" + String.valueOf(System.currentTimeMillis()),
                    nameInput.getText().toString(),
                    thumbnailImageUri.toString(),
                    categoryInput.getText().toString(),
                    cuisineInput.getText().toString(),
                    new ArrayList<>(Arrays.asList(tagsInput.getText().toString().split(","))),
                    ingredientsList,
                    instructionsString,
                    true
                    );

            Intent resultIntent = new Intent();
            resultIntent.putExtra("recipeToAdd", newRecipe);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

}