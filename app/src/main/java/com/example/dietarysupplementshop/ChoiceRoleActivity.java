package com.example.dietarysupplementshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.SellerRegistrationActivity;

public class ChoiceRoleActivity extends AppCompatActivity {

    private CardView buyerCard;
    private CardView sellerCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_role);

        buyerCard = findViewById(R.id.buyerCard);
        sellerCard = findViewById(R.id.sellerCard);

        buyerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceRoleActivity.this, HomepageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sellerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceRoleActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}