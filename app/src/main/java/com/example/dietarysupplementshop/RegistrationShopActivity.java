package com.example.dietarysupplementshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietarysupplementshop.model.ShopRegistrationData; // Import ShopRegistrationData
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegistrationShopActivity extends AppCompatActivity {

    private TextInputLayout tilShopName;
    private TextInputEditText etShopName;
    private TextInputLayout tilAddress;
    private TextInputEditText etAddress;
    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private TextInputLayout tilPhoneNumber;
    private TextInputEditText etPhoneNumber;

    private ShopRegistrationData shopRegistrationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shop_information_registration);

        shopRegistrationData = new ShopRegistrationData();

        tilShopName = findViewById(R.id.til_shop_name);
        etShopName = findViewById(R.id.et_shop_name);
        tilAddress = findViewById(R.id.til_address);
        etAddress = findViewById(R.id.et_address);
        tilEmail = findViewById(R.id.til_email);
        etEmail = findViewById(R.id.et_email);
        tilPhoneNumber = findViewById(R.id.til_phone_number);
        etPhoneNumber = findViewById(R.id.et_phone_number);


        loadShopInformation();

        Button btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAndCollectData()) {


                    Toast.makeText(RegistrationShopActivity.this, "Thông tin cửa hàng hợp lệ!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegistrationShopActivity.this, ShopTaxInformationRegistrationActivity.class);
                    intent.putExtra("shopRegistrationData", shopRegistrationData);
                    startActivity(intent);
                    // finish();
                }
            }
        });

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.thong_tin_shop_title);
    }


    private void loadShopInformation() {
    }


    private boolean validateAndCollectData() {
        boolean isValid = true;

        String shopName = etShopName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        if (shopName.isEmpty()) {
            tilShopName.setError("Vui lòng nhập tên cửa hàng.");
            isValid = false;
        } else {
            tilShopName.setError(null);
            shopRegistrationData.setShopName(shopName);
        }

        if (address.isEmpty()) {
            tilAddress.setError("Vui lòng nhập địa chỉ lấy hàng.");
            isValid = false;
        } else {
            tilAddress.setError(null);
            shopRegistrationData.setAddress(address);
        }

        if (email.isEmpty()) {
            tilEmail.setError("Vui lòng nhập địa chỉ email.");
            isValid = false;
        } else if (!isValidEmail(email)) {
            tilEmail.setError("Địa chỉ email không hợp lệ.");
            isValid = false;
        } else {
            tilEmail.setError(null);
            shopRegistrationData.setEmail(email);
        }

        if (phoneNumber.isEmpty()) {
            tilPhoneNumber.setError("Vui lòng nhập số điện thoại.");
            isValid = false;
        } else if (!isValidPhoneNumber(phoneNumber)) {
            tilPhoneNumber.setError("Số điện thoại không hợp lệ (ít nhất 10 chữ số).");
            isValid = false;
        } else {
            tilPhoneNumber.setError(null);
            shopRegistrationData.setPhoneNumber(phoneNumber);
        }

        return isValid;
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.length() >= 10 && TextUtils.isDigitsOnly(phone);
    }
}