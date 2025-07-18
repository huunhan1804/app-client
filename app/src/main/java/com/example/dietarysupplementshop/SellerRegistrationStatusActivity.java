package com.example.dietarysupplementshop;

import static com.example.dietarysupplementshop.constant.status.STATUS_APPROVED;
import static com.example.dietarysupplementshop.constant.status.STATUS_PENDING;
import static com.example.dietarysupplementshop.constant.status.STATUS_REJECTED;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.example.dietarysupplementshop.responses.AccountInformation;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;

public class SellerRegistrationStatusActivity extends AppCompatActivity {

    private AccountViewModel accountViewModel;
    private CardView cardStatusResultDisplay;
    private ImageView imgStatusIcon;
    private TextView tvStatusMainMessage, tvStatusSubMessage, tvErrorMessage, tvInitialLoadingMessage;
    private Button btnViewRejectionReason, btnReapply, btnRefreshStatus, btnContactSupport, btnGoToSellerChannel;
    private ProgressBar pbLoadingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration_status);
        setupViews();
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        setupClickListeners();
        observeViewModel();
        accountViewModel.loadAccountInfo();
    }

    private void setupViews() {
        cardStatusResultDisplay = findViewById(R.id.card_status_result_display);
        imgStatusIcon = findViewById(R.id.img_status_icon);
        tvStatusMainMessage = findViewById(R.id.tv_status_main_message);
        tvStatusSubMessage = findViewById(R.id.tv_status_sub_message);
        btnViewRejectionReason = findViewById(R.id.btn_view_rejection_reason);
        btnReapply = findViewById(R.id.btn_reapply);
        btnRefreshStatus = findViewById(R.id.btn_refresh_status);
        btnContactSupport = findViewById(R.id.btn_contact_support);
        tvErrorMessage = findViewById(R.id.tv_error_message);
        tvInitialLoadingMessage = findViewById(R.id.tv_initial_loading_message);
        pbLoadingStatus = findViewById(R.id.pb_loading_status);
        btnGoToSellerChannel = findViewById(R.id.btn_go_to_seller_channel);
    }

    private void setupClickListeners() {
        btnRefreshStatus.setOnClickListener(v -> accountViewModel.loadAccountInfo());
        btnReapply.setOnClickListener(v -> reapplyForSeller());
        btnContactSupport.setOnClickListener(v -> contactSupport());
        btnViewRejectionReason.setOnClickListener(v -> showRejectionReason());

        btnGoToSellerChannel.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
            prefs.edit().putBoolean("SELLER_APPROVAL_ACKNOWLEDGED", true).apply();

            Intent intent = new Intent(this, SellerMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void observeViewModel() {
        accountViewModel.getAccountInfo().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    showLoadingState();
                    break;
                case SUCCESS:
                    if (resource.getData() != null) {
                        showSuccessState(resource.getData());
                    }
                    break;
                case ERROR:
                    showErrorState(resource.getMessage());
                    break;
            }
        });
    }

    private void showLoadingState() {
        pbLoadingStatus.setVisibility(View.VISIBLE);
        tvInitialLoadingMessage.setVisibility(View.VISIBLE);
        cardStatusResultDisplay.setVisibility(View.GONE);
        tvErrorMessage.setVisibility(View.GONE);
        btnRefreshStatus.setEnabled(false);
    }

    private void showSuccessState(AccountInformation data) {
        pbLoadingStatus.setVisibility(View.GONE);
        tvInitialLoadingMessage.setVisibility(View.GONE);
        cardStatusResultDisplay.setVisibility(View.VISIBLE);
        tvErrorMessage.setVisibility(View.GONE);
        btnRefreshStatus.setEnabled(true);
        updateUIForStatus(data.getStatus());
    }

    private void showErrorState(String message) {
        pbLoadingStatus.setVisibility(View.GONE);
        tvInitialLoadingMessage.setVisibility(View.GONE);
        cardStatusResultDisplay.setVisibility(View.GONE);
        tvErrorMessage.setText("Không thể tải trạng thái: " + message);
        tvErrorMessage.setVisibility(View.VISIBLE);
        btnRefreshStatus.setEnabled(true);
    }

    private void updateUIForStatus(String status) {
        btnGoToSellerChannel.setVisibility(View.GONE);
        btnViewRejectionReason.setVisibility(View.GONE);
        btnReapply.setVisibility(View.GONE);
        btnContactSupport.setVisibility(View.VISIBLE);

        switch (status) {
            case STATUS_PENDING:
                imgStatusIcon.setImageResource(R.drawable.ic_status_pending);
                tvStatusMainMessage.setText(R.string.status_pending_title);
                tvStatusSubMessage.setText(R.string.status_pending_message);
                break;

            case STATUS_APPROVED:
                imgStatusIcon.setImageResource(R.drawable.ic_status_approved);
                tvStatusMainMessage.setText("Chúc mừng! Hồ sơ của bạn đã được duyệt.");
                tvStatusSubMessage.setText("Nhấn nút bên dưới để bắt đầu quản lý kênh bán hàng của bạn.");
                btnGoToSellerChannel.setVisibility(View.VISIBLE);
                btnContactSupport.setVisibility(View.GONE);
                break;

            case STATUS_REJECTED:
                imgStatusIcon.setImageResource(R.drawable.ic_status_rejected);
                tvStatusMainMessage.setText(R.string.status_rejected_title);
                tvStatusSubMessage.setText(R.string.status_rejected_message);
                btnViewRejectionReason.setVisibility(View.VISIBLE);
                btnReapply.setVisibility(View.VISIBLE);
                break;

            default:
                imgStatusIcon.setImageResource(R.drawable.ic_info);
                tvStatusMainMessage.setText(R.string.status_unknown_title);
                tvStatusSubMessage.setText(R.string.status_unknown_message);
                btnReapply.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showRejectionReason() {
        String reason = "Lý do từ chối: Tài liệu CCCD không rõ ràng hoặc không hợp lệ.";
        Toast.makeText(this, reason, Toast.LENGTH_LONG).show();
    }

    private void reapplyForSeller() {
        Intent intent = new Intent(this, SellerRegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    private void contactSupport() {
        Toast.makeText(this, "Liên hệ với bộ phận hỗ trợ", Toast.LENGTH_SHORT).show();
    }
}