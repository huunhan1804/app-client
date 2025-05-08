package com.example.dietarysupplementshop;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dietarysupplementshop.adapter.SearchHistoryAdapter;
import com.example.dietarysupplementshop.entities.SearchHistory;
import com.example.dietarysupplementshop.viewModel.SearchHistoryViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SearchActivity extends AppCompatActivity {

    private TextInputLayout searchTextInputLayout;
    private TextInputEditText searchEditText;
    private RecyclerView searchHistoryRecyclerView;
    private SearchHistoryViewModel viewModel;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TextView clearAllHistoryButton = findViewById(R.id.clearAllHistory);
        searchTextInputLayout = findViewById(R.id.searchTextInputLayout);
        searchEditText = findViewById(R.id.searchEditText);
        searchHistoryRecyclerView = findViewById(R.id.searchHistoryRecyclerView);
        searchHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = MyApplication.getInstance().getSearchHistoryViewModel();

        // Đặt adapter cho RecyclerView
        SearchHistoryAdapter adapter = new SearchHistoryAdapter();
        searchHistoryRecyclerView.setAdapter(adapter);

        // Lắng nghe dữ liệu từ ViewModel và cập nhật UI
        viewModel.getAllSearchHistories().observe(this, searchHistories -> {
            adapter.setSearchHistories(searchHistories);

            if (searchHistories == null || searchHistories.isEmpty()) {
                clearAllHistoryButton.setVisibility(View.GONE);
            } else {
                clearAllHistoryButton.setVisibility(View.VISIBLE);
            }

        });

        // Đặt sự kiện cho phím "Enter"
        searchEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchText = searchEditText.getText().toString();

                // Insert the search text into the database
                SearchHistory searchHistory = new SearchHistory(searchText);
                viewModel.insert(searchHistory);

                Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                intent.putExtra("SEARCH_TEXT", searchText);
                startActivity(intent);
                return true;
            }
            return false;
        });


        clearAllHistoryButton.setOnClickListener(v -> {
            new AlertDialog.Builder(SearchActivity.this)
                    .setTitle("Clear All Histories")
                    .setMessage("Are you sure you want to delete all search histories?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        viewModel.deleteAllSearchHistories();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });


        adapter.setOnSearchHistoryClickListener(new SearchHistoryAdapter.OnSearchHistoryClickListener() {
            @Override
            public void onSearchHistoryClicked(String searchText) {
                Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                intent.putExtra("SEARCH_TEXT", searchText);
                startActivity(intent);
            }

            @Override
            public void onSearchHistoryDeleteButtonClicked(SearchHistory searchHistory) {
                viewModel.delete(searchHistory);
            }
        });



        searchEditText.postDelayed(() -> {
            searchEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
        }, 100);



        RelativeLayout relativeLayout = findViewById(R.id.frameContain);
        relativeLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                hideKeyboard();
            }
            return false;
        });


        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }
}