package lk.dinuka.translate.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import lk.dinuka.translate.databases.DAOs.EnglishDAO;
import lk.dinuka.translate.databases.entities.EnglishEntered;
import lk.dinuka.translate.databases.DAOs.ForeignDAO;
import lk.dinuka.translate.databases.entities.ForeignLanguage;

@Database(entities = {EnglishEntered.class, ForeignLanguage.class}, version = 1)
public abstract class TranslationDB extends RoomDatabase {
    // all the entities (all the tables) that are required to be created for the database are defined here

    // list of operations that are required to be performed on the Database are below
    public abstract EnglishDAO englishDAO();
    public abstract ForeignDAO foreignDAO();
}