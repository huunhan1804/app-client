package com.example.dietarysupplementshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SellerMainActivity extends AppCompatActivity {
    private TextView tvViewShop;
    private TextView tvViewOrderHistory;
    private LinearLayout llMyProducts;
    private LinearLayout llFinance;
    private LinearLayout llSalesPerformance;
    private LinearLayout sellerMarketingChannel;
    private LinearLayout llShopeeAds;
    private LinearLayout llSupportCenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_channel_seller);

        tvViewShop = findViewById(R.id.tv_view_shop);
        tvViewOrderHistory = findViewById(R.id.tv_view_order_history);
        llMyProducts = findViewById(R.id.ll_my_products);
        llFinance = findViewById(R.id.ll_finance);
        llSalesPerformance = findViewById(R.id.ll_sales_performance);
        sellerMarketingChannel = findViewById(R.id.seller_marketing_channel);
        llShopeeAds = findViewById(R.id.ll_shopee_ads);
        llSupportCenter = findViewById(R.id.ll_support_center);


        tvViewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvViewOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerMainActivity.this, SellerOrderActivity.class);
                intent.putExtra("initial_status_key", "PENDING");
                startActivity(intent);
            }
        });

        llMyProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerMainActivity.this, SellerProductActivity.class);
                startActivity(intent);
            }
        });

        llFinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerMainActivity.this, FinanceActivity.class);
                startActivity(intent);
            }
        });

        llSalesPerformance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerMainActivity.this, SalesPerformanceActivity.class);
                startActivity(intent);
            }
        });

        sellerMarketingChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(SellerMainActivity.this, ShopManagementActivity.class);
                // startActivity(intent);
            }
        });

        llShopeeAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerMainActivity.this, SellerCreateVoucherActivity.class);
                startActivity(intent);
            }
        });


        llSupportCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 Intent intent = new Intent(SellerMainActivity.this, SupportCenterActivity.class);
//                 startActivity(intent);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView tvViewCancelledOrders = findViewById(R.id.tv_view_cancelled_orders); // Đảm bảo ID này tồn tại trong layout
        tvViewCancelledOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerMainActivity.this, SellerOrderActivity.class);
                intent.putExtra("initial_status_key", "CANCELLED"); // Đi đến tab "Đơn hủy"
                startActivity(intent);
            }
        });


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView tvViewRefundsOrders = findViewById(R.id.tv_view_refunds_oders);
        if (tvViewRefundsOrders != null) {
            tvViewRefundsOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SellerMainActivity.this, SellerOrderActivity.class);
                    intent.putExtra("initial_status_key", "RETURNED");
                    startActivity(intent);
                }
            });
        }


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView tvViewPendingOrders = findViewById(R.id.tv_view_pending_orders);
        if (tvViewPendingOrders != null) {
            tvViewPendingOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SellerMainActivity.this, SellerOrderActivity.class);
                    intent.putExtra("initial_status_key", "PENDING");
                    startActivity(intent);
                }
            });
        }


//        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
//        TextView tvViewShippingOrders = findViewById(R.id.tv_view_shipping_orders);
//        if (tvViewShippingOrders != null) {
//            tvViewShippingOrders.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(SellerMainActivity.this, SellerOrderActivity.class);
//                    intent.putExtra("initial_status_key", "SHIPPING");
//                    startActivity(intent);
//                }
//            });
//        }


//        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
//        TextView tvViewReviewFeedbackOrder = findViewById(R.id.tv_view_review_feedback_oders);
//        tvViewReviewFeedbackOrders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SellerMainActivity.this, SellerOrderActivity.class);
//                intent.putExtra("initial_status_key", "CANCELLED");
//                startActivity(intent);
//            }
//        });

    }
}