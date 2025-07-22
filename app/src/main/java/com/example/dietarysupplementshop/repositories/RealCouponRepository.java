package com.example.dietarysupplementshop.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dietarysupplementshop.interfaces.CouponAPI;
import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.model.AccountCoupon;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.ApplyCouponRequest;
import com.example.dietarysupplementshop.responses.AppliedCouponResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class RealCouponRepository {

    private static RealCouponRepository instance;
    private final CouponAPI couponAPI;
    private Gson gson;

    private RealCouponRepository() {
        this.couponAPI = RetrofitClient.getRetrofitInstance().create(CouponAPI.class);
        this.gson = new Gson();
    }

    public static RealCouponRepository getInstance() {
        if (instance == null) {
            synchronized (RealCouponRepository.class) {
                if (instance == null) {
                    instance = new RealCouponRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<Resource<List<AccountCoupon>>> getUserCoupons(String status) {
        MutableLiveData<Resource<List<AccountCoupon>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        couponAPI.getUserCoupons(status).enqueue(new Callback<ResponseModel<List<AccountCoupon>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<AccountCoupon>>> call, Response<ResponseModel<List<AccountCoupon>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Type listType = new TypeToken<List<AccountCoupon>>() {}.getType();
                    List<AccountCoupon> coupons = gson.fromJson(gson.toJson(response.body().getData()), listType);
                    data.setValue(Resource.success(coupons));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<AccountCoupon>>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<AppliedCouponResponse>> applyCouponToCart(ApplyCouponRequest request) {
        MutableLiveData<Resource<AppliedCouponResponse>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        couponAPI.applyCouponToCart(request).enqueue(new Callback<ResponseModel<AppliedCouponResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<AppliedCouponResponse>> call, Response<ResponseModel<AppliedCouponResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AppliedCouponResponse appliedResponse = gson.fromJson(gson.toJson(response.body().getData()), AppliedCouponResponse.class);
                    data.setValue(Resource.success(appliedResponse));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<AppliedCouponResponse>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<AppliedCouponResponse>> removeCouponFromCart(ApplyCouponRequest request) {
        MutableLiveData<Resource<AppliedCouponResponse>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        couponAPI.removeCouponFromCart(request).enqueue(new Callback<ResponseModel<AppliedCouponResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<AppliedCouponResponse>> call, Response<ResponseModel<AppliedCouponResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AppliedCouponResponse removedResponse = gson.fromJson(gson.toJson(response.body().getData()), AppliedCouponResponse.class);
                    data.setValue(Resource.success(removedResponse));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<AppliedCouponResponse>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    private <T> void handleErrorResponse(Response<?> response, MutableLiveData<Resource<T>> data) {
        if (response.errorBody() != null) {
            try {
                String errorJsonString = response.errorBody().string();
                ResponseModel<?> errorResponseModel = gson.fromJson(errorJsonString, ResponseModel.class);
                data.setValue(Resource.error(errorResponseModel.getMessage(), null, response.code()));
            } catch (IOException e) {
                data.setValue(Resource.error("Lỗi không xác định khi đọc phản hồi lỗi.", null, response.code()));
            }
        } else {
            data.setValue(Resource.error("Lỗi không xác định: Mã " + response.code(), null, response.code()));
        }
    }
}