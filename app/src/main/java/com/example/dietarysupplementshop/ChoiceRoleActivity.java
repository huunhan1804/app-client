package com.example.dietarysupplementshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ChoiceRoleActivity extends AppCompatActivity {

    private CardView customerCard;
    private CardView agencyCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_role);

        customerCard = findViewById(R.id.customerCard);
        agencyCard = findViewById(R.id.agencyCard);

        customerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceRoleActivity.this, HomepageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        agencyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceRoleActivity.this, AgencyWelcomeRegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}