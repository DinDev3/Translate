package lk.dinuka.translate.databases.english;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.Serializable;
import java.util.Date;

import lk.dinuka.translate.databases.TimestampConverter;

@Entity
public class EnglishEntered implements Serializable {
    // Represents the table structure

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String english;         // English word/ phrase


    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    private Date createdAt;

    @ColumnInfo(name = "updated_at")
    @TypeConverters({TimestampConverter.class})
    private Date updatedAt;


    private String translationLang0;        // 1st translation language



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getTranslationLang0() {
        return translationLang0;
    }

    public void setTranslationLang0(String translationLang0) {
        this.translationLang0 = translationLang0;
    }
}
