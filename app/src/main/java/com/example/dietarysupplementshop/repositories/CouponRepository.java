package com.example.dietarysupplementshop.repositories;

import androidx.lifecycle.LiveData;

import com.example.dietarysupplementshop.model.AccountCoupon;
import com.example.dietarysupplementshop.requests.ApplyCouponRequest;
import com.example.dietarysupplementshop.responses.AppliedCouponResponse;

import java.util.List;


public interface CouponRepository {
    // Phương thức để lấy danh sách voucher theo trạng thái
    LiveData<Resource<List<AccountCoupon>>> getUserCoupons(String status);

    // Phương thức để áp dụng voucher
    LiveData<Resource<AppliedCouponResponse>> applyCoupon(ApplyCouponRequest request);

    // Phương thức để hủy áp dụng voucher
    LiveData<Resource<AppliedCouponResponse>> removeCoupon(ApplyCouponRequest request);
}