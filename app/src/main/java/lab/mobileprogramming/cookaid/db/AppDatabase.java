package lab.mobileprogramming.cookaid.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import lab.mobileprogramming.cookaid.model.Recipe;

@Database(entities = Recipe.class, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecipesDao recipesDao();

}
