package com.example.dietarysupplementshop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.dietarysupplementshop.responses.AccountInformation;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;

public class CustomerProfileFragment extends Fragment {

    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final String ROLE_AGENCY = "agency";

    private AccountViewModel accountViewModel;
    private ImageView avatarImageView;
    private TextView userName;
    private TextView userId;
    private TextView memberLevel;
    private TextView tvAgencyChannelText;

    public CustomerProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_profile, container, false);

        bindViews(view);

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);

        setupClickListeners(view);

        observeViewModel();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        accountViewModel.loadAccountInfo();
    }

    private void bindViews(View view) {
        avatarImageView = view.findViewById(R.id.avatarImageView);
        userName = view.findViewById(R.id.user_name);
        userId = view.findViewById(R.id.user_id);
        memberLevel = view.findViewById(R.id.member_level);
        tvAgencyChannelText = view.findViewById(R.id.tv_agency_channel_text);
    }

    private void setupClickListeners(View view) {
        view.findViewById(R.id.frameAvatar).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });
        view.findViewById(R.id.tv_view_order_history).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OrdersActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.btn_pending_confirmation).setOnClickListener(this::onOrderStatusClick);
        view.findViewById(R.id.btn_shipping).setOnClickListener(this::onOrderStatusClick);
        view.findViewById(R.id.btn_delivered).setOnClickListener(this::onOrderStatusClick);
        view.findViewById(R.id.btn_cancelled).setOnClickListener(this::onOrderStatusClick);

        view.findViewById(R.id.btn_financial_services).setOnClickListener(this::onUtilityClick);
        view.findViewById(R.id.btn_membership).setOnClickListener(this::onUtilityClick);
        view.findViewById(R.id.btn_voucher).setOnClickListener(this::onUtilityClick);
        view.findViewById(R.id.btn_health_hub).setOnClickListener(this::onUtilityClick);

        view.findViewById(R.id.customer_support).setOnClickListener(this::onOtherFunctionClick);
    }

    private void observeViewModel() {
        accountViewModel.getAccountInfo().observe(getViewLifecycleOwner(), result -> {
            if (result == null) return;

            switch (result.getStatus()) {
                case SUCCESS:
                    if (result.getData() != null) {
                        updateUI(result.getData());
                    }
                    break;
                case ERROR:
                    Toast.makeText(getContext(), "Lỗi: " + result.getMessage(), Toast.LENGTH_LONG).show();
                    break;
                case LOADING:
                    break;
            }
        });
    }

    private void updateUI(AccountInformation accountInfo) {
        if (accountInfo.getAccountProfileDTO() != null) {
            userName.setText(accountInfo.getAccountProfileDTO().getFullname());
        } else {
            userName.setText("Chưa cập nhật");
        }
        userId.setText("ID: " + accountInfo.getId());

        if (getContext() != null) {
            Glide.with(this)
                    .load(accountInfo.getAvatar_url())
                    .placeholder(R.drawable.image_4)
                    .error(R.drawable.image_4)
                    .circleCrop()
                    .into(avatarImageView);
        }

        View btnAgencyChannel = requireView().findViewById(R.id.btn_agency_channel);
        String roleCode = accountInfo.getRole_code();
        String status = accountInfo.getStatus(); // Lấy trường status từ backend

        // Thêm các hằng số trạng thái để so sánh
        final String ROLE_AGENCY = "agency";
        final String STATUS_PENDING = "PENDING";
        final String STATUS_APPROVED = "APPROVED";
        final String STATUS_REJECTED = "REJECTED";
        final String STATUS_NOT_REGISTERED = "NOT_REGISTERED";


        if (roleCode != null && roleCode.equals(ROLE_AGENCY) && status != null && status.equals(STATUS_APPROVED)) {
            // Trường hợp 1: Đã là người bán và trạng thái được duyệt.
            // Chú ý: Cả hai điều kiện này phải khớp để tránh lỗi logic
            memberLevel.setText("Thành viên Bán hàng");
            tvAgencyChannelText.setText("Quản lý Shop");
            btnAgencyChannel.setOnClickListener(v -> {
                // Chuyển thẳng đến màn hình quản lý shop
                startActivity(new Intent(getActivity(), AgencyMainActivity.class));
            });
        } else if (status != null && (status.equals(STATUS_PENDING) || status.equals(STATUS_REJECTED))) {
            // Trường hợp 2: Chưa là người bán, nhưng đã có đơn đăng ký đang chờ hoặc bị từ chối.
            // Logic này cũng bao gồm cả khi role là customer nhưng status là pending/rejected
            memberLevel.setText("Thành viên Mua hàng");
            tvAgencyChannelText.setText("Trạng thái Đăng ký");
            btnAgencyChannel.setOnClickListener(v -> {
                // Chuyển đến màn hình trạng thái đăng ký
                startActivity(new Intent(getActivity(), AgencyRegistrationStatusActivity.class));
            });
        } else {
            // Trường hợp 3: Người dùng là khách hàng bình thường (role=customer, status=NOT_REGISTERED)
            // hoặc các trạng thái không xác định khác.
            memberLevel.setText("Thành viên Mua hàng");
            tvAgencyChannelText.setText("Kênh Bán Hàng");
            btnAgencyChannel.setOnClickListener(v -> {
                // Chuyển đến màn hình bắt đầu đăng ký
                startActivity(new Intent(getActivity(), AgencyWelcomeRegistrationActivity.class));
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                Toast.makeText(getContext(), "Đã chọn ảnh: " + imageUri.toString(), Toast.LENGTH_SHORT).show();
                Glide.with(this).load(imageUri).circleCrop().into(avatarImageView);
            }
        }
    }

    public void onOrderStatusClick(View view) {
        String statusKey = "ALL";
        int viewId = view.getId();

        if (viewId == R.id.btn_pending_confirmation) {
            statusKey = "PENDING"; // Chờ xác nhận
        } else if (viewId == R.id.btn_shipping) {
            statusKey = "SHIPPING"; // Đang giao
        } else if (viewId == R.id.btn_delivered) {
            statusKey = "DELIVERED"; // Đã giao
        } else if (viewId == R.id.btn_cancelled) {
            statusKey = "CANCELLED"; // Đã hủy
        }

        Intent intent = new Intent(getActivity(), OrdersActivity.class);
        intent.putExtra("initial_status_key", statusKey);
        startActivity(intent);
    }

    public void onUtilityClick(View view) {
        Intent intent = null;
        int viewId = view.getId();

        if (viewId == R.id.btn_financial_services) {
            Toast.makeText(getContext(), "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        } else if (viewId == R.id.btn_membership) {
            intent = new Intent(getActivity(), MembershipActivity.class);
        } else if (viewId == R.id.btn_voucher) {
            intent = new Intent(getActivity(), VouchersActivity.class);
        } else if (viewId == R.id.btn_health_hub) {
            intent = new Intent(getActivity(), HealthHubCtucareActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    public void onOtherFunctionClick(View view) {
        if (view.getId() == R.id.customer_support) {
            Toast.makeText(getContext(), "Chuyển đến Trung tâm hỗ trợ", Toast.LENGTH_SHORT).show();

        }
    }
}