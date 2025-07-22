package com.example.dietarysupplementshop;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dietarysupplementshop.model.ShopRegistrationData;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ShopIdentifierInformationRegistrationActivity extends AppCompatActivity {

    private ShopRegistrationData shopRegistrationData;

    private TextInputEditText etFullName, etDateOfBirth, etIdCardNumber, etDateOfIssue, etPlaceOfIssue;
    private AutoCompleteTextView actvGender;
    private ImageView ivIdFrontPreview, ivIdBackPreview;
    private Uri idFrontImageUri, idBackImageUri;
    private ActivityResultLauncher<String> pickIdFrontLauncher;
    private ActivityResultLauncher<String> pickIdBackLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shop_identifier_information_registration);

        shopRegistrationData = (ShopRegistrationData) getIntent().getSerializableExtra("shopRegistrationData");
        if (shopRegistrationData == null) {
            Toast.makeText(this, "Lỗi: Mất dữ liệu đăng ký.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (savedInstanceState != null) {
            idFrontImageUri = savedInstanceState.getParcelable("idFrontImageUri");
            idBackImageUri = savedInstanceState.getParcelable("idBackImageUri");
            if (idFrontImageUri != null) ivIdFrontPreview.setImageURI(idFrontImageUri);
            if (idBackImageUri != null) ivIdBackPreview.setImageURI(idBackImageUri);
        }

        bindViews();
        setupLaunchers();
        setupListeners();
        setupInitialState();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("idFrontImageUri", idFrontImageUri);
        outState.putParcelable("idBackImageUri", idBackImageUri);
    }

    private void bindViews() {
        findViewById(R.id.personal_info_section).setVisibility(View.VISIBLE);

        etFullName = findViewById(R.id.et_full_name);
        etDateOfBirth = findViewById(R.id.et_date_of_birth);
        actvGender = findViewById(R.id.actv_gender);
        etIdCardNumber = findViewById(R.id.et_id_card_number);
        etDateOfIssue = findViewById(R.id.et_date_of_issue);
        etPlaceOfIssue = findViewById(R.id.et_place_of_issue);
        ivIdFrontPreview = findViewById(R.id.iv_id_front_preview);
        ivIdBackPreview = findViewById(R.id.iv_id_back_preview);
    }

    private void setupListeners() {
        etDateOfBirth.setOnClickListener(v -> showDatePickerDialog(etDateOfBirth));
        etDateOfIssue.setOnClickListener(v -> showDatePickerDialog(etDateOfIssue));

        findViewById(R.id.cv_upload_id_front).setOnClickListener(v -> pickIdFrontLauncher.launch("image/*"));
        findViewById(R.id.cv_upload_id_back).setOnClickListener(v -> pickIdBackLauncher.launch("image/*"));

        findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (validateAndCollectData()) {
                Intent intent = new Intent(this, ShopCertificatesInformationRegistrationActivity.class);
                intent.putExtra("shopRegistrationData", shopRegistrationData);
                intent.putExtra("idFrontImageUri", idFrontImageUri);
                intent.putExtra("idBackImageUri", idBackImageUri);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
    }

    private void setupLaunchers() {
        pickIdFrontLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                idFrontImageUri = uri;
                ivIdFrontPreview.setImageURI(uri);
            }
        });
        pickIdBackLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                idBackImageUri = uri;
                ivIdBackPreview.setImageURI(uri);
            }
        });
    }

    private void setupInitialState() {
        String[] genders = {"Nam", "Nữ", "Khác"};
        actvGender.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genders));
        ((TextView) findViewById(R.id.toolbar_title)).setText("Thông Tin Định Danh");
    }

    private boolean validateAndCollectData() {
        shopRegistrationData.setFullName(Objects.requireNonNull(etFullName.getText()).toString().trim());
        shopRegistrationData.setDateOfBirth(Objects.requireNonNull(etDateOfBirth.getText()).toString().trim());
        shopRegistrationData.setGender(actvGender.getText().toString().trim());
        shopRegistrationData.setIdCardNumber(Objects.requireNonNull(etIdCardNumber.getText()).toString().trim());
        shopRegistrationData.setDateOfIssue(Objects.requireNonNull(etDateOfIssue.getText()).toString().trim());
        shopRegistrationData.setPlaceOfIssue(Objects.requireNonNull(etPlaceOfIssue.getText()).toString().trim());

        if (idFrontImageUri == null) {
            Toast.makeText(this, "Vui lòng tải ảnh mặt trước CCCD.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (idBackImageUri == null) {
            Toast.makeText(this, "Vui lòng tải ảnh mặt sau CCCD.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void showDatePickerDialog(final TextInputEditText dateField) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            dateField.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", day, month + 1, year));
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
}