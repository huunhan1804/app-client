package com.example.dietarysupplementshop.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.interfaces.AgencyRegistrationAPI;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.responses.AgencyInfoDTO;
import com.example.dietarysupplementshop.responses.AgencyRegistrationResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgencyRegistrationRepository implements IAgencyRepository {

    private static AgencyRegistrationRepository instance;
    private final AgencyRegistrationAPI agencyRegistrationAPI;

    public static synchronized AgencyRegistrationRepository getInstance() {
        if (instance == null) {
            instance = new AgencyRegistrationRepository();
        }
        return instance;
    }

    public AgencyRegistrationRepository() {
        agencyRegistrationAPI = RetrofitClient.getRetrofitInstance().create(AgencyRegistrationAPI.class);
    }

    @Override
    public LiveData<Resource<AgencyInfoDTO>> registerAgency(RequestBody requestBodyJson) {
        MutableLiveData<Resource<AgencyInfoDTO>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        agencyRegistrationAPI.registerAgency(requestBodyJson)
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
    public LiveData<Resource<AgencyRegistrationResponse>> getAgencyRegistrationStatus(long accountId) {
        MutableLiveData<Resource<AgencyRegistrationResponse>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        agencyRegistrationAPI.getAgencyRegistrationStatus(accountId).enqueue(new Callback<ResponseModel<AgencyRegistrationResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<AgencyRegistrationResponse>> call, @NonNull Response<ResponseModel<AgencyRegistrationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body().getData()));
                } else {
                    handleErrorResponse(response, data);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<AgencyRegistrationResponse>> call, @NonNull Throwable t) {
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