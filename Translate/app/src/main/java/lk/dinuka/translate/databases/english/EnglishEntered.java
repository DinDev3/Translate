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
    private String translationLang1;        // 2nd translation language
    private String translationLang2;        // 3rd translation language
    private String translationLang3;        // 4th translation language
    private String translationLang4;        // 5th translation language


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

    public String getTranslationLang1() {
        return translationLang1;
    }

    public void setTranslationLang1(String translationLang1) {
        this.translationLang1 = translationLang1;
    }

    public String getTranslationLang2() {
        return translationLang2;
    }

    public void setTranslationLang2(String translationLang2) {
        this.translationLang2 = translationLang2;
    }

    public String getTranslationLang3() {
        return translationLang3;
    }

    public void setTranslationLang3(String translationLang3) {
        this.translationLang3 = translationLang3;
    }

    public String getTranslationLang4() {
        return translationLang4;
    }

    public void setTranslationLang4(String translationLang4) {
        this.translationLang4 = translationLang4;
    }
}
