package com.example.dietarysupplementshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.adapter.VoucherAdapter;
import com.example.dietarysupplementshop.model.AccountCoupon;
import com.example.dietarysupplementshop.model.Coupon;
import com.example.dietarysupplementshop.repositories.CouponRepository;
import com.example.dietarysupplementshop.repositories.MockCouponRepository;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.requests.ApplyCouponRequest;
import com.example.dietarysupplementshop.responses.AppliedCouponResponse;
import com.example.dietarysupplementshop.viewModel.CouponViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class VouchersActivity extends AppCompatActivity implements VoucherAdapter.OnVoucherActionListener {

    private ImageButton btnBack;
    private TabLayout tabLayoutVouchers;
    private RecyclerView recyclerViewVouchers;
    private VoucherAdapter voucherAdapter;
    private CouponViewModel couponViewModel;
    private ProgressBar progressBar;
    private TextView tvEmptyVoucher;
    private View emptyStateLayout;
    private android.widget.Button btnExploreVouchers;

    private Long cartIdForApplyingVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers_buyer);

        if (getIntent().hasExtra("CART_ID")) {
            cartIdForApplyingVoucher = getIntent().getLongExtra("CART_ID", -1L);
        }

        initViews();
        setupToolbar();
        setupTabLayout();
        setupRecyclerView();

        CouponRepository repository = new MockCouponRepository();
        // CouponRepository repository = RealCouponRepository.getInstance();

        couponViewModel = new ViewModelProvider(this, new CouponViewModel.Factory(repository)).get(CouponViewModel.class);

        observeViewModel();

        couponViewModel.getUserCoupons("USABLE");
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tabLayoutVouchers = findViewById(R.id.tab_layout_vouchers);
        recyclerViewVouchers = findViewById(R.id.recycler_view_vouchers);
        progressBar = findViewById(R.id.progress_bar_vouchers);
        tvEmptyVoucher = findViewById(R.id.tv_empty_voucher);
        emptyStateLayout = findViewById(R.id.empty_state_layout);
        btnExploreVouchers = findViewById(R.id.btn_explore_vouchers);

        if (btnExploreVouchers != null) {
            btnExploreVouchers.setOnClickListener(v -> {
                Toast.makeText(this, "Chuyển đến màn hình khám phá voucher...", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(this, MainActivity.class);
                // intent.putExtra("navigateToTab", "vouchers");
                // startActivity(intent);
            });
        }
    }


    private void setupToolbar() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
        // TextView tvHeaderTitle = findViewById(R.id.tv_header_title);
        // if (tvHeaderTitle != null) {
        //     tvHeaderTitle.setText("Voucher của tôi");
        // }
    }

    private void setupTabLayout() {
        tabLayoutVouchers.addTab(tabLayoutVouchers.newTab().setText("Có thể dùng"));
        tabLayoutVouchers.addTab(tabLayoutVouchers.newTab().setText("Đã dùng"));
        tabLayoutVouchers.addTab(tabLayoutVouchers.newTab().setText("Đã hết hạn"));

        tabLayoutVouchers.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String status = "";
                switch (tab.getPosition()) {
                    case 0:
                        status = "USABLE";
                        break;
                    case 1:
                        status = "USED";
                        break;
                    case 2:
                        status = "EXPIRED";
                        break;
                }
                couponViewModel.getUserCoupons(status);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String status = "";
                switch (tab.getPosition()) {
                    case 0: status = "USABLE"; break;
                    case 1: status = "USED"; break;
                    case 2: status = "EXPIRED"; break;
                }
                couponViewModel.getUserCoupons(status);
            }
        });
    }

    private void setupRecyclerView() {
        recyclerViewVouchers.setLayoutManager(new LinearLayoutManager(this));
        voucherAdapter = new VoucherAdapter(new ArrayList<>(), this, this);
        recyclerViewVouchers.setAdapter(voucherAdapter);
    }


    private void observeViewModel() {
        couponViewModel.getUserCoupons(null).observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerViewVouchers.setVisibility(View.GONE);
                    emptyStateLayout.setVisibility(View.GONE);
                    if (tvEmptyVoucher != null) tvEmptyVoucher.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    if (resource.getData() != null && !resource.getData().isEmpty()) {
                        recyclerViewVouchers.setVisibility(View.VISIBLE);
                        emptyStateLayout.setVisibility(View.GONE);
                        if (tvEmptyVoucher != null) tvEmptyVoucher.setVisibility(View.GONE);
                        voucherAdapter.setData(resource.getData());
                    } else {
                        recyclerViewVouchers.setVisibility(View.GONE);
                        emptyStateLayout.setVisibility(View.VISIBLE);
                        if (tvEmptyVoucher != null) tvEmptyVoucher.setVisibility(View.GONE);
                        TextView detailedEmptyText = emptyStateLayout.findViewById(R.id.tv_empty_voucher); // Assuming you used this ID inside empty_state_layout
                        if (detailedEmptyText != null) {
                            detailedEmptyText.setText("Không có voucher nào trong danh mục này.");
                        }
                    }
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    recyclerViewVouchers.setVisibility(View.GONE);
                    emptyStateLayout.setVisibility(View.VISIBLE);
                    if (tvEmptyVoucher != null) tvEmptyVoucher.setVisibility(View.GONE);
                    TextView detailedEmptyText = emptyStateLayout.findViewById(R.id.tv_empty_voucher); // Assuming same ID
                    if (detailedEmptyText != null) {
                        detailedEmptyText.setText("Lỗi tải voucher: " + resource.getMessage() + ". Vui lòng thử lại.");
                    } else {
                        Toast.makeText(this, "Lỗi tải voucher: " + resource.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        });

        couponViewModel.applyCoupon(null).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.getStatus()) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, resource.getData().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("APPLIED_COUPON_RESPONSE", resource.getData());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Lỗi áp dụng voucher: " + resource.getMessage(), Toast.LENGTH_LONG).show();
                    setResult(RESULT_CANCELED);
                    break;
            }
        });

        couponViewModel.removeCoupon(null).observe(this, resource -> {
            if (resource == null) return;
            switch (resource.getStatus()) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, resource.getData().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("REMOVED_COUPON_RESPONSE", resource.getData());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Lỗi hủy voucher: " + resource.getMessage(), Toast.LENGTH_LONG).show();
                    setResult(RESULT_CANCELED);
                    break;
            }
        });
    }

    @Override
    public void onApplyCoupon(Coupon coupon) {
        if (cartIdForApplyingVoucher == null || cartIdForApplyingVoucher == -1L) {
            Toast.makeText(this, "Vui lòng mở voucher từ màn hình thanh toán để áp dụng.", Toast.LENGTH_LONG).show();
            return;
        }
        ApplyCouponRequest request = new ApplyCouponRequest(coupon.getCouponCode(), cartIdForApplyingVoucher);
        couponViewModel.applyCoupon(request);
    }

    @Override
    public void onRemoveCoupon(Coupon coupon) {
        if (cartIdForApplyingVoucher == null || cartIdForApplyingVoucher == -1L) {
            Toast.makeText(this, "Không thể hủy voucher khi không có giỏ hàng được liên kết.", Toast.LENGTH_LONG).show();
            return;
        }
        ApplyCouponRequest request = new ApplyCouponRequest(coupon.getCouponCode(), cartIdForApplyingVoucher);
        couponViewModel.removeCoupon(request);
    }

    @Override
    public void onViewDetail(Coupon coupon) {
        Toast.makeText(this, "Xem chi tiết voucher: " + coupon.getCouponCode(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCouponSelected(Coupon coupon) {
    }

    @Override
    public void onCouponDeselected(Coupon coupon) {
    }
}