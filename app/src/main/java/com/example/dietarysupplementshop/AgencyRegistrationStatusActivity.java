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
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.example.dietarysupplementshop.viewModel.AccountViewModel;

public class AgencyRegistrationStatusActivity extends AppCompatActivity {
    private static final String TAG = "AgencyRegStatusActivity";

    private AccountViewModel accountViewModel;
    private CardView cardStatusResultDisplay;
    private ImageView imgStatusIcon;
    private TextView tvStatusMainMessage, tvStatusSubMessage, tvErrorMessage, tvInitialLoadingMessage;
    private Button btnViewRejectionReason, btnReapply, btnRefreshStatus, btnContactSupport;
    // Khai báo nút mới
    private Button btnGoToHomepage, btnGoToAgencyChannel;
    private ProgressBar pbLoadingStatus;

    private String rejectionReasonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_registration_status);
        setupViews();
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        setupClickListeners();
        observeViewModel();
        accountViewModel.loadAccountInfo();
        Log.d(TAG, "AgencyRegistrationStatusActivity created, loading account info.");
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
        // Ánh xạ nút mới
        btnGoToHomepage = findViewById(R.id.btn_go_to_homepage);
        btnGoToAgencyChannel = findViewById(R.id.btn_go_to_agency_channel);

        tvErrorMessage = findViewById(R.id.tv_error_message);
        tvInitialLoadingMessage = findViewById(R.id.tv_initial_loading_message);
        pbLoadingStatus = findViewById(R.id.pb_loading_status);
    }

    private void setupClickListeners() {
        btnRefreshStatus.setOnClickListener(v -> {
            Log.d(TAG, "Refresh Status button clicked.");
            accountViewModel.loadAccountInfo();
        });
        btnReapply.setOnClickListener(v -> {
            Log.d(TAG, "Reapply button clicked.");
            reapplyForAgency();
        });

        btnContactSupport.setOnClickListener(v -> {
            Log.d(TAG, "Contact Support button clicked (default fallback).");
            contactSupport();
        });

        btnViewRejectionReason.setOnClickListener(v -> {
            Log.d(TAG, "View Rejection Reason button clicked. Reason: " + rejectionReasonText);
            showRejectionReason();
        });

        btnGoToAgencyChannel.setOnClickListener(v -> {
            Log.d(TAG, "Go To Agency Channel button clicked. Acknowledging approval.");
            SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
            prefs.edit().putBoolean("AGENCY_APPROVAL_ACKNOWLEDGED", true).apply();

            Intent intent = new Intent(this, AgencyMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        btnGoToHomepage.setOnClickListener(v -> {
            Log.d(TAG, "Go to Homepage button clicked.");
            Intent intent = new Intent(this, HomepageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void observeViewModel() {
        accountViewModel.getAccountInfo().observe(this, resource -> {
            if (resource == null) return;

            Log.d(TAG, "observeViewModel: Resource status = " + resource.getStatus());

            switch (resource.getStatus()) {
                case LOADING:
                    Log.d(TAG, "observeViewModel: Loading state.");
                    showLoadingState();
                    break;
                case SUCCESS:
                    if (resource.getData() != null) {
                        Log.d(TAG, "observeViewModel: SUCCESS - Data received from ViewModel:");
                        Log.d(TAG, " - Status (Agency Reg): " + resource.getData().getStatus());
                        Log.d(TAG, " - Rejection Reason: " + resource.getData().getRejectionReason());

                        updateUIForStatus(resource.getData().getStatus());
                        this.rejectionReasonText = resource.getData().getRejectionReason();
                    } else {
                        Log.w(TAG, "observeViewModel: SUCCESS - resource.getData() is null.");
                    }
                    showSuccessState();
                    break;
                case ERROR:
                    Log.e(TAG, "observeViewModel: ERROR - Message = " + resource.getMessage());
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

    private void showSuccessState() {
        pbLoadingStatus.setVisibility(View.GONE);
        tvInitialLoadingMessage.setVisibility(View.GONE);
        cardStatusResultDisplay.setVisibility(View.VISIBLE);
        tvErrorMessage.setVisibility(View.GONE);
        btnRefreshStatus.setEnabled(true);
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
        Log.d(TAG, "updateUIForStatus called with status: " + status);
        String trimmedStatus = status != null ? status.trim() : "NOT_REGISTERED";
        btnGoToAgencyChannel.setVisibility(View.GONE);
        btnViewRejectionReason.setVisibility(View.GONE);
        btnReapply.setVisibility(View.GONE);
        btnRefreshStatus.setVisibility(View.GONE);
        btnContactSupport.setVisibility(View.GONE);
        btnGoToHomepage.setVisibility(View.GONE);


        switch (trimmedStatus) {
            case STATUS_PENDING:
                imgStatusIcon.setImageResource(R.drawable.ic_status_pending);
                tvStatusMainMessage.setText(R.string.status_pending_title);
                tvStatusSubMessage.setText(R.string.status_pending_message);

                btnRefreshStatus.setVisibility(View.VISIBLE);
                btnGoToHomepage.setVisibility(View.VISIBLE);
                btnContactSupport.setVisibility(View.VISIBLE);
                Log.i(TAG, "updateUIForStatus: Displaying PENDING status.");
                break;

            case STATUS_APPROVED:
                imgStatusIcon.setImageResource(R.drawable.ic_status_approved);
                tvStatusMainMessage.setText("Chúc mừng! Hồ sơ của bạn đã được duyệt.");
                tvStatusSubMessage.setText("Nhấn nút bên dưới để bắt đầu quản lý kênh bán hàng của bạn.");
                btnGoToAgencyChannel.setVisibility(View.VISIBLE);
                btnGoToHomepage.setVisibility(View.VISIBLE);
                Log.i(TAG, "updateUIForStatus: Displaying APPROVED status.");
                break;

            case STATUS_REJECTED:
                imgStatusIcon.setImageResource(R.drawable.ic_status_rejected);
                tvStatusMainMessage.setText(R.string.status_rejected_title);
                tvStatusSubMessage.setText(R.string.status_rejected_message);
                btnViewRejectionReason.setVisibility(View.VISIBLE);
                btnReapply.setVisibility(View.VISIBLE);
                btnGoToHomepage.setVisibility(View.VISIBLE);
                btnContactSupport.setVisibility(View.VISIBLE);
                Log.i(TAG, "updateUIForStatus: Displaying REJECTED status. Rejection reason: " + rejectionReasonText);
                break;

            default:
                imgStatusIcon.setImageResource(R.drawable.ic_info);
                tvStatusMainMessage.setText(R.string.status_unknown_title);
                tvStatusSubMessage.setText(R.string.status_unknown_message);
                btnReapply.setVisibility(View.VISIBLE);
                btnGoToHomepage.setVisibility(View.VISIBLE);
                btnContactSupport.setVisibility(View.VISIBLE);
                Log.i(TAG, "updateUIForStatus: Displaying UNKNOWN/NOT_REGISTERED status.");
                break;
        }
    }

    private void showRejectionReason() {
        String reasonToShow = (rejectionReasonText != null && !rejectionReasonText.isEmpty()) ?
                "Lý do từ chối: " + rejectionReasonText :
                "Không có lý do từ chối cụ thể được cung cấp.";
        Toast.makeText(this, reasonToShow, Toast.LENGTH_LONG).show();
    }

    private void reapplyForAgency() {
        Intent intent = new Intent(this, AgencyWelcomeRegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    private void contactSupport() {
        Toast.makeText(this, "Liên hệ với bộ phận hỗ trợ", Toast.LENGTH_SHORT).show();
    }
}