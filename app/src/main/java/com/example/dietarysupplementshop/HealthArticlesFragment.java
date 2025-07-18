package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dietarysupplementshop.adapter.ArticleCategoryAdapter;
import com.example.dietarysupplementshop.adapter.HealthArticleAdapter;
import com.example.dietarysupplementshop.model.ArticleCategory;
import com.example.dietarysupplementshop.viewModel.HealthViewModel;

import java.util.ArrayList;

public class HealthArticlesFragment extends Fragment {

    private HealthViewModel healthViewModel;
    private EditText etSearchArticles;
    private RecyclerView rvArticleCategories, rvHealthArticles;
    private HealthArticleAdapter healthArticleAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private String currentSearchQuery = "";
    private String currentCategoryFilter = "Tất cả";
    private final Handler searchHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_health_articles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        healthViewModel = new ViewModelProvider(this).get(HealthViewModel.class);

        setupAdapters();
        setupListeners();
        observeViewModel();

        healthViewModel.fetchArticles(currentCategoryFilter, currentSearchQuery, true);
    }

    private void initViews(View view) {
        etSearchArticles = view.findViewById(R.id.et_search_articles);
        rvArticleCategories = view.findViewById(R.id.rv_article_categories);
        rvHealthArticles = view.findViewById(R.id.rv_health_articles);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupAdapters() {
        rvHealthArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        healthArticleAdapter = new HealthArticleAdapter(new ArrayList<>());
        rvHealthArticles.setAdapter(healthArticleAdapter);

        ArrayList<ArticleCategory> categories = new ArrayList<>();
        categories.add(new ArticleCategory("Tất cả", true));
        categories.add(new ArticleCategory("Vitamin", false));
        categories.add(new ArticleCategory("Khoáng chất", false));
        rvArticleCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ArticleCategoryAdapter categoryAdapter = new ArticleCategoryAdapter(categories, categoryName -> {
            currentCategoryFilter = categoryName;
            healthViewModel.fetchArticles(currentCategoryFilter, currentSearchQuery, true);
        });
        rvArticleCategories.setAdapter(categoryAdapter);
    }

    private void setupListeners() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            healthViewModel.fetchArticles(currentCategoryFilter, currentSearchQuery, true);
        });

        rvHealthArticles.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == healthArticleAdapter.getItemCount() - 1) {
                    healthViewModel.fetchArticles(currentCategoryFilter, currentSearchQuery, false);
                }
            }
        });

        etSearchArticles.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchHandler.removeCallbacksAndMessages(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentSearchQuery = s.toString();
                searchHandler.postDelayed(() -> {
                    healthViewModel.fetchArticles(currentCategoryFilter, currentSearchQuery, true);

                }, 500);
            }
        });
    }

    private void observeViewModel() {
        healthViewModel.getArticlesResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        if (healthArticleAdapter.getItemCount() == 0) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        if (resource.getData() != null) {
                            healthArticleAdapter.updateList(resource.getData());
                        }
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), "Lỗi: " + resource.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }
}