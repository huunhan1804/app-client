package com.example.dietarysupplementshop.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.dietarysupplementshop.model.AccountCoupon;
import com.example.dietarysupplementshop.repositories.CouponRepository; // Import interface
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.requests.ApplyCouponRequest;
import com.example.dietarysupplementshop.responses.AppliedCouponResponse;

import java.util.List;

public class CouponViewModel extends ViewModel {

    private final CouponRepository repository;

    public CouponViewModel(CouponRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<List<AccountCoupon>>> getUserCoupons(String statusFilter) {
        return repository.getUserCoupons(statusFilter);
    }

    public LiveData<Resource<AppliedCouponResponse>> applyCoupon(ApplyCouponRequest request) {
        return repository.applyCoupon(request);
    }

    public LiveData<Resource<AppliedCouponResponse>> removeCoupon(ApplyCouponRequest request) {
        return repository.removeCoupon(request);
    }


    public static class Factory implements ViewModelProvider.Factory {
        private final CouponRepository repository;

        public Factory(CouponRepository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CouponViewModel.class)) {
                return (T) new CouponViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}