package com.example.dietarysupplementshop;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dietarysupplementshop.adapter.ProductAdapter;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.viewModel.ProductViewModel;

import java.util.ArrayList;

public class ShopAgencyActivity extends AppCompatActivity {
    private ProductAdapter productAdapter;
    private RecyclerView recyclerView;
    private ProductViewModel productViewModel;
    private ImageView shopAvatarImageView;
    private TextView shopNameTextView;
    private ProgressBar progressBar;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_agency);

        shopAvatarImageView = findViewById(R.id.shopAvatarImageView);
        shopNameTextView = findViewById(R.id.shopNameTextView);
        recyclerView = findViewById(R.id.recyclerViewShopProduct);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        long agencyId = getIntent().getLongExtra("SHOP_ID", -1);
        String shopName = getIntent().getStringExtra("SHOP_NAME");
        String shopAvatar = getIntent().getStringExtra("SHOP_AVATAR");
        String shopAddress = getIntent().getStringExtra("SHOP_ADDRESS");
        String shopPhone = getIntent().getStringExtra("SHOP_PHONE");
        String shopEmail = getIntent().getStringExtra("SHOP_EMAIL");

        if (agencyId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin shop", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        shopNameTextView.setText(shopName);
        if (shopAvatar != null) {
            Glide.with(this).load(shopAvatar).into(shopAvatarImageView);
        } else {
            shopAvatarImageView.setImageResource(R.drawable.logo_2);
        }

        productAdapter = new ProductAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(productAdapter);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        productViewModel.getProductByAgency(agencyId).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        if (resource.getData() != null) {
                            productAdapter.updateProducts(resource.getData());
                        }
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }
}