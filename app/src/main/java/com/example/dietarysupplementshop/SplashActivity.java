package com.example.dietarysupplementshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dietarysupplementshop.responses.AccountInformation;
import com.example.dietarysupplementshop.model.CartItem;


import com.example.dietarysupplementshop.token.TokenManager;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private AccountViewModel accountViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_splash);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        new Handler(Looper.getMainLooper()).postDelayed(this::checkUserStatus, 1500);
    }

    private void checkUserStatus() {
        TokenManager tokenManager = MyApplication.getInstance().getTokenManager();

        if (tokenManager.getAccessToken() != null && !tokenManager.getAccessToken().isEmpty()) {
            observeAccountInfo();
        } else {
            navigateTo(SignInActivity.class);
        }
    }

    private void observeAccountInfo() {
        accountViewModel.getAccountInfo().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    if (resource.getData() != null) {
                        handleLoginSuccess(resource.getData());
                    }
                    break;
                case ERROR:
                    Log.e(TAG, "Lỗi lấy thông tin tài khoản: " + resource.getMessage());
                    Toast.makeText(getApplicationContext(), "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
                    navigateTo(SignInActivity.class);
                    break;
            }
        });
    }

    private void handleLoginSuccess(AccountInformation data) {
        List<CartItem> cartItems = data.getCart_info().getCartItem();
        if (cartItems != null && !cartItems.isEmpty()) {
            MyApplication.getInstance().sendNotification(
                    "Sản phẩm trong giỏ hàng!",
                    "Bạn có sản phẩm trong giỏ hàng. Hãy kiểm tra ngay!"
            );
        }

        String status = data.getStatus();
        if (status == null) {
            status = "NOT_REGISTERED";
        }

        SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
        boolean hasAcknowledged = prefs.getBoolean("SELLER_APPROVAL_ACKNOWLEDGED", false);

        Class<?> destinationActivity;

        switch (status) {
            case "APPROVED":
                if (hasAcknowledged) {
                    destinationActivity = SellerMainActivity.class;
                } else {
                    destinationActivity = SellerRegistrationStatusActivity.class;
                }
                break;
            case "PENDING":
            case "REJECTED":
                destinationActivity = SellerRegistrationStatusActivity.class;
                break;

            case "NOT_REGISTERED":
            default:
                destinationActivity = HomepageActivity.class;
                break;
        }

        navigateTo(destinationActivity);
    }
    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(SplashActivity.this, destination);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}