package com.example.dietarysupplementshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log; // <-- Đảm bảo import này có
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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

            Log.d(TAG, "observeAccountInfo: Status of resource = " + resource.getStatus());

            switch (resource.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    if (resource.getData() != null) {
                        Log.d(TAG, "observeAccountInfo: SUCCESS - Data received from backend:");
                        Log.d(TAG, " - Account ID: " + resource.getData().getId());
                        Log.d(TAG, " - Role Code: " + resource.getData().getRole_code());
                        Log.d(TAG, " - Status (Seller Reg): " + resource.getData().getStatus());
                        Log.d(TAG, " - Rejection Reason: " + resource.getData().getRejectionReason());
                        handleLoginSuccess(resource.getData());
                    } else {
                        Log.w(TAG, "observeAccountInfo: SUCCESS - resource.getData() is null.");
                    }
                    break;
                case ERROR:
                    Log.e(TAG, "observeAccountInfo: ERROR - " + resource.getMessage());
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
            Log.w(TAG, "handleLoginSuccess: Status from backend was null, set to NOT_REGISTERED.");
        }

        Log.d(TAG, "handleLoginSuccess: Final status for switch = " + status);

        SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
        boolean hasAcknowledged = prefs.getBoolean("SELLER_APPROVAL_ACKNOWLEDGED", false);
        Log.d(TAG, "handleLoginSuccess: hasAcknowledged = " + hasAcknowledged);

        Class<?> destinationActivity;

        switch (status) {
            case "APPROVED":
                if (hasAcknowledged) {
                    destinationActivity = SellerMainActivity.class;
                    Log.i(TAG, "handleLoginSuccess: Status APPROVED & Acknowledged. Navigating to SellerMainActivity.");
                } else {
                    destinationActivity = SellerRegistrationStatusActivity.class;
                    Log.i(TAG, "handleLoginSuccess: Status APPROVED & Not Acknowledged. Navigating to SellerRegistrationStatusActivity.");
                }
                break;
            case "PENDING":
                destinationActivity = SellerRegistrationStatusActivity.class;
                Log.i(TAG, "handleLoginSuccess: Status PENDING. Navigating to SellerRegistrationStatusActivity.");
                break;
            case "REJECTED":
                destinationActivity = SellerRegistrationStatusActivity.class;
                Log.i(TAG, "handleLoginSuccess: Status REJECTED. Navigating to SellerRegistrationStatusActivity.");
                break;
            case "NOT_REGISTERED":
            default:
                destinationActivity = HomepageActivity.class;
                Log.i(TAG, "handleLoginSuccess: Status NOT_REGISTERED or default. Navigating to HomepageActivity.");
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