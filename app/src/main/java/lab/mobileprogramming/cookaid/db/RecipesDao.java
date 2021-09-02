package lab.mobileprogramming.cookaid.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import lab.mobileprogramming.cookaid.model.Recipe;

@Dao
public interface RecipesDao {

    @Query("SELECT * FROM Recipes WHERE isOwn = 0")
    List<Recipe> getAllRandom();

    @Insert
    void insertRecipe(Recipe newRecipe);

    @Update
    void updateRecipe(Recipe updatedRecipe);

    @Query("DELETE FROM Recipes WHERE isOwn = 0")
    void deleteAllRandom();

    @Query("DELETE FROM Recipes WHERE isOwn = 1")
    void deleteAllOwn();

    @Delete
    void deleteById(Recipe recipe);

    @Query("SELECT * FROM RECIPES WHERE isOwn = 1")
    List<Recipe> getAllOwn();
}
