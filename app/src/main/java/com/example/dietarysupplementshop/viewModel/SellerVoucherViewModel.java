package com.example.dietarysupplementshop.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.model.Coupon;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.repositories.SellerCouponRepository;
import com.example.dietarysupplementshop.requests.CreateVoucherRequest;
import com.example.dietarysupplementshop.responses.VoucherCreationResponse;

import java.util.List;

public class SellerVoucherViewModel extends ViewModel {

    private final SellerCouponRepository sellerCouponRepository;
    private MutableLiveData<Resource<List<Coupon>>> sellerCoupons;

    public SellerVoucherViewModel() {
        this.sellerCouponRepository = SellerCouponRepository.getInstance();
    }

    public LiveData<Resource<List<Coupon>>> getSellerCoupons(String status) {
        if (sellerCoupons == null || !("ALL".equals(status) || "ACTIVE".equals(status) || "INACTIVE".equals(status) || "EXPIRED".equals(status)) ) { // Load initially or on specific status change
            sellerCoupons = new MutableLiveData<>();
            loadSellerCoupons(status);
        }
        return sellerCoupons;
    }

    public void loadSellerCoupons(String status) {
        sellerCouponRepository.getSellerCoupons(status).observeForever(resource -> {
            sellerCoupons.setValue(resource);
        });
    }

    public LiveData<Resource<VoucherCreationResponse>> createCoupon(CreateVoucherRequest request) {
        return sellerCouponRepository.createCoupon(request);
    }

    public LiveData<Resource<VoucherCreationResponse>> updateCoupon(Long couponId, CreateVoucherRequest request) {
        return sellerCouponRepository.updateCoupon(couponId, request);
    }

    public LiveData<Resource<VoucherCreationResponse>> updateCouponStatus(Long couponId, boolean isActive) {
        return sellerCouponRepository.updateCouponStatus(couponId, isActive);
    }

    public LiveData<Resource<Void>> deleteCoupon(Long couponId) {
        return sellerCouponRepository.deleteCoupon(couponId);
    }

    public void refreshSellerCoupons(String status) {
        loadSellerCoupons(status);
    }
}