package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.dietarysupplementshop.adapter.ProductAdapter; // Dùng lại adapter sản phẩm
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.viewModel.ShopViewModel;
import java.util.ArrayList;

public class ShopAgencyActivity extends AppCompatActivity {

    private ShopViewModel shopViewModel;
    private ImageView backArrow, shopAvatar;
    private TextView agencyName;
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_agency);

        initViews();
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        long shopId = getIntent().getLongExtra("SHOP_ID", -1);
        String shopName = getIntent().getStringExtra("SHOP_NAME");

        if (shopName != null) {
            agencyName.setText(shopName);
        }

        if (shopId != -1) {
            setupRecyclerView();
            observeViewModel(shopId);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin shop", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        backArrow = findViewById(R.id.back_arrow_shop_agency);
        shopAvatar = findViewById(R.id.shopAvatarImageView);
        agencyName = findViewById(R.id.agency_name);
        productRecyclerView = findViewById(R.id.product_recycler_view);

        backArrow.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(new ArrayList<>(), this);
        productRecyclerView.setAdapter(productAdapter);
    }

    private void observeViewModel(long shopId) {
        shopViewModel.getShopDetails(shopId).observe(this, resource -> {
            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                agencyName.setText(resource.getData().getShopName());
                Glide.with(this).load(resource.getData().getAvatarUrl()).into(shopAvatar);
            }
        });

        shopViewModel.getShopProducts(shopId, 0).observe(this, resource -> {
            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                productAdapter.updateProducts(resource.getData().getContent());
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                Toast.makeText(this, resource.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}