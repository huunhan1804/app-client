package com.example.dietarysupplementshop.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.interfaces.SellerCouponAPI;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.model.Coupon;
import com.example.dietarysupplementshop.requests.CreateVoucherRequest;
import com.example.dietarysupplementshop.responses.VoucherCreationResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerCouponRepository {

    private static SellerCouponRepository instance;
    private final SellerCouponAPI sellerCouponAPI;
    private final Gson gson;

    private SellerCouponRepository() {
        this.sellerCouponAPI = RetrofitClient.getRetrofitInstance().create(SellerCouponAPI.class);
        this.gson = new Gson();
    }

    public static SellerCouponRepository getInstance() {
        if (instance == null) {
            synchronized (SellerCouponRepository.class) {
                if (instance == null) {
                    instance = new SellerCouponRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<Resource<VoucherCreationResponse>> createCoupon(CreateVoucherRequest request) {
        MutableLiveData<Resource<VoucherCreationResponse>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        sellerCouponAPI.createCoupon(request).enqueue(new Callback<ResponseModel<VoucherCreationResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<VoucherCreationResponse>> call, Response<ResponseModel<VoucherCreationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VoucherCreationResponse creationResponse = gson.fromJson(gson.toJson(response.body().getData()), VoucherCreationResponse.class);
                    data.setValue(Resource.success(creationResponse));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<VoucherCreationResponse>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<List<Coupon>>> getSellerCoupons(String status) {
        MutableLiveData<Resource<List<Coupon>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        sellerCouponAPI.getSellerCoupons(status).enqueue(new Callback<ResponseModel<List<Coupon>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<Coupon>>> call, Response<ResponseModel<List<Coupon>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Type listType = new TypeToken<List<Coupon>>() {}.getType();
                    List<Coupon> coupons = gson.fromJson(gson.toJson(response.body().getData()), listType);
                    data.setValue(Resource.success(coupons));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<Coupon>>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<VoucherCreationResponse>> updateCoupon(Long couponId, CreateVoucherRequest request) {
        MutableLiveData<Resource<VoucherCreationResponse>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        sellerCouponAPI.updateCoupon(couponId, request).enqueue(new Callback<ResponseModel<VoucherCreationResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<VoucherCreationResponse>> call, Response<ResponseModel<VoucherCreationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VoucherCreationResponse updateResponse = gson.fromJson(gson.toJson(response.body().getData()), VoucherCreationResponse.class);
                    data.setValue(Resource.success(updateResponse));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<VoucherCreationResponse>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<VoucherCreationResponse>> updateCouponStatus(Long couponId, boolean isActive) {
        MutableLiveData<Resource<VoucherCreationResponse>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        sellerCouponAPI.updateCouponStatus(couponId, isActive).enqueue(new Callback<ResponseModel<VoucherCreationResponse>>() {
            @Override
            public void onResponse(Call<ResponseModel<VoucherCreationResponse>> call, Response<ResponseModel<VoucherCreationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VoucherCreationResponse statusResponse = gson.fromJson(gson.toJson(response.body().getData()), VoucherCreationResponse.class);
                    data.setValue(Resource.success(statusResponse));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<VoucherCreationResponse>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<Void>> deleteCoupon(Long couponId) {
        MutableLiveData<Resource<Void>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        sellerCouponAPI.deleteCoupon(couponId).enqueue(new Callback<ResponseModel<Void>>() {
            @Override
            public void onResponse(Call<ResponseModel<Void>> call, Response<ResponseModel<Void>> response) {
                if (response.isSuccessful()) {
                    data.setValue(Resource.success(null));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<Void>> call, Throwable t) {
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