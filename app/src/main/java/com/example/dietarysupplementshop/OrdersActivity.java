package com.example.dietarysupplementshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dietarysupplementshop.adapter.OrdersViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        OrdersViewPagerAdapter adapter = new OrdersViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

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
                            tab.setText("Mua lại");
                            break;
                    }
                }
        ).attach();

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
            case "REORDER":
                return 5;
            default:
                return -1;
        }
    }
}