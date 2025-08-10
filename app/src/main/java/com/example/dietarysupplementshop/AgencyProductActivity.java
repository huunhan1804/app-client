package com.example.dietarysupplementshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dietarysupplementshop.adapter.ProductAgencyAdapter;
import com.example.dietarysupplementshop.adapter.AgencyProductPagerAdapter;
import com.example.dietarysupplementshop.responses.ProductInfoDTO;
import com.example.dietarysupplementshop.viewModel.AgencyProductViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dietarysupplementshop.adapter.ProductAgencyAdapter;
import com.example.dietarysupplementshop.adapter.AgencyProductPagerAdapter;
import com.example.dietarysupplementshop.responses.ProductInfoDTO;
import com.example.dietarysupplementshop.viewModel.AgencyProductViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AgencyProductActivity extends AppCompatActivity implements ProductAgencyAdapter.OnProductActionListener {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private AgencyProductPagerAdapter pagerAdapter;
    private AgencyProductViewModel viewModel;
    private ProgressBar progressBar;
    private Button addNewProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_product);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.productsViewPager);
        addNewProductButton = findViewById(R.id.addNewProductButton);
        progressBar = findViewById(R.id.progressBar);

        viewModel = new ViewModelProvider(this).get(AgencyProductViewModel.class);

        pagerAdapter = new AgencyProductPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(pagerAdapter.getTabTitle(position))
        ).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                String statusCode = pagerAdapter.getStatusCode(position);
                viewModel.loadProductsByStatus(statusCode);
            }
        });

        setupListeners();
        observeViewModel();

        // Tải dữ liệu ban đầu cho tab đầu tiên
        viewModel.loadProductsByStatus(pagerAdapter.getStatusCode(0));
    }

    private void setupListeners() {
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        addNewProductButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AgencyAddProductActivity.class);
            startActivity(intent);
        });
    }

    private void observeViewModel() {
        viewModel.isLoading.observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.errorMessage.observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        // Lắng nghe dữ liệu cho các tab và cập nhật số lượng
        viewModel.approvedProducts.observe(this, products -> updateTabCount(0, "Đã duyệt", products));
        viewModel.pendingProducts.observe(this, products -> updateTabCount(1, "Chờ duyệt", products));
        viewModel.rejectedProducts.observe(this, products -> updateTabCount(2, "Từ chối", products));
    }

    private void updateTabCount(int position, String title, List<ProductInfoDTO> products) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        if (tab != null) {
            int count = products != null ? products.size() : 0;
            tab.setText(title + " (" + count + ")");
        }
    }

    @Override
    public void onEditProduct(ProductInfoDTO product) {
        Intent intent = new Intent(this, AgencyAddProductActivity.class);
        intent.putExtra("product_id_to_edit", product.getProduct_id());
        startActivity(intent);
    }

    @Override
    public void onDeleteProduct(ProductInfoDTO product) {
        viewModel.deleteProduct(product.getProduct_id());
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadAllTabs();
    }
}