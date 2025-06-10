package com.example.dietarysupplementshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
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

    private List<OrderDetail> orderDetail;

    private FrameLayout frameLayout;
    LottieAnimationView animationView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
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
                    showProgressBar();
                    break;
                case SUCCESS:
                    hideProgressBar();
                    if (resource.getData() != null && !resource.getData().isEmpty()) {
                        Address defaultAddress = resource.getData().stream()
                                .filter(Address::getIs_default)
                                .findFirst()
                                .orElse(resource.getData().get(0));
                        fullnameText.setText(defaultAddress.getFullname());
                        phoneText.setText(defaultAddress.getPhone());
                        addressText.setText(defaultAddress.getAddress_detail());
                    }else {
                        Toast.makeText(this, "Please add an address to buy now", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, AddressInfoActivity.class);
                        startActivity(intent);
                    }
                    break;
                case ERROR:
                    hideProgressBar();
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
        assert selectedItems != null;
        for (CartItem cartItem : selectedItems) {
            cartItemIds.add(cartItem.getCart_item_id());
        }
        CheckoutRequest request = new CheckoutRequest(cartItemIds);
        accountViewModel.getOrderDetailCheckout(request).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case SUCCESS:
                        hideProgressBar();
                        if (resource.getData() != null) {
                            List<OrderDetailResponse> orderDetailResponses = resource.getData();
                            orderDetailRecyclerView = findViewById(R.id.orderDetailRecyclerView);
                            orderDetailAdapter = new OrderDetailAdapter(getApplicationContext(), orderDetailResponses);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            orderDetailRecyclerView.setLayoutManager(linearLayoutManager);
                            orderDetailRecyclerView.setAdapter(orderDetailAdapter);

                            String shippingFee = shippingFeeValue.getText().toString().replaceAll("\\D+", "");
                            double total = Double.parseDouble(shippingFee);

                            LinearLayout selectedItemsContainer = findViewById(R.id.selectedItemsContainer);
                            selectedItemsContainer.removeAllViews();

                            orderDetail = new ArrayList<>();
                            for (OrderDetailResponse orderDetailResponse : orderDetailResponses) {
                                String priceCleaned = orderDetailResponse.getPrice().replaceAll("\\D+", "");
                                double priceValue = Double.parseDouble(priceCleaned);
                                total += priceValue * orderDetailResponse.getQuantity();

                                @SuppressLint("InflateParams") LinearLayout productItem = (LinearLayout) getLayoutInflater().inflate(R.layout.selected_product_item, null);

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
                        hideProgressBar();
                        break;
                    case LOADING:
                        showProgressBar();
                        break;
                }
            }
        });

        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(view -> {
            String shippingInfo  = "Name: " + fullnameText.getText().toString().trim() + "\n" +
                    "Phone: " + phoneText.getText().toString().trim() + "\n" +
                    "Address: " + addressText.getText().toString().trim();
            OrderRequest orderRequest = new OrderRequest(shippingInfo, totalPriceTextView.getText().toString().trim(),orderDetail);
            accountViewModel.addOrder(orderRequest).observe(this, orderResource -> {
                if (orderResource != null) {
                    switch (orderResource.getStatus()) {
                        case LOADING:
                            showProgressBar();
                            break;
                        case SUCCESS:
                            hideProgressBar();
                            if (orderResource.getData() != null) {
                                Intent intent = new Intent(getApplicationContext(), OrderSuccessActivity.class);
                                intent.putExtra("orderId", orderResource.getData().getOrder_id());
                                startActivity(intent);
                                finish();
                            }
                            break;
                        case ERROR:
                            hideProgressBar();
                            Toast.makeText(this, orderResource.getMessage(), Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            });
        });

        ImageView addressListIcon = findViewById(R.id.addressListIcon);
        addressListIcon.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddressListActivity.class);
            startActivityForResult(intent, 1001);
        });

        RelativeLayout defaultAddress = findViewById(R.id.defaultAddress);
        defaultAddress.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddressListActivity.class);
            startActivityForResult(intent, 1001);
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            if (data != null) {
                long selectedAddress = data.getLongExtra("selectedAddress", 0);
                if (selectedAddress != 0) {
                    accountViewModel.getInfoAddress(selectedAddress).observe(this, resource -> {
                        switch (resource.getStatus()) {
                            case LOADING:
                                showProgressBar();
                                break;
                            case SUCCESS:
                                hideProgressBar();
                                if(resource.getData() != null){
                                    Address defaultAddress = resource.getData();
                                    if (defaultAddress != null) {
                                        fullnameText.setText(defaultAddress.getFullname());
                                        phoneText.setText(defaultAddress.getPhone());
                                        addressText.setText(defaultAddress.getAddress_detail());
                                    }
                                }
                                break;
                            case ERROR:
                                hideProgressBar();
                                Toast.makeText(this, resource.getMessage(), Toast.LENGTH_LONG).show();
                                break;
                        }
                    });
                }

            }
        }

    }
    public void showProgressBar() {
        frameLayout = findViewById(R.id.frameLoading);
        animationView = findViewById(R.id.animationView);
        frameLayout.setVisibility(View.VISIBLE);
        animationView.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        frameLayout = findViewById(R.id.frameLoading);
        animationView = findViewById(R.id.animationView);
        frameLayout.setVisibility(View.GONE);
        animationView.setVisibility(View.GONE);
    }
}