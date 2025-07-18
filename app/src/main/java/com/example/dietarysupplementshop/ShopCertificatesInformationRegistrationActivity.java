package com.example.dietarysupplementshop;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dietarysupplementshop.model.ShopRegistrationData;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.viewModel.SellerRegistrationViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShopCertificatesInformationRegistrationActivity extends AppCompatActivity {

    private SellerRegistrationViewModel viewModel;
    private ShopRegistrationData shopRegistrationData;

    private LinearLayout llBusinessLicenses, llProfessionalCertificates, llDiplomaCertificates;
    private LottieAnimationView animationViewLoading;
    private View frameLayoutLoading;

    private List<Uri> businessLicensesUris = new ArrayList<>();
    private List<Uri> professionalCertificatesUris = new ArrayList<>();
    private List<Uri> diplomaCertificatesUris = new ArrayList<>();

    private ActivityResultLauncher<String> pickImageLauncher;
    private CertificateType currentCertificateType;

    private enum CertificateType {
        BUSINESS_LICENSE, PROFESSIONAL_CERTIFICATE, DIPLOMA_CERTIFICATE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shop_certificates_information_registration);
        viewModel = new ViewModelProvider(this).get(SellerRegistrationViewModel.class);
        shopRegistrationData = (ShopRegistrationData) getIntent().getSerializableExtra("shopRegistrationData");
        if (shopRegistrationData == null) {
            Toast.makeText(this, "Lỗi: Mất dữ liệu đăng ký.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        bindViews();
        setupListeners();
        setupImagePicker();
    }

    private void bindViews() {
        llBusinessLicenses = findViewById(R.id.ll_business_licenses);
        llProfessionalCertificates = findViewById(R.id.ll_professional_certificates);
        llDiplomaCertificates = findViewById(R.id.ll_diploma_certificates);
        frameLayoutLoading = findViewById(R.id.frame_layout_loading);
        animationViewLoading = findViewById(R.id.animation_view_loading);
        animationViewLoading.setAnimation(R.raw.loading);
        animationViewLoading.loop(true);
    }

    private void setupListeners() {
        findViewById(R.id.btn_add_business_license).setOnClickListener(v -> openPickerFor(CertificateType.BUSINESS_LICENSE));
        findViewById(R.id.btn_add_professional_certificate).setOnClickListener(v -> openPickerFor(CertificateType.PROFESSIONAL_CERTIFICATE));
        findViewById(R.id.btn_add_diploma_certificate).setOnClickListener(v -> openPickerFor(CertificateType.DIPLOMA_CERTIFICATE));
        findViewById(R.id.btn_back_form).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.btn_complete_registration).setOnClickListener(v -> {
            if (validateData()) {
                collectUris();
                showConfirmationDialog(shopRegistrationData);
            }
        });
    }

    private void setupImagePicker() {
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                addUriToCorrectList(uri);
            }
        });
    }

    private void openPickerFor(CertificateType type) {
        currentCertificateType = type;
        pickImageLauncher.launch("*/*");
    }

    private void addUriToCorrectList(Uri uri) {
        switch (currentCertificateType) {
            case BUSINESS_LICENSE:
                businessLicensesUris.add(uri);
                addCertificateSlot(llBusinessLicenses, uri, CertificateType.BUSINESS_LICENSE);
                break;
            case PROFESSIONAL_CERTIFICATE:
                professionalCertificatesUris.add(uri);
                addCertificateSlot(llProfessionalCertificates, uri, CertificateType.PROFESSIONAL_CERTIFICATE);
                break;
            case DIPLOMA_CERTIFICATE:
                diplomaCertificatesUris.add(uri);
                addCertificateSlot(llDiplomaCertificates, uri, CertificateType.DIPLOMA_CERTIFICATE);
                break;
        }
    }

    private void collectUris() {
        shopRegistrationData.setProfessionalCertificatesUris(convertUriListToStringList(professionalCertificatesUris));
        shopRegistrationData.setDiplomaCertificatesUris(convertUriListToStringList(diplomaCertificatesUris));
    }

    private boolean validateData() {
        return true;
    }

    private void showLoading(boolean isLoading) {
        if (frameLayoutLoading != null) {
            frameLayoutLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading) {
                animationViewLoading.playAnimation();
            } else {
                animationViewLoading.pauseAnimation();
            }
        }
    }

    private ArrayList<String> convertUriListToStringList(List<Uri> uriList) {
        ArrayList<String> stringList = new ArrayList<>();
        for (Uri uri : uriList) {
            stringList.add(uri.toString());
        }
        return stringList;
    }

    private void addCertificateSlot(LinearLayout parentLayout, Uri fileUri, final CertificateType type) {
        int margin_16dp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());

        LinearLayout slotContainer = new LinearLayout(this);
        slotContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        containerParams.setMargins(0, 0, 0, margin_16dp);
        slotContainer.setLayoutParams(containerParams);
        slotContainer.setPadding(margin_16dp, margin_16dp, margin_16dp, margin_16dp);
        slotContainer.setBackgroundResource(R.drawable.bg_dashed_boder);


        ImageView fileIcon = new ImageView(this);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics()), // Kích thước icon
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics())
        );
        iconParams.setMargins(0, 0, 0, margin_16dp / 2);
        fileIcon.setLayoutParams(iconParams);
        fileIcon.setImageResource(R.drawable.ic_certificate);
        slotContainer.addView(fileIcon);

        TextView fileNameTextView = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        fileNameTextView.setLayoutParams(textParams);
        fileNameTextView.setText(getFileName(fileUri));
        fileNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        fileNameTextView.setTextColor(getResources().getColor(android.R.color.black));
        fileNameTextView.setSingleLine(true);
        fileNameTextView.setEllipsize(android.text.TextUtils.TruncateAt.END);
        slotContainer.addView(fileNameTextView);

        // Nút Xóa
        Button removeButton = new Button(this);
        removeButton.setText(R.string.remove_certificate);
        removeButton.setTextColor(getResources().getColor(R.color.red_error));
        removeButton.setAllCaps(false);
        removeButton.setBackgroundResource(android.R.color.transparent);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.setMargins(0, margin_16dp / 2, 0, 0);
        removeButton.setLayoutParams(buttonParams);

        removeButton.setOnClickListener(v -> {
            parentLayout.removeView(slotContainer);
            Toast.makeText(this, "Đã xóa chứng chỉ.", Toast.LENGTH_SHORT).show();
        });
        slotContainer.addView(removeButton);

        slotContainer.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String mimeType = getContentResolver().getType(fileUri);
                intent.setDataAndType(fileUri, mimeType);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Không thể mở file. Không có ứng dụng nào hỗ trợ.", Toast.LENGTH_SHORT).show();
            }
        });

        parentLayout.addView(slotContainer);
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    private boolean validateAndCollectData() {
        boolean isValid = true;

        if (businessLicensesUris.isEmpty()) {
            Toast.makeText(this, "Vui lòng tải lên ít nhất một Giấy phép kinh doanh.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (professionalCertificatesUris.isEmpty()) {
            Toast.makeText(this, "Vui lòng tải lên ít nhất một Chứng chỉ chuyên môn.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (diplomaCertificatesUris.isEmpty()) {
            Toast.makeText(this, "Vui lòng tải lên ít nhất một Chứng chỉ học vị.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }


    private void showConfirmationDialog(ShopRegistrationData data) {
        StringBuilder confirmationMessage = new StringBuilder();
        confirmationMessage.append("Vui lòng xác nhận lại các thông tin bạn đã cung cấp:\n\n");

        confirmationMessage.append("--- THÔNG TIN CỬA HÀNG ---\n");
        confirmationMessage.append("Tên shop: ").append(data.getShopName()).append("\n");
        confirmationMessage.append("Địa chỉ: ").append(data.getAddress()).append("\n");
        confirmationMessage.append("Email: ").append(data.getEmail()).append("\n");
        confirmationMessage.append("SĐT: ").append(data.getPhoneNumber()).append("\n\n");

        confirmationMessage.append("--- THÔNG TIN ĐỊNH DANH ---\n");
        confirmationMessage.append("Loại: Cá nhân\n");
        confirmationMessage.append("Họ tên: ").append(data.getFullName()).append("\n");
        confirmationMessage.append("Số CCCD: ").append(data.getIdCardNumber()).append("\n");


        confirmationMessage.append("\n--- CÁC FILE ĐÍNH KÈM ---\n");
        confirmationMessage.append("Mặt trước CCCD: ").append(data.getIdFrontImageUri() != null ? "Đã có" : "Chưa có").append("\n");
        confirmationMessage.append("Mặt sau CCCD: ").append(data.getIdBackImageUri() != null ? "Đã có" : "Chưa có").append("\n");
        if (data.getBusinessLicenseUris() != null) {
            confirmationMessage.append("Giấy phép kinh doanh: Đã có\n");
        }
        if (data.getTaxNumber() != null) {
            confirmationMessage.append("Mã số thuế: Đã có\n").append(data.getTaxNumber()).append("\n");
        }
        confirmationMessage.append("Số lượng chứng chỉ khác: ").append(data.getProfessionalCertificatesUris().size() + data.getDiplomaCertificatesUris().size());

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận thông tin")
                .setMessage(confirmationMessage.toString())
                .setPositiveButton("Xác nhận & Gửi", (dialog, which) -> {
                    submitRegistrationData(data);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }


    private void submitRegistrationData(ShopRegistrationData data) {
        viewModel.submitRegistration(data).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.getStatus()) {
                case LOADING:
                    showLoading(true);
                    break;
                case SUCCESS:
                    showLoading(false);
                    Toast.makeText(this, "Hồ sơ đã được gửi thành công!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, SellerRegistrationStatusActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    break;
                case ERROR:
                    showLoading(false);
                    Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }
}