package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.adapter.OrderDetailAdapter;
import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        ImageButton backButton = findViewById(R.id.backButton);
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView orderStatusValue = findViewById(R.id.orderStatusValue);
        TextView orderIdValue = findViewById(R.id.orderIdValue);
        TextView orderTimeValue = findViewById(R.id.orderTimeValue);
        TextView fullnameText = findViewById(R.id.fullnameText);
        TextView phoneText = findViewById(R.id.phoneText);
        TextView addressText = findViewById(R.id.addressText);
        TextView shippingFeeValue = findViewById(R.id.shippingFeeValue);
        TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);
        recyclerView = findViewById(R.id.orderDetailRecyclerView);

        AccountViewModel accountViewModel = MyApplication.getInstance().getAccountViewModel();
        long orderId = getIntent().getLongExtra("orderId", 0);
        if(orderId != 0){
            accountViewModel.getOrderInfo(orderId).observe(this, resource -> {
                if (resource != null) {
                    switch (resource.getStatus()) {
                        case SUCCESS:
                            if (resource.getData() != null) {
                                Order order = resource.getData();
                                titleTextView.setText("Order Details");
                                orderIdValue.setText(String.valueOf(order.getOrder_id()));

                                Date orderDate = order.getOrder_date();
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss MMM dd yyyy");
                                String baseFormattedDate = sdf.format(orderDate);

                                int day = Integer.parseInt(new SimpleDateFormat("dd").format(orderDate));
                                String daySuffix = getDaySuffix(day);

                                String formattedDate = baseFormattedDate.replaceFirst(" " + day + " ", " " + day + daySuffix + " ");
                                orderTimeValue.setText(formattedDate);
                                orderStatusValue.setText(order.getOrder_status());

                                fullnameText.setText(order.getAddress_info().getFullname());
                                phoneText.setText(order.getAddress_info().getPhone());
                                addressText.setText(order.getAddress_info().getAddress_detail());

                                shippingFeeValue.setText("20.000 đ");
                                totalPriceTextView.setText(order.getTotalBill());

                                backButton.setOnClickListener(v -> finish());

                                recyclerView = findViewById(R.id.orderDetailRecyclerView);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(new OrderDetailAdapter(getApplicationContext(), order.getOrder_detail()));
                            }

                        case ERROR:

                            break;
                        case LOADING:
                            break;
                    }
                }
            });
        }
    }

    private String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

}