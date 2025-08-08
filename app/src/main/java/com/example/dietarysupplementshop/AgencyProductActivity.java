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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AgencyProductActivity extends AppCompatActivity implements ProductAgencyAdapter.OnProductActionListener {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private AgencyProductPagerAdapter pagerAdapter;
    private AgencyProductViewModel viewModel;
    private ProgressBar progressBar;
    private List<ProductInfoDTO> allProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_product);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.productsViewPager);
        Button addNewProductButton = findViewById(R.id.addNewProductButton);
        ImageView backButton = findViewById(R.id.backButton);
        ImageView searchButton = findViewById(R.id.searchButton);
        ImageView chatButton = findViewById(R.id.chatButton);
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
                updateFragmentData(position);
            }
        });

        setupListeners(backButton, searchButton, chatButton, addNewProductButton);
        observeViewModel();
    }

    private void setupListeners(ImageView backButton, ImageView searchButton, ImageView chatButton, Button addNewProductButton) {
        backButton.setOnClickListener(v -> finish());
        searchButton.setOnClickListener(v -> Toast.makeText(this, "Tìm kiếm sản phẩm", Toast.LENGTH_SHORT).show());
        chatButton.setOnClickListener(v -> Toast.makeText(this, "Mở chat", Toast.LENGTH_SHORT).show());

        addNewProductButton.setOnClickListener(v -> {
            Intent intent = new Intent(AgencyProductActivity.this, AgencyAddProductActivity.class);
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

        viewModel.allAgencyProducts.observe(this, products -> {
            if (products != null) {
                allProducts = products;
                updateTabCounts(allProducts);
                updateFragmentData(viewPager.getCurrentItem());
            }
        });
    }

    private void updateFragmentData(int position) {
        String statusCode = pagerAdapter.getStatusCode(position);

        List<ProductInfoDTO> filteredProducts = allProducts.stream()
                .filter(p -> {
                    String productStatus = p.getApprovalStatus().getStatusCode();
                    if ("APPROVED".equals(statusCode)) {
                        return "APPROVED".equals(productStatus) && p.getQuantity_in_stock() > 0;
                    } else if ("OUT_OF_STOCK".equals(statusCode)) {
                        return "APPROVED".equals(productStatus) && p.getQuantity_in_stock() <= 0;
                    }
                    return statusCode.equals(productStatus);
                })
                .collect(Collectors.toList());

        ProductListFragment fragment = (ProductListFragment) getSupportFragmentManager()
                .findFragmentByTag("f" + pagerAdapter.getItemId(position));
        if (fragment != null) {
            fragment.updateProductList(filteredProducts);
        }
    }

    private void updateTabCounts(List<ProductInfoDTO> products) {
        Map<String, Long> statusCounts = products.stream()
                .collect(Collectors.groupingBy(p -> p.getApprovalStatus().getStatusCode(), Collectors.counting()));

        long approvedCount = products.stream().filter(p -> p.getApprovalStatus().getStatusCode().equals("APPROVED") && p.getQuantity_in_stock() > 0).count();
        long outOfStockCount = products.stream().filter(p -> p.getApprovalStatus().getStatusCode().equals("APPROVED") && p.getQuantity_in_stock() <= 0).count();

        for (int i = 0; i < pagerAdapter.getItemCount(); i++) {
            String tabTitle = pagerAdapter.getTabTitle(i);
            String statusCode = pagerAdapter.getStatusCode(i);
            long count;

            if (statusCode.equals("APPROVED")) {
                count = approvedCount;
            } else if (statusCode.equals("OUT_OF_STOCK")) {
                count = outOfStockCount;
            } else {
                count = statusCounts.getOrDefault(statusCode, 0L);
            }

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setText(tabTitle + " (" + count + ")");
            }
        }
    }

    @Override
    public void onEditProduct(ProductInfoDTO product) {
        Intent intent = new Intent(this, AgencyAddProductActivity.class);
        intent.putExtra("product_to_edit", product);
        startActivity(intent);
    }

    @Override
    public void onDeleteProduct(ProductInfoDTO product) {
        viewModel.deleteProduct(product.getProduct_id());
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadAllAgencyProducts();
    }
}