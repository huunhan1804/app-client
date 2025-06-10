package com.example.dietarysupplementshop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dietarysupplementshop.adapter.Product2Adapter;
import com.example.dietarysupplementshop.adapter.ProductAdapter;
import com.example.dietarysupplementshop.entities.SearchHistory;
import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.viewModel.ProductViewModel;
import com.example.dietarysupplementshop.viewModel.SearchHistoryViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    private ImageButton horizontalButton;
    private ImageButton menuButton;
    private TextInputLayout searchTextInputLayout;
    private TextInputEditText searchEditText;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private Product2Adapter product2Adapter;

    private FrameLayout frameLayout;
    private LottieAnimationView animationView;
    private String searchText;

    private List<Product> productList;

    private ProductViewModel productViewModel;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        productViewModel = MyApplication.getInstance().getProductViewModel();
        productList = new ArrayList<>();

        searchTextInputLayout = findViewById(R.id.searchTextInputLayout);
        searchEditText = findViewById(R.id.searchEditText);

        searchEditText.setOnClickListener(v -> {
            finish();
        });

        if (getIntent() != null && getIntent().hasExtra("SEARCH_TEXT")) {
            searchText = getIntent().getStringExtra("SEARCH_TEXT");
            searchEditText.setText(searchText);
            receiveSearchText(searchText);
        }

        if(getIntent() != null && getIntent().getLongExtra("categoryId", -1) != -1) {
            TextView titleCategory = findViewById(R.id.titleCategory);
            titleCategory.setText(getIntent().getStringExtra("categoryName"));
            showListProductByCategory(getIntent().getLongExtra("categoryId", -1));
        }

        horizontalButton = findViewById(R.id.iconHamburger);
        menuButton = findViewById(R.id.iconMenu);
        recyclerView = findViewById(R.id.productRecyclerView);


        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        searchEditText.setOnTouchListener((v, event) -> {
            v.performClick();
            return false;
        });

        CoordinatorLayout coordinatorLayout = findViewById(R.id.searchView);
        coordinatorLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (searchEditText.isFocused()) {
                    searchEditText.clearFocus();  // Bỏ focus khỏi EditText
                }
                hideKeyboard(v);
            }
            return false;
        });

    }

    private void showListProductByCategory(long categoryId) {
        productViewModel.getProductByCategory(categoryId).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case SUCCESS:
                        hideProgressBar();
                        if (resource.getData() != null) {
                            productList = resource.getData();
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                            recyclerView.setLayoutManager(gridLayoutManager);

                            productAdapter = new ProductAdapter(productList, getApplicationContext());
                            recyclerView.setAdapter(productAdapter);

                            menuButton.setOnClickListener(view1 -> {
                                recyclerView.setLayoutManager(gridLayoutManager);
                                recyclerView.setAdapter(productAdapter);
                            });

                            horizontalButton.setOnClickListener(view12 -> {
                                product2Adapter = new Product2Adapter(getApplicationContext(), productList);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(product2Adapter);
                            });
                        }
                        break;
                    case ERROR:
                        hideProgressBar();
                        Toast.makeText(getApplicationContext(), resource.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                    case LOADING:
                        showProgressBar();
                        break;
                }
            }
        });
    }

    public void receiveSearchText(String searchText) {
        productViewModel.getListSearchProduct(searchText).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case SUCCESS:
                        hideProgressBar();
                        if (resource.getData() != null) {
                            productList = resource.getData();
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                            recyclerView.setLayoutManager(gridLayoutManager);

                            productAdapter = new ProductAdapter(productList, getApplicationContext());
                            recyclerView.setAdapter(productAdapter);

                            menuButton.setOnClickListener(view1 -> {
                                recyclerView.setLayoutManager(gridLayoutManager);
                                recyclerView.setAdapter(productAdapter);
                            });

                            horizontalButton.setOnClickListener(view12 -> {
                                product2Adapter = new Product2Adapter(getApplicationContext(), productList);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(product2Adapter);
                            });
                        }
                        break;
                    case ERROR:
                        hideProgressBar();
                        Toast.makeText(getApplicationContext(), resource.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                    case LOADING:
                        showProgressBar();
                        break;
                }
            }
        });
    }

    public void showProgressBar() {
        frameLayout = findViewById(R.id.frameLoading);
        animationView = findViewById(R.id.animationView);
        frameLayout.setVisibility(View.VISIBLE);
        animationView.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        frameLayout = findViewById(R.id.frameLoading);
        animationView = findViewById(R.id.animationView);
        frameLayout.setVisibility(View.GONE);
        animationView.setVisibility(View.GONE);
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

}