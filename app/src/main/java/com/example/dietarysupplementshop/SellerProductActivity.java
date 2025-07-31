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

import com.example.dietarysupplementshop.adapter.ProductSellerAdapter;
import com.example.dietarysupplementshop.adapter.SellerProductPagerAdapter;
import com.example.dietarysupplementshop.model.ProductSeller;
import com.example.dietarysupplementshop.viewModel.SellerProductViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SellerProductActivity extends AppCompatActivity implements ProductSellerAdapter.OnProductActionListener {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private SellerProductPagerAdapter pagerAdapter;
    private SellerProductViewModel viewModel;
    private ProgressBar progressBar;
    private final Map<String, ProductListFragment> activeFragments = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.productsViewPager);
        Button addNewProductButton = findViewById(R.id.addNewProductButton);
        ImageView backButton = findViewById(R.id.backButton);
        ImageView searchButton = findViewById(R.id.searchButton);
        ImageView chatButton = findViewById(R.id.chatButton);
        progressBar = findViewById(R.id.progressBar);

        viewModel = new ViewModelProvider(this).get(SellerProductViewModel.class);

        pagerAdapter = new SellerProductPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(pagerAdapter.getTabTitle(position) + " (0)")
        ).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                updateFragmentData();
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
            Intent intent = new Intent(SellerProductActivity.this, SellerAddProductActivity.class);
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

        viewModel.allSellerProducts.observe(this, products -> {
            updateTabCounts(products);
            updateFragmentData();
        });
    }

    private void updateTabCounts(List<ProductSeller> products) {
        Map<String, Long> statusCounts = products.stream()
                .collect(Collectors.groupingBy(ProductSeller::getProductStatus, Collectors.counting()));

        for (int i = 0; i < pagerAdapter.getItemCount(); i++) {
            String tabTitle = pagerAdapter.getTabTitle(i);
            long count = statusCounts.getOrDefault(tabTitle, 0L);
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setText(tabTitle + " (" + count + ")");
            }
        }
    }

    private void updateFragmentData() {
        List<ProductSeller> currentProducts = viewModel.allSellerProducts.getValue();
        if (currentProducts == null) {
            return;
        }


        int currentPosition = viewPager.getCurrentItem();
        String currentStatus = pagerAdapter.getTabTitle(currentPosition);


        ProductListFragment fragment = (ProductListFragment) getSupportFragmentManager()
                .findFragmentByTag("f" + pagerAdapter.getItemId(currentPosition));

        if (fragment != null) {
            List<ProductSeller> filteredProducts = filterProductsByStatus(currentStatus, currentProducts);
            fragment.updateProductList(filteredProducts);
        }
    }


    private List<ProductSeller> filterProductsByStatus(String status, List<ProductSeller> allProducts) {
        return allProducts.stream()
                .filter(p -> p.getProductStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public void onEditProduct(ProductSeller product) {
        Intent intent = new Intent(this, SellerAddProductActivity.class);
        intent.putExtra("product_to_edit", product); // Pass the product object
        startActivity(intent);
    }

    @Override
    public void onHideProduct(ProductSeller product) {
        viewModel.updateProductStatus(product.getProductId(), "Ẩn");
    }

    @Override
    public void onUnHideProduct(ProductSeller product) {
        viewModel.updateProductStatus(product.getProductId(), "Còn hàng");
    }

    @Override
    public void onDeleteProduct(ProductSeller product) {

    }

//    @Override
//    public void onDeleteProduct(ProductSeller product) {
//
//        ProductApiService.getInstance().getAllSellerProducts(new ProductApiService.ProductCallback<List<ProductSeller>>() {
//            @Override
//            public void onSuccess(List<ProductSeller> result) {
//                result.removeIf(p -> p.getProductId().equals(product.getProductId()));
//                viewModel.loadAllSellerProducts(); // Force refresh
//                Toast.makeText(SellerProductActivity.this, "Đã xóa (mô phỏng): " + product.getProductName(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                Toast.makeText(SellerProductActivity.this, "Lỗi khi xóa (mô phỏng): " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void onViewProductDetail(ProductSeller product) {
        Toast.makeText(this, "Chuyển đến chi tiết sản phẩm: " + product.getProductName(), Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, ProductInfoActivity.class);
        // intent.putExtra("product_id", product.getProductId());
        // startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadAllSellerProducts();
    }
}