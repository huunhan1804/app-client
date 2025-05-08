package com.example.dietarysupplementshop.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dietarysupplementshop.entities.SearchHistory;

import java.util.List;

@Dao
public interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SearchHistory searchHistory);

    @Query("SELECT * FROM SearchHistory ORDER BY id DESC")
    LiveData<List<SearchHistory>> getAllSearchHistories();

    @Delete
    void delete(SearchHistory searchHistory);

    @Query("DELETE FROM SearchHistory")
    void deleteAll();
}

