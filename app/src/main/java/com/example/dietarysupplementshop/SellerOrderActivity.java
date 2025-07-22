package com.example.dietarysupplementshop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.dietarysupplementshop.adapter.SellerOrderPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SellerOrderActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        ImageView backButton = findViewById(R.id.backButton);
        ImageView searchButton = findViewById(R.id.searchButton);
        ImageView chatButton = findViewById(R.id.chatButton);

        SellerOrderPagerAdapter pagerAdapter = new SellerOrderPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Chờ Xác nhận");
                            break;
                        case 1:
                            tab.setText("Đang giao");
                            break;
                        case 2:
                            tab.setText("Đã giao");
                            break;
                        case 3:
                            tab.setText("Đơn hủy");
                            break;
                        case 4:
                            tab.setText("Trả hàng/hoàn tiền");
                            break;
                        case 5:
                            tab.setText("Giao hàng thất bại");
                            break;
                    }
                }).attach();

        backButton.setOnClickListener(v -> onBackPressed());

        searchButton.setOnClickListener(v -> {
            Toast.makeText(SellerOrderActivity.this, "Chức năng tìm kiếm", Toast.LENGTH_SHORT).show();
        });

        chatButton.setOnClickListener(v -> {
            Toast.makeText(SellerOrderActivity.this, "Mở màn hình Chat", Toast.LENGTH_SHORT).show();
        });

        handleInitialTab(viewPager);
    }


    private void handleInitialTab(ViewPager2 viewPager) {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("initial_status_key")) {
            String statusKey = intent.getStringExtra("initial_status_key");
            int tabIndex = getIndexForStatus(statusKey);
            if (tabIndex != -1) {
                viewPager.setCurrentItem(tabIndex, false);
            }
        }
    }

    private int getIndexForStatus(String statusKey) {
        if (statusKey == null) return -1;
        switch (statusKey) {
            case "PENDING":
                return 0;
            case "SHIPPING":
                return 1;
            case "DELIVERED":
                return 2;
            case "CANCELLED":
                return 3;
            case "RETURNED":
                return 4;
            case "DELIVERY_FAILED":
                return 5;
            default:
                return -1;
        }
    }
}