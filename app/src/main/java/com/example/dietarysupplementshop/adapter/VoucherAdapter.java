package com.example.dietarysupplementshop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.constant.Validation;
import com.example.dietarysupplementshop.model.AccountCoupon;
import com.example.dietarysupplementshop.model.Coupon;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    private List<AccountCoupon> couponList;
    private Context context;
    private OnVoucherActionListener listener;
    private Long currentSelectedCouponId = null;

    public interface OnVoucherActionListener {
        void onApplyCoupon(Coupon coupon);

        void onRemoveCoupon(Coupon coupon);

        void onCouponSelected(Coupon coupon);

        void onCouponDeselected(Coupon coupon);

        void onViewDetail(Coupon coupon);
    }

    public VoucherAdapter(List<AccountCoupon> couponList, Context context, OnVoucherActionListener listener) {
        this.couponList = couponList;
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<AccountCoupon> newCouponList) {
        this.couponList = newCouponList;
        notifyDataSetChanged();
    }

    public void setSelectedCouponId(Long couponId) {
        if (this.currentSelectedCouponId != null && !this.currentSelectedCouponId.equals(couponId)) {
            int oldPos = findPositionById(this.currentSelectedCouponId);
            this.currentSelectedCouponId = null;
            if (oldPos != -1) notifyItemChanged(oldPos);
        }
        this.currentSelectedCouponId = couponId;
        if (couponId != null) {
            int newPos = findPositionById(couponId);
            if (newPos != -1) notifyItemChanged(newPos);
        }
    }

    private int findPositionById(Long couponId) {
        for (int i = 0; i < couponList.size(); i++) {
            if (couponList.get(i).getCoupon().getCouponId().equals(couponId)) {
                return i;
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        AccountCoupon accountCoupon = couponList.get(position);
        Coupon coupon = accountCoupon.getCoupon();

        holder.tvVoucherName.setText(coupon.getDescription());
        holder.tvStoreName.setText("Cửa hàng của bạn");
        if ("SHIPPING".equalsIgnoreCase(coupon.getCouponType())) {
            holder.ivVoucherTypeIcon.setImageResource(R.drawable.shipping_ic);
        } else {
            holder.ivVoucherTypeIcon.setImageResource(R.drawable.voucher);
        }


        if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
            holder.tvVoucherValue.setText(String.format(Locale.getDefault(), "%.0f%%", coupon.getDiscountValue().floatValue()));
        } else {
            holder.tvVoucherValue.setText(String.format(Locale.getDefault(), "%,.0fK", coupon.getDiscountValue().floatValue() / 1000)); // Example: 50000 -> 50K
        }


        String discountDetail = "";
        if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
            discountDetail = String.format(Locale.getDefault(), "Giảm %.0f%% cho đơn hàng", coupon.getDiscountValue().floatValue());
            if (coupon.getMaxQuantity() != null && coupon.getMaxQuantity() > 0) {
                discountDetail += String.format(" (tối đa %,.0f đ)", coupon.getMaxQuantity().floatValue());
            }
        } else {
            discountDetail = String.format(Locale.getDefault(), "Giảm %,.0f đ", coupon.getDiscountValue().floatValue());
        }

        if (coupon.getMinPurchaseAmount() != null && coupon.getMinPurchaseAmount().floatValue() > 0) {
            discountDetail += String.format("\nĐơn tối thiểu từ %,.0f đ", coupon.getMinPurchaseAmount().floatValue());
        }
        holder.tvVoucherDescription.setText(discountDetail);


        if (coupon.getExpiryDate() != null) {
            long diff = coupon.getExpiryDate().getTime() - new Date().getTime();
            long daysRemaining = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            if (daysRemaining > 0) {
                holder.tvVoucherExpiry.setText("HSD: " + Validation.formatDate(coupon.getExpiryDate()) + " (Còn " + daysRemaining + " ngày)");
                holder.tvVoucherExpiry.setTextColor(ContextCompat.getColor(context, R.color.red_error)); // Still valid, but highlighted
            } else {
                holder.tvVoucherExpiry.setText("HSD: Đã hết hạn");
                holder.tvVoucherExpiry.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray)); // Gray for expired
            }
        } else {
            holder.tvVoucherExpiry.setText("Hạn dùng: Vô thời hạn");
            holder.tvVoucherExpiry.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }

        updateVoucherUI(holder, coupon);

        holder.btnVoucherDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewDetail(coupon);
            }
        });

        holder.btnUseVoucher.setOnClickListener(v -> {
            if (coupon.isUsable()) {
                if (currentSelectedCouponId != null && currentSelectedCouponId.equals(coupon.getCouponId())) {
                    currentSelectedCouponId = null;
                    listener.onRemoveCoupon(coupon);
                    listener.onCouponDeselected(coupon);
                } else {
                    Long oldSelectedId = currentSelectedCouponId;
                    currentSelectedCouponId = coupon.getCouponId();
                    if (oldSelectedId != null) {
                        int oldPos = findPositionById(oldSelectedId);
                        if (oldPos != -1) notifyItemChanged(oldPos);
                    }
                    listener.onApplyCoupon(coupon);
                    listener.onCouponSelected(coupon);
                }
                notifyItemChanged(position);
            } else {
                Toast.makeText(context, "Voucher này không thể sử dụng.", Toast.LENGTH_SHORT).show();
            }
        });

        if (currentSelectedCouponId != null && currentSelectedCouponId.equals(coupon.getCouponId())) {
            holder.itemView.setBackgroundResource(R.drawable.rounded_chip_background_selected);
            holder.btnUseVoucher.setText("Đã chọn");
            holder.btnUseVoucher.setBackgroundColor(ContextCompat.getColor(context, R.color.color_app));
            holder.btnUseVoucher.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.rounded_chip_background_default);
            updateVoucherUI(holder, coupon);
        }
    }

    private void updateVoucherUI(VoucherViewHolder holder, Coupon coupon) {
        boolean isUsable = coupon.getActivated() != null && coupon.getActivated() &&
                (coupon.getRemainingQuantity() == null || coupon.getRemainingQuantity() > 0) &&
                (coupon.getExpiryDate() == null || coupon.getExpiryDate().after(new Date()));

        if (!isUsable) {
            holder.btnUseVoucher.setText("Không dùng được");
            holder.btnUseVoucher.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            holder.btnUseVoucher.setTextColor(Color.WHITE);
            holder.btnUseVoucher.setEnabled(false);
            holder.itemView.setAlpha(0.6f);
        } else {
            holder.btnUseVoucher.setText("Sử dụng ngay");
            holder.btnUseVoucher.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));
            holder.btnUseVoucher.setTextColor(Color.WHITE);
            holder.btnUseVoucher.setEnabled(true);
            holder.itemView.setAlpha(1.0f);
        }

        if (coupon.getExpiryDate() != null && coupon.getExpiryDate().before(new Date())) {
            holder.tvVoucherExpiry.setText("HSD: Đã hết hạn");
            holder.btnUseVoucher.setText("Đã hết hạn");
            holder.btnUseVoucher.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            holder.btnUseVoucher.setTextColor(Color.WHITE);
            holder.btnUseVoucher.setEnabled(false);
            holder.itemView.setAlpha(0.6f);
        } else if (coupon.getRemainingQuantity() != null && coupon.getRemainingQuantity() <= 0) {
            holder.tvVoucherDescription.setText("Số lượng còn lại: Hết");
            holder.btnUseVoucher.setText("Hết lượt");
            holder.btnUseVoucher.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            holder.btnUseVoucher.setTextColor(Color.WHITE);
            holder.btnUseVoucher.setEnabled(false);
            holder.itemView.setAlpha(0.6f);
        }
    }

    @Override
    public int getItemCount() {
        return couponList != null ? couponList.size() : 0;
    }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder {
        ImageView ivVoucherTypeIcon, ivStoreLogo;
        TextView tvVoucherValue, tvVoucherName, tvStoreName;
        TextView tvVoucherDescription, tvVoucherExpiry;
        Button btnVoucherDetail, btnUseVoucher;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            ivVoucherTypeIcon = itemView.findViewById(R.id.iv_voucher_type_icon);
            tvVoucherValue = itemView.findViewById(R.id.tv_voucher_value);
            tvVoucherName = itemView.findViewById(R.id.tv_voucher_name);
            ivStoreLogo = itemView.findViewById(R.id.iv_store_logo);
            tvStoreName = itemView.findViewById(R.id.tv_store_name);
            tvVoucherDescription = itemView.findViewById(R.id.tv_voucher_description);
            tvVoucherExpiry = itemView.findViewById(R.id.tv_voucher_expiry);
            btnVoucherDetail = itemView.findViewById(R.id.btn_voucher_detail);
            btnUseVoucher = itemView.findViewById(R.id.btn_use_voucher);
        }
    }
}