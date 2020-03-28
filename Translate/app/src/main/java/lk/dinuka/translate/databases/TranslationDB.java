package lk.dinuka.translate.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {EnglishEntered.class}, version = 1)
public abstract class TranslationDB extends RoomDatabase {
    // all the entities (all the tables) that are required to be created for the database are defined here

    // list of operations that are required to be performed on the Database are below
    public abstract EnglishDAO englishDAO();

}
