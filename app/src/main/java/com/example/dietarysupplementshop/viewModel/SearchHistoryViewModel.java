package com.example.dietarysupplementshop.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dietarysupplementshop.entities.SearchHistory;
import com.example.dietarysupplementshop.repositories.SearchHistoryRepository;

import java.util.List;

public class SearchHistoryViewModel extends AndroidViewModel {
    private SearchHistoryRepository repository;
    private LiveData<List<SearchHistory>> allSearchHistories;

    public SearchHistoryViewModel(Application application) {
        super(application);
        repository = new SearchHistoryRepository(application);
        allSearchHistories = repository.getAllSearchHistories();
    }

    public LiveData<List<SearchHistory>> getAllSearchHistories() {
        return allSearchHistories;
    }

    public void insert(SearchHistory searchHistory) {
        repository.insert(searchHistory);
    }
    public void delete(SearchHistory searchHistory) {
        repository.delete(searchHistory);
    }

    public void deleteAllSearchHistories() {
        repository.deleteAllSearchHistories();
    }

}
