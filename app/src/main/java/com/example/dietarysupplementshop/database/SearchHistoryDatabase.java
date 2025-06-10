package com.example.dietarysupplementshop.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dietarysupplementshop.daos.SearchHistoryDao;
import com.example.dietarysupplementshop.entities.SearchHistory;

@Database(entities = {SearchHistory.class}, version = 1)
public abstract class SearchHistoryDatabase extends RoomDatabase {
    public  static final String DATABASE_NAME = "searchResult.db";
    private static SearchHistoryDatabase instance;

    public  static synchronized SearchHistoryDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), SearchHistoryDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract SearchHistoryDao searchHistoryDao();
}
