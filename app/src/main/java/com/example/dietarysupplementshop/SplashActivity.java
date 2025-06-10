package com.example.dietarysupplementshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.dietarysupplementshop.model.CartItem;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.token.TokenManager;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        new Handler().postDelayed(() -> {
            if (MyApplication.getInstance().getTokenManager().getAccessToken() != null) {
                AccountViewModel accountViewModel = MyApplication.getInstance().getAccountViewModel();

                accountViewModel.getAccountInfoResource().observe(this, resource -> {
                    if (resource != null && resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {

                        List<CartItem> cartItems = resource.getData().getCart_info().getCartItem();

                        if (cartItems != null && !cartItems.isEmpty()) {
                            MyApplication.getInstance().sendNotification(
                                    "Products in Cart!",
                                    "You have products in your cart. Check them out!"
                            );
                        }
                    }
                });                startActivity(new Intent(SplashActivity.this, HomepageActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, SignInActivity.class));
            }
            finish();
        }, 1000);



    }
}