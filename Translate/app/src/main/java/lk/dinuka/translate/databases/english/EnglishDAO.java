package lk.dinuka.translate.databases.english;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EnglishDAO {
    // Contains the methods used for accessing the database.
    // DAO: Data Access Objects

    @Insert
    Long insertTask(EnglishEntered englishEntered);

    @Query("SELECT * FROM EnglishEntered ORDER BY english")         // get all English words & phrases sorted in Alphabetical order
    LiveData<List<EnglishEntered>> fetchAllEnglish();


    @Query("SELECT * FROM EnglishEntered WHERE id =:id")        // get a specified English word (specified by id here)
    LiveData<EnglishEntered> getEnglishByID(int id);


    @Query("SELECT * FROM EnglishEntered WHERE english =:englishSearched ORDER BY english")        // get a specified English word (specified by english phrase here)
    LiveData<EnglishEntered> getEnglishByEnglish(String englishSearched);


    @Update
    void updateTask(EnglishEntered englishEntered);


    @Delete
    void deleteTask(EnglishEntered englishEntered);
}
