package com.example.dietarysupplementshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AgencyWelcomeRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_registration_welcome);

        Button startButton = findViewById(R.id.startButton); //
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgencyWelcomeRegistrationActivity.this, AgencyRegistrationFormActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }
}