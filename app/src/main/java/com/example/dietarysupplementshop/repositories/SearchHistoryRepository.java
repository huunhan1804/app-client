package com.example.dietarysupplementshop.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.dietarysupplementshop.daos.SearchHistoryDao;
import com.example.dietarysupplementshop.database.SearchHistoryDatabase;
import com.example.dietarysupplementshop.entities.SearchHistory;
import java.util.List;
public class SearchHistoryRepository {
    private SearchHistoryDao searchHistoryDao;
    private LiveData<List<SearchHistory>> allSearchHistories;

    // Constructor
    public SearchHistoryRepository(Application application) {
        SearchHistoryDatabase db = SearchHistoryDatabase.getInstance(application);
        searchHistoryDao = db.searchHistoryDao();
        allSearchHistories = searchHistoryDao.getAllSearchHistories();
    }

    public void insert(SearchHistory searchHistory) {
        new InsertAsyncTask(searchHistoryDao).execute(searchHistory);
    }

    public LiveData<List<SearchHistory>> getAllSearchHistories() {
        return allSearchHistories;
    }

    public void delete(SearchHistory searchHistory) {
        new DeleteAsyncTask(searchHistoryDao).execute(searchHistory);
    }

    public void deleteAllSearchHistories() {
        new DeleteAllAsyncTask(searchHistoryDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private SearchHistoryDao asyncTaskDao;

        DeleteAllAsyncTask(SearchHistoryDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<SearchHistory, Void, Void> {
        private SearchHistoryDao asyncTaskDao;

        InsertAsyncTask(SearchHistoryDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SearchHistory... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<SearchHistory, Void, Void> {
        private SearchHistoryDao asyncTaskDao;

        DeleteAsyncTask(SearchHistoryDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SearchHistory... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
