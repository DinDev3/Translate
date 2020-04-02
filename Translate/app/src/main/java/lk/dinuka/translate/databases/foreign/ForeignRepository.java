//package lk.dinuka.translate.databases.foreign;
//
//import android.content.Context;
//import android.os.AsyncTask;
//
//import androidx.lifecycle.LiveData;
//import androidx.room.Room;
//
//import java.util.List;
//
//import lk.dinuka.translate.databases.TranslationDB;
//import lk.dinuka.translate.util.AppUtils;
//
//public class ForeignRepository {
//    // hold all the methods of the DAO to access the DB
//    // intermediate class between the Main Thread and the DAO
//
//    private static final String DATABASE_NAME = "translation.db";      // name of the database
//
//    private TranslationDB translationDB;
//
//
//    // creating an instance of this class
//    public ForeignRepository(Context context) {
//
//        // creating an instance of the created database
//        translationDB = Room.databaseBuilder(context, TranslationDB.class, DATABASE_NAME).build();
//    }
//
//    public void updateTask(final ForeignLanguage foreignLanguage) {
//        foreignLanguage.setUpdatedAt(AppUtils.getCurrentDateTime());
//
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                translationDB.foreignDAO().updateTask(foreignLanguage);
//                return null;
//            }
//        }.execute();
//    }
//
//    public LiveData<ForeignLanguage> getLangByID(int id) {            // get a record using the id of the record
//        return translationDB.foreignDAO().getLangByID(id);
//    }
//
//    public LiveData<List<ForeignLanguage>> getLangsFromDB() {            // get all the English words/ phrases stored in the database
//        return translationDB.foreignDAO().fetchAllLanguages();
//    }
//
//
//}
