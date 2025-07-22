package com.example.dietarysupplementshop;

import static com.example.dietarysupplementshop.repositories.Resource.Status.LOADING;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.responses.AccountInformation;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;

public class BuyerProfileOverviewFragment extends Fragment {

    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final String ROLE_SELLER = "agency";

    private AccountViewModel accountViewModel;
    private ImageView avatarImageView;
    private TextView userName;
    private TextView userId;
    private TextView memberLevel;
    private TextView tvSellerChannelText;

    public BuyerProfileOverviewFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buyer_profile, container, false);

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
        tvSellerChannelText = view.findViewById(R.id.tv_seller_channel_text);
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

        View btnSellerChannel = requireView().findViewById(R.id.btn_seller_channel);
        boolean isSeller = ROLE_SELLER.equals(accountInfo.getRole_code());

        if (isSeller) {
            memberLevel.setText("Thành viên Bán hàng");
            tvSellerChannelText.setText("Quản lý Shop");
            btnSellerChannel.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), SellerMainActivity.class));
            });
        } else {
            memberLevel.setText("Thành viên Mua hàng");
            tvSellerChannelText.setText("Kênh Bán Hàng");
            btnSellerChannel.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), SellerRegistrationActivity.class));
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