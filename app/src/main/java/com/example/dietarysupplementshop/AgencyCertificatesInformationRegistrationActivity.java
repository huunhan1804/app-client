package com.example.dietarysupplementshop;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dietarysupplementshop.model.AgencyRegistrationData;
import com.example.dietarysupplementshop.viewModel.AgencyRegistrationViewModel;
import com.example.dietarysupplementshop.responses.AgencyInfoDTO;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AgencyCertificatesInformationRegistrationActivity extends AppCompatActivity {

    private AgencyRegistrationViewModel viewModel;
    private AgencyRegistrationData agencyRegistrationData;

    private LinearLayout llBusinessLicenses, llProfessionalCertificates, llDiplomaCertificates;
    private LottieAnimationView animationViewLoading;
    private View frameLayoutLoading;
    private Uri idCardFrontUri;
    private Uri idCardBackUri;
    private ActivityResultLauncher<String> pickImageLauncher;
    private CertificateType currentCertificateType;

    private enum CertificateType {
        BUSINESS_LICENSE, PROFESSIONAL_CERTIFICATE, DIPLOMA_CERTIFICATE, ID_CARD_FRONT, ID_CARD_BACK
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shop_certificates_information_registration);
        viewModel = new ViewModelProvider(this).get(AgencyRegistrationViewModel.class);
        agencyRegistrationData = (AgencyRegistrationData) getIntent().getSerializableExtra("agencyRegistrationData");

        idCardFrontUri = getIntent().getParcelableExtra("idFrontImageUri");
        idCardBackUri = getIntent().getParcelableExtra("idBackImageUri");

        if (agencyRegistrationData == null) {
            Toast.makeText(this, "Lỗi: Mất dữ liệu đăng ký cửa hàng.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (idCardFrontUri == null || idCardBackUri == null) {
            Toast.makeText(this, "Lỗi: Mất dữ liệu ảnh CCCD.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (savedInstanceState != null) {
            idCardFrontUri = savedInstanceState.getParcelable("idCardFrontUri");
            idCardBackUri = savedInstanceState.getParcelable("idCardBackUri");

            agencyRegistrationData.setBusinessLicenseUrl(savedInstanceState.getString("businessLicenseUrl"));
            agencyRegistrationData.setProfessionalCertificateUrl(savedInstanceState.getString("professionalCertificateUrl"));
            agencyRegistrationData.setDiplomaCertificateUrl(savedInstanceState.getString("diplomaCertificateUrl"));
            agencyRegistrationData = (AgencyRegistrationData) savedInstanceState.getSerializable("agencyRegistrationData");

            bindViews();

            if (agencyRegistrationData.getBusinessLicenseUrl() != null) {
                llBusinessLicenses.removeAllViews();
                addCertificateSlot(llBusinessLicenses, Uri.parse(agencyRegistrationData.getBusinessLicenseUrl()), CertificateType.BUSINESS_LICENSE);
            }
            if (agencyRegistrationData.getProfessionalCertificateUrl() != null) {
                llProfessionalCertificates.removeAllViews();
                addCertificateSlot(llProfessionalCertificates, Uri.parse(agencyRegistrationData.getProfessionalCertificateUrl()), CertificateType.PROFESSIONAL_CERTIFICATE);
            }
            if (agencyRegistrationData.getDiplomaCertificateUrl() != null) {
                llDiplomaCertificates.removeAllViews();
                addCertificateSlot(llDiplomaCertificates, Uri.parse(agencyRegistrationData.getDiplomaCertificateUrl()), CertificateType.DIPLOMA_CERTIFICATE);
            }
        } else {
            agencyRegistrationData = (AgencyRegistrationData) getIntent().getSerializableExtra("agencyRegistrationData");
            idCardFrontUri = getIntent().getParcelableExtra("idFrontImageUri");
            idCardBackUri = getIntent().getParcelableExtra("idBackImageUri");

            if (agencyRegistrationData == null) {
                Toast.makeText(this, "Lỗi: Mất dữ liệu đăng ký cửa hàng.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            if (idCardFrontUri == null || idCardBackUri == null) {
                Toast.makeText(this, "Lỗi: Mất dữ liệu ảnh CCCD.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            agencyRegistrationData.setBusinessLicenseUrl(null);
            agencyRegistrationData.setProfessionalCertificateUrl(null);
            agencyRegistrationData.setDiplomaCertificateUrl(null);
            agencyRegistrationData.setIdCardFrontUrl(null);
            agencyRegistrationData.setIdCardBackUrl(null);
        }


        bindViews();
        setupListeners();
        setupImagePicker();
        updateButtonState();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("idCardFrontUri", idCardFrontUri);
        outState.putParcelable("idCardBackUri", idCardBackUri);
        outState.putString("businessLicenseUrl", agencyRegistrationData.getBusinessLicenseUrl());
        outState.putString("professionalCertificateUrl", agencyRegistrationData.getProfessionalCertificateUrl());
        outState.putString("diplomaCertificateUrl", agencyRegistrationData.getDiplomaCertificateUrl());
        outState.putSerializable("agencyRegistrationData", agencyRegistrationData);
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

        // THAY ĐỔI LỚN Ở ĐÂY:
        findViewById(R.id.btn_complete_registration).setOnClickListener(v -> {
            if (validateAndCollectData()) { // Hàm này chỉ kiểm tra hợp lệ các URL đã có và URI cục bộ
                showLoading(true); // Bắt đầu hiển thị loading khi chuẩn bị upload ảnh CCCD
                // Tải ảnh CCCD lên Firebase trước
                uploadIdCardToFirebase(idCardFrontUri, idCardBackUri)
                        .addOnSuccessListener(idCardUrls -> {
                            // GÁN CÁC URL TỪ FIREBASE VÀO ĐỐI TƯỢNG DATA SAU KHI UPLOAD THÀNH CÔNG
                            agencyRegistrationData.setIdCardFrontUrl(idCardUrls[0]);
                            agencyRegistrationData.setIdCardBackUrl(idCardUrls[1]);

                            // Thêm log để kiểm tra lại dữ liệu sau khi có đủ URL ảnh CCCD
                            Log.d("PRE_DIALOG_FINAL_CHECK", "AgencyRegistrationData before final dialog: " +
                                    "Shop Name: " + agencyRegistrationData.getShopName() +
                                    ", Address: " + agencyRegistrationData.getAddress() +
                                    ", Email: " + agencyRegistrationData.getEmail() +
                                    ", Phone: " + agencyRegistrationData.getPhoneNumber() +
                                    ", Tax Number: " + agencyRegistrationData.getTaxNumber() +
                                    ", Full Name: " + agencyRegistrationData.getFullName() +
                                    ", DOB: " + agencyRegistrationData.getDateOfBirth() +
                                    ", Gender: " + agencyRegistrationData.getGender() +
                                    ", ID Number: " + agencyRegistrationData.getIdCardNumber() +
                                    ", Date of Issue: " + agencyRegistrationData.getDateOfIssue() +
                                    ", Place of Issue: " + agencyRegistrationData.getPlaceOfIssue() +
                                    ", ID Front URL: " + agencyRegistrationData.getIdCardFrontUrl() +
                                    ", ID Back URL: " + agencyRegistrationData.getIdCardBackUrl() +
                                    ", Business License URL: " + agencyRegistrationData.getBusinessLicenseUrl() +
                                    ", Professional Cert URL: " + agencyRegistrationData.getProfessionalCertificateUrl() +
                                    ", Diploma Cert URL: " + agencyRegistrationData.getDiplomaCertificateUrl());

                            showLoading(false); // Ẩn loading sau khi upload ảnh CCCD xong

                            // BÂY GIỜ MỚI HIỂN THỊ DIALOG XÁC NHẬN VỚI DỮ LIỆU ĐẦY ĐỦ
                            showConfirmationDialog(agencyRegistrationData);
                        })
                        .addOnFailureListener(e -> {
                            showLoading(false);
                            Toast.makeText(this, "Lỗi khi tải ảnh CCCD: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            }
        });
    }

    private void setupImagePicker() {
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        uploadFileToFirebase(uri, currentCertificateType);
                    }
                });
    }

    private void openPickerFor(CertificateType type) {
        currentCertificateType = type;
        pickImageLauncher.launch("*/*");
    }

    private void uploadFileToFirebase(Uri fileUri, CertificateType type) {
        if (fileUri == null) {
            Toast.makeText(this, "Không có file để tải lên.", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        String folderName = "";
        String successMessage = "";

        switch (type) {
            case BUSINESS_LICENSE:
                folderName = "business_licenses";
                successMessage = "Giấy phép kinh doanh";
                break;
            case PROFESSIONAL_CERTIFICATE:
                folderName = "professional_certs";
                successMessage = "Chứng chỉ chuyên môn";
                break;
            case DIPLOMA_CERTIFICATE:
                folderName = "diploma_certs";
                successMessage = "Chứng chỉ học vị";
                break;

            case ID_CARD_FRONT:
            case ID_CARD_BACK:
                folderName = "id_cards";
                successMessage = "Ảnh CCCD";
                break;
            default:
                folderName = "others";
                successMessage = "File";
                break;
        }

        String fileNameInStorage = UUID.randomUUID().toString() + "_" + getFileName(fileUri);
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("agency_documents/" + folderName + "/" + fileNameInStorage);

        String finalSuccessMessage = successMessage;
        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String downloadUrl = uri.toString();
                                switch (type) {
                                    case BUSINESS_LICENSE:
                                        agencyRegistrationData.setBusinessLicenseUrl(downloadUrl);
                                        llBusinessLicenses.removeAllViews();
                                        addCertificateSlot(llBusinessLicenses, fileUri, type);
                                        break;
                                    case PROFESSIONAL_CERTIFICATE:
                                        agencyRegistrationData.setProfessionalCertificateUrl(downloadUrl);
                                        llProfessionalCertificates.removeAllViews();
                                        addCertificateSlot(llProfessionalCertificates, fileUri, type);
                                        break;
                                    case DIPLOMA_CERTIFICATE:
                                        agencyRegistrationData.setDiplomaCertificateUrl(downloadUrl);
                                        llDiplomaCertificates.removeAllViews();
                                        addCertificateSlot(llDiplomaCertificates, fileUri, type);
                                        break;
                                }
                                Toast.makeText(this, "Tải " + finalSuccessMessage + " thành công!", Toast.LENGTH_SHORT).show();
                                updateButtonState();
                                showLoading(false);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Lỗi lấy URL " + finalSuccessMessage + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
                                showLoading(false);
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi tải " + finalSuccessMessage + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
                    showLoading(false);
                });
    }


    private boolean validateAndCollectData() {
        boolean isValid = true;

        if (agencyRegistrationData.getBusinessLicenseUrl() == null || agencyRegistrationData.getBusinessLicenseUrl().isEmpty()) {
            Toast.makeText(this, "Vui lòng tải lên Giấy phép kinh doanh.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (agencyRegistrationData.getProfessionalCertificateUrl() == null || agencyRegistrationData.getProfessionalCertificateUrl().isEmpty()) {
            Toast.makeText(this, "Vui lòng tải lên Chứng chỉ chuyên môn.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (agencyRegistrationData.getDiplomaCertificateUrl() == null || agencyRegistrationData.getDiplomaCertificateUrl().isEmpty()) {
            Toast.makeText(this, "Vui lòng tải lên Chứng chỉ học vị.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (idCardFrontUri == null) {
            Toast.makeText(this, "Vui lòng tải ảnh mặt trước CCCD.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (idCardBackUri == null) {
            Toast.makeText(this, "Vui lòng tải ảnh mặt sau CCCD.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
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
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics()),
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
            parentLayout.removeAllViews();
            removeUrlFromField(type);
            Toast.makeText(this, "Đã xóa chứng chỉ.", Toast.LENGTH_SHORT).show();
            updateButtonState();
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


    private void removeUrlFromField(CertificateType type) {
        switch (type) {
            case BUSINESS_LICENSE:
                agencyRegistrationData.setBusinessLicenseUrl(null);
                break;
            case PROFESSIONAL_CERTIFICATE:
                agencyRegistrationData.setProfessionalCertificateUrl(null);
                break;
            case DIPLOMA_CERTIFICATE:
                agencyRegistrationData.setDiplomaCertificateUrl(null);
                break;
        }
        updateButtonState();
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

    private void showConfirmationDialog(AgencyRegistrationData data) {
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
        confirmationMessage.append("Ngày sinh: ").append(data.getDateOfBirth()).append("\n"); // THÊM
        confirmationMessage.append("Giới tính: ").append(data.getGender()).append("\n"); // THÊM
        confirmationMessage.append("Ngày cấp: ").append(data.getDateOfIssue()).append("\n"); // THÊM
        confirmationMessage.append("Nơi cấp: ").append(data.getPlaceOfIssue()).append("\n"); // THÊM
        confirmationMessage.append("Mã số thuế: ").append(data.getTaxNumber()).append("\n\n"); // THÊM


        confirmationMessage.append("\n--- CÁC FILE ĐÍNH KÈM ---\n");
        confirmationMessage.append("Mặt trước CCCD: ").append(data.getIdCardFrontUrl() != null ? data.getIdCardFrontUrl() : "Chưa có").append("\n");
        confirmationMessage.append("Mặt sau CCCD: ").append(data.getIdCardBackUrl() != null ? data.getIdCardBackUrl() : "Chưa có").append("\n");
        confirmationMessage.append("Giấy phép kinh doanh: ").append(data.getBusinessLicenseUrl() != null ? data.getBusinessLicenseUrl() : "Chưa có").append("\n");
        confirmationMessage.append("Chứng chỉ chuyên môn: ").append(data.getProfessionalCertificateUrl() != null ? data.getProfessionalCertificateUrl() : "Chưa có").append("\n");
        confirmationMessage.append("Chứng chỉ học vị: ").append(data.getDiplomaCertificateUrl() != null ? data.getDiplomaCertificateUrl() : "Chưa có");
        Log.d("CONFIRM_DATA_DEBUG", "AgencyRegistrationData before dialog: " +
                "Shop Name: " + data.getShopName() +
                ", Address: " + data.getAddress() +
                ", Email: " + data.getEmail() +
                ", Phone: " + data.getPhoneNumber() +
                ", Tax Number: " + data.getTaxNumber() +
                ", Full Name: " + data.getFullName() +
                ", DOB: " + data.getDateOfBirth() +
                ", Gender: " + data.getGender() +
                ", ID Number: " + data.getIdCardNumber() +
                ", Date of Issue: " + data.getDateOfIssue() +
                ", Place of Issue: " + data.getPlaceOfIssue() +
                ", ID Front URL: " + data.getIdCardFrontUrl() +
                ", ID Back URL: " + data.getIdCardBackUrl() +
                ", Business License URL: " + data.getBusinessLicenseUrl() +
                ", Professional Cert URL: " + data.getProfessionalCertificateUrl() +
                ", Diploma Cert URL: " + data.getDiplomaCertificateUrl());


        new AlertDialog.Builder(this)
                .setTitle("Xác nhận thông tin đăng ký") // Thêm tiêu đề cho dễ nhìn
                .setMessage(confirmationMessage.toString())
                .setPositiveButton("Xác nhận & Gửi", (dialog, which) -> {
                    submitRegistrationData(data);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void submitRegistrationData(AgencyRegistrationData data) {
        showLoading(true);

        viewModel.submitRegistration(data)
                .observe(this, resource -> {
                    showLoading(false);
                    if (resource == null) return;

                    switch (resource.getStatus()) {
                        case LOADING:
                            // showLoading(true) đã được gọi ở đầu phương thức này
                            break;
                        case SUCCESS:
                            AgencyInfoDTO agencyInfoResponse = resource.getData();
                            if (agencyInfoResponse != null) {
                                String statusFromBackend = agencyInfoResponse.getStatus();
                                if ("PENDING".equalsIgnoreCase(statusFromBackend)) {
                                    Toast.makeText(this, "Hồ sơ đã được gửi thành công và đang chờ xét duyệt!", Toast.LENGTH_LONG).show();
                                } else if ("APPROVED".equalsIgnoreCase(statusFromBackend)) {
                                    Toast.makeText(this, "Hồ sơ đã được duyệt rồi. Vui lòng chuyển sang kênh người bán.", Toast.LENGTH_LONG).show();
                                } else if ("REJECTED".equalsIgnoreCase(statusFromBackend)) {
                                    Toast.makeText(this, "Hồ sơ đã bị từ chối trước đó. Vui lòng xem lý do và đăng ký lại.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(this, "Đăng ký thành công nhưng trạng thái không rõ. Vui lòng kiểm tra lại.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(this, "Đăng ký thành công nhưng không nhận được dữ liệu trạng thái.", Toast.LENGTH_LONG).show();
                            }
                            Intent intent = new Intent(this, AgencyRegistrationStatusActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            break;
                        case ERROR:
                            // showLoading(false) đã được gọi ở đầu phương thức này
                            String errorMessage = resource.getMessage();
                            Log.e(TAG, "Registration ERROR: " + errorMessage);

                            String toastMessage;
                            if (errorMessage != null && errorMessage.contains("đã có một đơn đăng ký người bán đang chờ duyệt")) {
                                toastMessage = "Bạn đã có một đơn đăng ký người bán đang chờ duyệt. Vui lòng đợi phê duyệt.";
                            } else {
                                toastMessage = "Lỗi: " + (errorMessage != null ? errorMessage : "Không xác định");
                            }
                            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();

                            Intent statusIntent = new Intent(this, AgencyRegistrationStatusActivity.class);
                            statusIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(statusIntent);
                            finish();
                            break;
                    }
                });
    }

    private com.google.android.gms.tasks.Task<String[]> uploadIdCardToFirebase(Uri frontUri, Uri backUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("agency_documents/id_cards/");
        String frontFileName = "front_" + UUID.randomUUID().toString() + "_" + getFileName(frontUri);
        String backFileName = "back_" + UUID.randomUUID().toString() + "_" + getFileName(backUri);

        com.google.android.gms.tasks.Task<Uri> uploadFrontTask = storageRef.child(frontFileName).putFile(frontUri).continueWithTask(task -> {
            if (!task.isSuccessful()) throw Objects.requireNonNull(task.getException());
            return storageRef.child(frontFileName).getDownloadUrl();
        });

        com.google.android.gms.tasks.Task<Uri> uploadBackTask = storageRef.child(backFileName).putFile(backUri).continueWithTask(task -> {
            if (!task.isSuccessful()) throw Objects.requireNonNull(task.getException());
            return storageRef.child(backFileName).getDownloadUrl();
        });

        return com.google.android.gms.tasks.Tasks.whenAllSuccess(uploadFrontTask, uploadBackTask)
                .continueWith(task -> {
                    List<Object> results = (List<Object>) task.getResult();

                    String frontUrl = ((Uri) results.get(0)).toString();
                    String backUrl = ((Uri) results.get(1)).toString();

                    return new String[]{frontUrl, backUrl};
                });
    }
    private void updateButtonState() {
        Button btnCompleteRegistration = findViewById(R.id.btn_complete_registration);
        if (btnCompleteRegistration != null) {
            boolean isAllRequiredFieldsFilled =
                    (agencyRegistrationData.getBusinessLicenseUrl() != null && !agencyRegistrationData.getBusinessLicenseUrl().isEmpty()) && // Lấy từ agencyRegistrationData
                            (agencyRegistrationData.getProfessionalCertificateUrl() != null && !agencyRegistrationData.getProfessionalCertificateUrl().isEmpty()) && // Lấy từ agencyRegistrationData
                            (agencyRegistrationData.getDiplomaCertificateUrl() != null && !agencyRegistrationData.getDiplomaCertificateUrl().isEmpty()) && // Lấy từ agencyRegistrationData
                            (idCardFrontUri != null) &&
                            (idCardBackUri != null);

            btnCompleteRegistration.setEnabled(isAllRequiredFieldsFilled);
        }
    }
}