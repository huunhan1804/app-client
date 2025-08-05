package com.example.dietarysupplementshop.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.model.Coupon;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.repositories.AgencyCouponRepository;
import com.example.dietarysupplementshop.requests.CreateVoucherRequest;
import com.example.dietarysupplementshop.responses.VoucherCreationResponse;

import java.util.List;

public class AgencyCouponViewModel extends ViewModel {

    private final AgencyCouponRepository agencyCouponRepository;
    private MutableLiveData<Resource<List<Coupon>>> agencyCoupons;

    public AgencyCouponViewModel() {
        this.agencyCouponRepository = AgencyCouponRepository.getInstance();
    }

    public LiveData<Resource<List<Coupon>>> getCoupons(String status) {
        if (agencyCoupons == null || !("ALL".equals(status) || "ACTIVE".equals(status) || "INACTIVE".equals(status) || "EXPIRED".equals(status)) ) { // Load initially or on specific status change
            agencyCoupons = new MutableLiveData<>();
            loadAgencyCoupons(status);
        }
        return agencyCoupons;
    }

    public void loadAgencyCoupons(String status) {
        agencyCouponRepository.getAgencyCoupons(status).observeForever(resource -> {
            agencyCoupons.setValue(resource);
        });
    }

    public LiveData<Resource<VoucherCreationResponse>> createCoupon(CreateVoucherRequest request) {
        return agencyCouponRepository.createCoupon(request);
    }

    public LiveData<Resource<VoucherCreationResponse>> updateCoupon(Long couponId, CreateVoucherRequest request) {
        return agencyCouponRepository.updateCoupon(couponId, request);
    }

    public LiveData<Resource<VoucherCreationResponse>> updateCouponStatus(Long couponId, boolean isActive) {
        return agencyCouponRepository.updateCouponStatus(couponId, isActive);
    }

    public LiveData<Resource<Void>> deleteCoupon(Long couponId) {
        return agencyCouponRepository.deleteCoupon(couponId);
    }

    public void refreshAgencyCoupons(String status) {
        loadAgencyCoupons(status);
    }
}