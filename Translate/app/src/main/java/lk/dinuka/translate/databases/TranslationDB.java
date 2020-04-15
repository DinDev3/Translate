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

/*
References:
https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24
https://medium.com/@tonia.tkachuk/android-app-example-using-room-database-63f7091e69af
https://github.com/anitaa1990/RoomDb-Sample/blob/master/app/src/main/java/com/an/room/ui/activity/NotesListActivity.java

 */