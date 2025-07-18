package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton; // Import ImageButton

import androidx.appcompat.app.AppCompatActivity; // Import AppCompatActivity

import com.example.dietarysupplementshop.R;

public class MembershipActivity extends AppCompatActivity {

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        btnBack = findViewById(R.id.btn_back);

        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}