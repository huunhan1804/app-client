package com.example.dietarysupplementshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.entities.SearchHistory;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder> {
    private OnSearchHistoryClickListener listener;

    public void setOnSearchHistoryClickListener(OnSearchHistoryClickListener listener) {
        this.listener = listener;
    }
    private List<SearchHistory> searchHistories = new ArrayList<>();

    @NonNull
    @Override
    public SearchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_history_item, parent, false);
        return new SearchHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryViewHolder holder, int position) {
        SearchHistory currentSearchHistory = searchHistories.get(position);
        holder.searchHistoryTextView.setText(currentSearchHistory.getQuery());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                String searchText = searchHistories.get(position).getQuery();
                listener.onSearchHistoryClicked(searchText);
            }
        });

        holder.deleteHistoryButton.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                SearchHistory searchHistoryToDelete = searchHistories.get(position);
                listener.onSearchHistoryDeleteButtonClicked(searchHistoryToDelete);
            }
        });



    }

    @Override
    public int getItemCount() {
        return searchHistories.size();
    }

    public void setSearchHistories(List<SearchHistory> searchHistories) {
        this.searchHistories = searchHistories;
        notifyDataSetChanged();
    }

    class SearchHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView searchHistoryTextView;
        private ImageButton deleteHistoryButton;

        public SearchHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            searchHistoryTextView = itemView.findViewById(R.id.searchHistoryTextView);
            deleteHistoryButton = itemView.findViewById(R.id.deleteHistoryButton);
        }
    }

    public interface OnSearchHistoryClickListener {
        void onSearchHistoryClicked(String searchText);
        void onSearchHistoryDeleteButtonClicked(SearchHistory searchHistory);
    }

}

