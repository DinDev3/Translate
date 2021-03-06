package lk.dinuka.translate.databases.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import lk.dinuka.translate.databases.entities.ForeignLanguage;


@Dao
public interface ForeignDAO {

    @Insert
    Long insertTask(ForeignLanguage foreignLang);

    @Query("SELECT * FROM ForeignLanguage ORDER BY language")         // get all languages sorted in Alphabetical order
    LiveData<List<ForeignLanguage>> fetchAllLanguages();


//    @Query("SELECT * FROM ForeignLanguage WHERE lang_id =:request_id")        // get a specified Language (specified by id here)
//    LiveData<ForeignLanguage> getLangByID(int request_id);

    @Query("SELECT * FROM ForeignLanguage WHERE language =:lang_name")        // get a specified Language (specified by language name here)
    LiveData<ForeignLanguage> getLangByName(String lang_name);

    @Update
    void updateTask(ForeignLanguage foreignLang);
}
