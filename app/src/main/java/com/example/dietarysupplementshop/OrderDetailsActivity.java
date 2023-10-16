package com.example.dietarysupplementshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dietarysupplementshop.adapter.OrderDetailAdapter;
import com.example.dietarysupplementshop.adapter.Product2Adapter;
import com.example.dietarysupplementshop.adapter.ProductAdapter;
import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.model.OrderDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private OrderDetailAdapter orderDetailAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        String orderId = getIntent().getStringExtra("orderId");
        Order order = getOrderInfo(orderId);


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

        titleTextView.setText("Order Details");
        orderIdValue.setText(String.valueOf(order.getOrderId()));

        Date orderDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss MMM dd yyyy");
        String baseFormattedDate = sdf.format(orderDate);

        int day = Integer.parseInt(new SimpleDateFormat("dd").format(orderDate));
        String daySuffix = getDaySuffix(day);

        String formattedDate = baseFormattedDate.replaceFirst(" " + day + " ", " " + day + daySuffix + " ");
        orderTimeValue.setText(formattedDate);
        orderStatusValue.setText(order.getOrderStatus());

        String addressDetail = order.getAddressDetail();

        String name = addressDetail.substring(addressDetail.indexOf("Name: ") + 6, addressDetail.indexOf(", Phone:")).trim();
        String phone = addressDetail.substring(addressDetail.indexOf("Phone: ") + 7, addressDetail.indexOf(", Address:")).trim();
        String address = addressDetail.substring(addressDetail.indexOf("Address: ") + 9).trim();

        fullnameText.setText(name);
        phoneText.setText(phone);
        addressText.setText(address);


        shippingFeeValue.setText("20.000 đ");
        totalPriceTextView.setText(order.getTotalPrice());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.orderDetailRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new OrderDetailAdapter(getApplicationContext(), order.getOrderDetails()));


    }

    private Order getOrderInfo(String orderId){
        return new Order(1,"23/02/2023", "1.400.000 đ", "PENDING_PAYMENT", "Name: Trần Quang Quí, Phone: 0945605514, Address: Ấp Phong Lưu, Xã Tân Hưng, Huyện Cái Nước, Tỉnh Cà Mau", OrderedFragment.getOrderDetailExample());
    }

    private String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }

}