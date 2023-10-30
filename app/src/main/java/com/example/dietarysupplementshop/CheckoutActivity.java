package com.example.dietarysupplementshop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.adapter.OrderDetailAdapter;
import com.example.dietarysupplementshop.constant.Validation;
import com.example.dietarysupplementshop.model.Address;
import com.example.dietarysupplementshop.model.CartItem;
import com.example.dietarysupplementshop.model.OrderDetail;
import com.example.dietarysupplementshop.requests.CheckoutRequest;
import com.example.dietarysupplementshop.requests.OrderRequest;
import com.example.dietarysupplementshop.responses.OrderDetailResponse;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private AccountViewModel accountViewModel;

    private OrderDetailAdapter orderDetailAdapter;

    private RecyclerView orderDetailRecyclerView;

    private TextView totalPriceTextView, shippingFeeValue;

    private TextView fullnameText, phoneText, addressText;

    private Button continueButton;

    private List<OrderDetail> orderDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        accountViewModel = MyApplication.getInstance().getAccountViewModel();

        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        shippingFeeValue = findViewById(R.id.shippingFeeValue);

        fullnameText = findViewById(R.id.fullnameText);
        phoneText = findViewById(R.id.phoneText);
        addressText = findViewById(R.id.addressText);

        accountViewModel.getAddressListResource().observe(this, resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    if (resource.getData() != null) {
                        Address defaultAddress = resource.getData().stream()
                                .filter(Address::getIs_default)
                                .findFirst()
                                .orElse(null);
                        fullnameText.setText(defaultAddress.getFullname());
                        phoneText.setText(defaultAddress.getPhone());
                        addressText.setText(defaultAddress.getAddress_detail());
                    }
                    break;
                case ERROR:
                    Toast.makeText(this, resource.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        });

        String selectedItemsJson = getIntent().getStringExtra("selectedItems");
        Gson gson = new Gson();
        Type type = new TypeToken<List<CartItem>>() {
        }.getType();
        List<CartItem> selectedItems = gson.fromJson(selectedItemsJson, type);
        List<Long> cartItemIds = new ArrayList<>();
        for (CartItem cartItem : selectedItems) {
            cartItemIds.add(cartItem.getCart_item_id());
        }
        CheckoutRequest request = new CheckoutRequest(cartItemIds);
        accountViewModel.getOrderDetailCheckout(request).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case SUCCESS:
                        if (resource.getData() != null) {
                            List<OrderDetailResponse> orderDetailResponses = resource.getData();
                            orderDetailRecyclerView = findViewById(R.id.orderDetailRecyclerView);
                            orderDetailAdapter = new OrderDetailAdapter(getApplicationContext(), orderDetailResponses);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            orderDetailRecyclerView.setLayoutManager(linearLayoutManager);
                            orderDetailRecyclerView.setAdapter(orderDetailAdapter);

                            String shippingFee = shippingFeeValue.getText().toString().replaceAll("[^\\d]+", "");
                            double shippingFeeValue = Double.parseDouble(shippingFee);
                            double total = shippingFeeValue;

                            LinearLayout selectedItemsContainer = findViewById(R.id.selectedItemsContainer);
                            selectedItemsContainer.removeAllViews();

                            orderDetail = new ArrayList<>();
                            for (OrderDetailResponse orderDetailResponse : orderDetailResponses) {
                                String priceCleaned = orderDetailResponse.getPrice().replaceAll("\\D+", "");
                                double priceValue = Double.parseDouble(priceCleaned);
                                total += priceValue * orderDetailResponse.getQuantity();

                                LinearLayout productItem = (LinearLayout) getLayoutInflater().inflate(R.layout.selected_product_item, null);

                                TextView productName = productItem.findViewById(R.id.productNameTextView);
                                TextView productQuantity = productItem.findViewById(R.id.productQuantityTextView);
                                TextView productPrice = productItem.findViewById(R.id.productPriceTextView);

                                productName.setText(orderDetailResponse.getProductInfoDTO().getProduct_name());
                                productQuantity.setText(orderDetailResponse.getQuantity() + " x");

                                productPrice.setText(orderDetailResponse.getPrice());
                                selectedItemsContainer.addView(productItem);

                                orderDetail.add(new OrderDetail(orderDetailResponse.getProductInfoDTO().getProduct_id(), orderDetailResponse.getProductVariantDTO().getProduct_variant_id(), orderDetailResponse.getQuantity(), orderDetailResponse.getPrice(), orderDetailResponse.getSub_total()));
                            }
                            totalPriceTextView.setText(Validation.formatPriceToVND(total));
                        }
                    case ERROR:

                        break;
                    case LOADING:
                        break;
                }
            }
        });

        continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(view -> {
            String shippingInfo  = "Name: " + fullnameText.getText().toString().trim() + "\n" +
                    "Phone: " + phoneText.getText().toString().trim() + "\n" +
                    "Address: " + addressText.getText().toString().trim();
            OrderRequest orderRequest = new OrderRequest(shippingInfo, totalPriceTextView.getText().toString().trim(),orderDetail);
            accountViewModel.addOrder(orderRequest).observe(this, orderResource -> {
                if (orderResource != null) {
                    switch (orderResource.getStatus()) {
                        case LOADING:
                            break;
                        case SUCCESS:
                            if (orderResource.getData() != null) {
                                Intent intent = new Intent(getApplicationContext(), OrderSuccessActivity.class);
                                intent.putExtra("orderId", orderResource.getData().getOrder_id());
                                startActivity(intent);
                                finish();
                            }
                            break;
                        case ERROR:
                            Toast.makeText(this, orderResource.getMessage(), Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            });
        });


    }
}