package com.example.dietarysupplementshop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietarysupplementshop.HomepageActivity;

public class SellerPendingApprovalActivity extends AppCompatActivity {

    private static final long DELAY_TIME_MILLIS = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_pending_approval);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SellerPendingApprovalActivity.this, HomepageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }, DELAY_TIME_MILLIS);
    }
}