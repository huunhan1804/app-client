package com.example.dietarysupplementshop;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dietarysupplementshop.adapter.OrderDetailAdapter;
import com.example.dietarysupplementshop.constant.Validation;
import com.example.dietarysupplementshop.model.Address;
import com.example.dietarysupplementshop.model.CartItem;
import com.example.dietarysupplementshop.model.Coupon; // Import Coupon
import com.example.dietarysupplementshop.model.OrderDetail;
import com.example.dietarysupplementshop.repositories.RealCouponRepository;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.requests.ApplyCouponRequest;
import com.example.dietarysupplementshop.requests.CheckoutRequest;
import com.example.dietarysupplementshop.requests.OrderRequest;
import com.example.dietarysupplementshop.responses.AppliedCouponResponse; // Import AppliedCouponResponse
import com.example.dietarysupplementshop.responses.OrderDetailResponse;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal; // Import BigDecimal
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

    // Voucher related views
    private RelativeLayout rlApplyCoupon;
    private TextView tvAppliedCouponInfo;
    private TextView tvCouponDiscountValue;

    private AppliedCouponResponse appliedCouponResponse = null; // Lưu trữ thông tin voucher đã áp dụng

    private Long currentCartId = null; // ID giỏ hàng để áp dụng voucher

    private static final int REQUEST_CODE_SELECT_ADDRESS = 1001;
    private static final int REQUEST_CODE_SELECT_VOUCHER = 1002;


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

        // Init Voucher related views
        rlApplyCoupon = findViewById(R.id.rl_apply_coupon);
        tvAppliedCouponInfo = findViewById(R.id.tv_applied_coupon_info);
        tvCouponDiscountValue = findViewById(R.id.tv_coupon_discount_value);

        setupAddressSection();
        loadOrderItemsFromIntent();
        setupOrderButton();
        setupVoucherSection();
    }

    private void setupAddressSection() {
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
                                .orElse(resource.getData().get(0)); // Lấy địa chỉ đầu tiên nếu không có mặc định
                        if (defaultAddress != null) {
                            fullnameText.setText(defaultAddress.getFullname());
                            phoneText.setText(defaultAddress.getPhone());
                            addressText.setText(defaultAddress.getAddress_detail());
                        }
                    } else {
                        Toast.makeText(this, "Vui lòng thêm địa chỉ để mua hàng.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, AddressInfoActivity.class);
                        startActivity(intent);
                        finish(); // Kết thúc Activity này nếu không có địa chỉ nào
                    }
                    break;
                case ERROR:
                    hideProgressBar();
                    Toast.makeText(this, resource.getMessage(), Toast.LENGTH_LONG).show();
                    finish(); // Kết thúc Activity này nếu có lỗi tải địa chỉ
                    break;
            }
        });

        ImageView addressListIcon = findViewById(R.id.addressListIcon);
        addressListIcon.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddressListActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_ADDRESS);
        });

        RelativeLayout defaultAddress = findViewById(R.id.defaultAddress);
        defaultAddress.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddressListActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_ADDRESS);
        });
    }

    private void loadOrderItemsFromIntent() {
        String selectedItemsJson = getIntent().getStringExtra("selectedItems");
        Gson gson = new Gson();
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        List<CartItem> selectedItems = gson.fromJson(selectedItemsJson, type);

        // Lấy cartId từ một CartItem bất kỳ (giả sử tất cả CartItem thuộc cùng một giỏ hàng)
        if (selectedItems != null && !selectedItems.isEmpty()) {
            currentCartId = selectedItems.get(0).getCart_item_id(); // Hoặc lấy từ AccountInformation
        }

        List<Long> cartItemIds = new ArrayList<>();
        if (selectedItems != null) {
            for (CartItem cartItem : selectedItems) {
                cartItemIds.add(cartItem.getCart_item_id());
            }
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

                            orderDetail = new ArrayList<>();
                            for (OrderDetailResponse odr : orderDetailResponses) {
                                orderDetail.add(new OrderDetail(
                                        odr.getProductInfoDTO().getProduct_id(),
                                        odr.getProductVariantDTO() != null ? odr.getProductVariantDTO().getProduct_variant_id() : null, // Check for null variant
                                        odr.getQuantity(),
                                        odr.getPrice(),
                                        odr.getSub_total()
                                ));
                            }
                            updateTotalPriceDisplay();
                        }
                    case ERROR:
                        hideProgressBar();
                        Toast.makeText(this, "Lỗi tải chi tiết đơn hàng: " + resource.getMessage(), Toast.LENGTH_LONG).show();
                        finish(); // Kết thúc Activity nếu có lỗi tải chi tiết đơn hàng
                        break;
                    case LOADING:
                        showProgressBar();
                        break;
                }
            }
        });
    }

    private void setupOrderButton() {
        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(view -> {
            if (orderDetail == null || orderDetail.isEmpty()) {
                Toast.makeText(this, "Không có sản phẩm để đặt hàng.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (fullnameText.getText().toString().isEmpty() || addressText.getText().toString().isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn địa chỉ giao hàng.", Toast.LENGTH_SHORT).show();
                return;
            }

            String shippingInfo = "Name: " + fullnameText.getText().toString().trim() + "\n" +
                    "Phone: " + phoneText.getText().toString().trim() + "\n" +
                    "Address: " + addressText.getText().toString().trim();

            // Lấy tổng tiền cuối cùng đã áp dụng voucher
            String finalTotalBill = totalPriceTextView.getText().toString().trim();

            OrderRequest orderRequest = new OrderRequest(shippingInfo, finalTotalBill, orderDetail);
            // Thêm coupon_code vào OrderRequest nếu có voucher áp dụng
            if (appliedCouponResponse != null && appliedCouponResponse.getAppliedCoupon() != null) {
                // orderRequest.setCouponCode(appliedCouponResponse.getAppliedCoupon().getCouponCode()); // Bạn cần thêm trường này vào OrderRequest
            }

            accountViewModel.addOrder(orderRequest).observe(this, orderResource -> {
                if (orderResource != null) {
                    switch (orderResource.getStatus()) {
                        case LOADING:
                            showProgressBar();
                            break;
                        case SUCCESS:
                            hideProgressBar();
                            if (orderResource.getData() != null) {
                                Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                                // Xóa các item đã mua khỏi giỏ hàng
                                // accountViewModel.deleteCartItems(cartItemIds); // Bạn cần thêm phương thức này vào AccountViewModel và API
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
    }

    private void setupVoucherSection() {
        rlApplyCoupon.setOnClickListener(v -> {
            if (currentCartId == null || currentCartId == -1L) {
                Toast.makeText(this, "Vui lòng đợi giỏ hàng được tải trước khi chọn voucher.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, VouchersActivity.class);
            intent.putExtra("CART_ID", currentCartId); // Truyền ID giỏ hàng để áp dụng voucher
            startActivityForResult(intent, REQUEST_CODE_SELECT_VOUCHER);
        });

        // Xóa voucher đã áp dụng
        tvAppliedCouponInfo.setOnClickListener(v -> {
            if (appliedCouponResponse != null) {
                new AlertDialog.Builder(this)
                        .setTitle("Hủy áp dụng mã giảm giá")
                        .setMessage("Bạn có muốn hủy áp dụng mã " + appliedCouponResponse.getAppliedCoupon().getCouponCode() + " không?")
                        .setPositiveButton("Hủy", (dialog, which) -> {
                            // Gọi API để hủy áp dụng voucher
                            // Bạn cần có CouponViewModel và phương thức removeCoupon
                            // Hoặc gọi thẳng qua repository nếu không muốn thêm dependency ViewModel vào Activity
                            RealCouponRepository.getInstance().removeCouponFromCart(
                                    new ApplyCouponRequest(appliedCouponResponse.getAppliedCoupon().getCouponCode(), currentCartId)
                            ).observe(this, resource -> {
                                if (resource.getStatus() == Resource.Status.SUCCESS) {
                                    appliedCouponResponse = null;
                                    Toast.makeText(this, "Đã hủy áp dụng mã giảm giá.", Toast.LENGTH_SHORT).show();
                                    updateTotalPriceDisplay(); // Cập nhật lại tổng tiền
                                    updateVoucherInfoUI(); // Cập nhật UI voucher
                                } else if (resource.getStatus() == Resource.Status.ERROR) {
                                    Toast.makeText(this, "Lỗi hủy áp dụng: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });
        updateVoucherInfoUI();
    }


    // Hàm này được gọi sau khi giỏ hàng/chi tiết đơn hàng đã được tải.
    // Và sau khi áp dụng/hủy voucher.
    private void updateTotalPriceDisplay() {
        BigDecimal subtotalItems = BigDecimal.ZERO;
        if (orderDetail != null) {
            for (OrderDetail od : orderDetail) {
                try {
                    // Chuyển đổi giá từ String sang BigDecimal
                    String priceCleaned = od.getPrice().replaceAll("[^\\d.]+", "");
                    BigDecimal itemPrice = new BigDecimal(priceCleaned);
                    subtotalItems = subtotalItems.add(itemPrice.multiply(BigDecimal.valueOf(od.getQuantity())));
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Lỗi định dạng giá sản phẩm.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }

        BigDecimal shippingFee = new BigDecimal("20000.00"); // Phí cố định 20.000đ

        BigDecimal currentTotal = subtotalItems.add(shippingFee);
        BigDecimal discountAmount = BigDecimal.ZERO;

        if (appliedCouponResponse != null && appliedCouponResponse.getFinalTotalPrice() != null) {
            // Nếu có phản hồi từ BE về tổng tiền cuối cùng sau khi áp dụng voucher
            totalPriceTextView.setText(Validation.formatPriceToVND(appliedCouponResponse.getFinalTotalPrice().doubleValue()));
            discountAmount = appliedCouponResponse.getOriginalTotalPrice().subtract(appliedCouponResponse.getFinalTotalPrice());
        } else {
            // Nếu không có voucher áp dụng hoặc voucher bị hủy
            totalPriceTextView.setText(Validation.formatPriceToVND(currentTotal.doubleValue()));
        }

        tvCouponDiscountValue.setText("- " + Validation.formatPriceToVND(discountAmount.doubleValue()));
        tvCouponDiscountValue.setVisibility(discountAmount.compareTo(BigDecimal.ZERO) > 0 ? View.VISIBLE : View.GONE);

        shippingFeeValue.setText(Validation.formatPriceToVND(shippingFee.doubleValue()));
    }

    private void updateVoucherInfoUI() {
        if (appliedCouponResponse != null && appliedCouponResponse.getAppliedCoupon() != null) {
            Coupon coupon = appliedCouponResponse.getAppliedCoupon();
            tvAppliedCouponInfo.setText("Đã áp dụng: " + coupon.getCouponCode());
            tvAppliedCouponInfo.setTextColor(ContextCompat.getColor(this, R.color.color_app)); // Màu xanh cho đã áp dụng
        } else {
            tvAppliedCouponInfo.setText("Chọn hoặc nhập mã");
            tvAppliedCouponInfo.setTextColor(ContextCompat.getColor(this, R.color.grey)); // Màu mặc định
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_ADDRESS && resultCode == RESULT_OK) {
            if (data != null) {
                long selectedAddressId = data.getLongExtra("selectedAddress", 0);
                if (selectedAddressId != 0) {
                    accountViewModel.getInfoAddress(selectedAddressId).observe(this, resource -> {
                        switch (resource.getStatus()) {
                            case LOADING:
                                showProgressBar();
                                break;
                            case SUCCESS:
                                hideProgressBar();
                                if (resource.getData() != null) {
                                    Address selectedAddress = resource.getData();
                                    fullnameText.setText(selectedAddress.getFullname());
                                    phoneText.setText(selectedAddress.getPhone());
                                    addressText.setText(selectedAddress.getAddress_detail());
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
        } else if (requestCode == REQUEST_CODE_SELECT_VOUCHER) {
            if (resultCode == RESULT_OK && data != null) {
                appliedCouponResponse = (AppliedCouponResponse) data.getSerializableExtra("APPLIED_COUPON_RESPONSE");
                updateTotalPriceDisplay();
                updateVoucherInfoUI();
            } else if (resultCode == RESULT_CANCELED) {
                // Người dùng hủy chọn voucher hoặc có lỗi xảy ra
                // Kiểm tra lại trạng thái giỏ hàng từ BE nếu cần, hoặc đơn giản là reset voucher
                appliedCouponResponse = null;
                updateTotalPriceDisplay();
                updateVoucherInfoUI();
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