package com.example.dietarysupplementshop.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.interfaces.SellerRegistrationAPI;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.responses.AgencyInfoDTO;
import com.example.dietarysupplementshop.responses.SellerRegistrationResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerRegistrationRepository implements ISellerRepository {

    private static SellerRegistrationRepository instance;
    private final SellerRegistrationAPI sellerRegistrationAPI;

    public static synchronized SellerRegistrationRepository getInstance() {
        if (instance == null) {
            instance = new SellerRegistrationRepository();
        }
        return instance;
    }

    public SellerRegistrationRepository() {
        sellerRegistrationAPI = RetrofitClient.getRetrofitInstance().create(SellerRegistrationAPI.class);
    }

    @Override
    public LiveData<Resource<AgencyInfoDTO>> registerSeller(RequestBody requestBodyJson) {
        MutableLiveData<Resource<AgencyInfoDTO>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        sellerRegistrationAPI.registerSeller(requestBodyJson)
                .enqueue(new Callback<ResponseModel<AgencyInfoDTO>>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseModel<AgencyInfoDTO>> call, @NonNull Response<ResponseModel<AgencyInfoDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            data.setValue(Resource.success(response.body().getData()));
                        } else {
                            handleErrorResponse(response, data);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseModel<AgencyInfoDTO>> call, @NonNull Throwable t) {
                        data.setValue(Resource.error(t.getMessage(), null));
                    }
                });
        return data;
    }

    @Override
    public LiveData<Resource<SellerRegistrationResponse>> getSellerRegistrationStatus(long accountId) {
        MutableLiveData<Resource<SellerRegistrationResponse>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        sellerRegistrationAPI.getSellerRegistrationStatus(accountId).enqueue(new Callback<ResponseModel<SellerRegistrationResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<SellerRegistrationResponse>> call, @NonNull Response<ResponseModel<SellerRegistrationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body().getData()));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<SellerRegistrationResponse>> call, @NonNull Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    private <T> void handleErrorResponse(Response<?> response, MutableLiveData<Resource<T>> data) {
        int errorCode = response.code();
        String errorMessage = "Lỗi không xác định";

        if (response.errorBody() != null) {
            try {
                String errorJsonString = response.errorBody().string();
                ResponseModel<?> errorResponseModel = new Gson().fromJson(errorJsonString, ResponseModel.class);
                if (errorResponseModel != null && errorResponseModel.getMessage() != null) {
                    errorMessage = errorResponseModel.getMessage();
                }
            } catch (IOException e) {
                errorMessage = "Lỗi khi đọc phản hồi.";
            }
        } else {
            errorMessage = "Lỗi không xác định: " + errorCode;
        }

        data.setValue(Resource.error(errorMessage, null, errorCode));
    }
}