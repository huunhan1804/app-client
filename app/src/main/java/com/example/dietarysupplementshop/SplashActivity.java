package com.example.dietarysupplementshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.dietarysupplementshop.token.TokenManager;

public class SplashActivity extends AppCompatActivity {

    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tokenManager = new TokenManager(getApplicationContext());
        new Handler().postDelayed(() -> {
            if (tokenManager.getAccessToken() != null) {
                startActivity(new Intent(SplashActivity.this, HomepageActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, SignInActivity.class));
            }
            finish();
        }, 3000);
    }
}