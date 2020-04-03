package lk.dinuka.translate.databases.english;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;

import lk.dinuka.translate.databases.TranslationDB;
import lk.dinuka.translate.util.AppUtils;

public class EnglishRepository {
    // hold all the methods of the DAO to access the DB
    // intermediate class between the Main Thread and the DAO

    private static final String DATABASE_NAME = "translation.db";      // name of the database

    private TranslationDB translationDB;

    // creating an instance of this class
    public EnglishRepository(Context context) {

        // creating an instance of the created database
        translationDB = Room.databaseBuilder(context, TranslationDB.class, DATABASE_NAME).build();
    }


//    public void insertTask(String english) {
//
//        insertTask(english);
//    }

    public void insertTask(String english) {        // creates the table entry row object

        EnglishEntered englishEntered = new EnglishEntered();
        englishEntered.setEnglish(english);
        englishEntered.setCreatedAt(AppUtils.getCurrentDateTime());
        englishEntered.setUpdatedAt(AppUtils.getCurrentDateTime());

        insertTask(englishEntered);
    }

    public void insertTask(final EnglishEntered englishEntered) {       // enters the table entry row object into the database
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                translationDB.englishDAO().insertTask(englishEntered);
                return null;
            }
        }.execute();
    }

    public void updateTask(final EnglishEntered englishEntered) {
        englishEntered.setUpdatedAt(AppUtils.getCurrentDateTime());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                translationDB.englishDAO().updateTask(englishEntered);
                return null;
            }
        }.execute();
    }

//    public void deleteTask(final int id) {
//        final LiveData<Note> task = getTask(id);
//        if(task != null) {
//            new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... voids) {
//                    noteDatabase.daoAccess().deleteTask(task.getValue());
//                    return null;
//                }
//            }.execute();
//        }
//    }

//    public void deleteTask(final EnglishEntered englishEntered) {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                translationDB.englishDAO().deleteTask(englishEntered);
//                return null;
//            }
//        }.execute();
//    }

    public LiveData<EnglishEntered> getEnglishByID(int id) {            // get a record using the id of the record
        return translationDB.englishDAO().getEnglishByID(id);
    }

    public LiveData<List<EnglishEntered>> getEnglishFromDB() {            // get all the English words/ phrases stored in the database
        return translationDB.englishDAO().fetchAllEnglish();
    }


}
