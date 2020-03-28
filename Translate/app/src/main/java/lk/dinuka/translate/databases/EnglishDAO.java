package lk.dinuka.translate.databases;

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


//    @Query("SELECT * FROM EnglishEntered WHERE id =:taskId")        // get a specified English word (specified by id here)
//    LiveData<EnglishEntered> getEnglish(int taskId);


    @Update
    void updateTask(EnglishEntered englishEntered);


    @Delete
    void deleteTask(EnglishEntered englishEntered);
}
