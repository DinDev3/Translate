package lk.dinuka.translate.Databases;

import android.content.Context;

import androidx.room.Room;

public class EnglishRepository {
    // hold all the methods of the DAO to access the DB
    // intermediate class between the Main Thread and the DAO

    private static final String DATABASE_NAME = "translation";      // name of the database

    private TranslationDB translationDB;

    // creating an instance of this class
    public EnglishRepository(Context context) {

        // creating an instance of the created database
        translationDB = Room.databaseBuilder(context, TranslationDB.class, DATABASE_NAME).build();
    }
}


