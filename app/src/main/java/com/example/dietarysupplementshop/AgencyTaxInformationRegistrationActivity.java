package com.example.dietarysupplementshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietarysupplementshop.model.AgencyRegistrationData;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AgencyTaxInformationRegistrationActivity extends AppCompatActivity {

    private TextInputLayout tilTaxNumber;
    private TextInputEditText etTaxNumber;
    private AgencyRegistrationData agencyRegistrationData;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shop_tax_information_registration);

        agencyRegistrationData = (AgencyRegistrationData) getIntent().getSerializableExtra("agencyRegistrationData");
        if (agencyRegistrationData == null) {
            agencyRegistrationData = new AgencyRegistrationData();
        }

        tilTaxNumber = findViewById(R.id.til_tax_number);
        etTaxNumber = findViewById(R.id.tax_number);

        Button btnNext = findViewById(R.id.btn_next);
        Button btnBack = findViewById(R.id.btn_back);
        ImageView backArrow = findViewById(R.id.back_arrow);

        btnNext.setOnClickListener(v -> {
            if (validateAndCollectData()) {
                Intent intent = new Intent(this, AgencyIdentifierInformationRegistrationActivity.class);
                intent.putExtra("agencyRegistrationData", agencyRegistrationData);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(v -> onBackPressed());
        backArrow.setOnClickListener(v -> onBackPressed());
    }

    private boolean validateAndCollectData() {
        String taxNumberString = etTaxNumber.getText().toString().trim();

        if (taxNumberString.isEmpty()) {
            tilTaxNumber.setError("Vui lòng nhập mã số thuế cá nhân.");
            return false;
        } else if (taxNumberString.length() != 10 && taxNumberString.length() != 13) {
            tilTaxNumber.setError("Mã số thuế không hợp lệ (phải có 10 hoặc 13 chữ số).");
            return false;
        } else {
            tilTaxNumber.setError(null);
        }

        agencyRegistrationData.setTaxNumber(taxNumberString);

        return true;
    }
}