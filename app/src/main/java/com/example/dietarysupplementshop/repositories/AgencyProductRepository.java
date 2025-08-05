package com.example.dietarysupplementshop.repositories;

import com.example.dietarysupplementshop.interfaces.AgencyProductAPI;
import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.AddNewProductRequest;
import com.example.dietarysupplementshop.requests.UpdateProductRequest;
import com.example.dietarysupplementshop.responses.ProductInfoDTO;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class AgencyProductRepository {
    private final AgencyProductAPI agencyProductAPI;

    public AgencyProductRepository() {
        this.agencyProductAPI = RetrofitClient.getRetrofitInstance().create(AgencyProductAPI.class);
    }



    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    private <T> void handleApiError(Response<?> response, ApiCallback<T> callback) {
        String errorMessage = "Lỗi không xác định.";
        if (response.errorBody() != null) {
            try {
                ResponseModel<?> errorResponse = new Gson().fromJson(response.errorBody().string(),  ResponseModel.class);
                if (errorResponse != null && errorResponse.getMessage() != null) {
                    errorMessage = errorResponse.getMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        callback.onError(errorMessage);
    }

    public void createProduct(AddNewProductRequest request, ApiCallback<ProductInfoDTO> callback) {
        agencyProductAPI.createProduct(request).enqueue(new Callback< ResponseModel<ProductInfoDTO>>() {
            @Override
            public void onResponse(Call< ResponseModel<ProductInfoDTO>> call, Response< ResponseModel<ProductInfoDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    handleApiError(response, callback);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ProductInfoDTO>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void updateProduct(Long productId, UpdateProductRequest request, ApiCallback<ProductInfoDTO> callback) {
        agencyProductAPI.updateProduct(productId, request).enqueue(new Callback<ResponseModel<ProductInfoDTO>>() {
            @Override
            public void onResponse(Call<ResponseModel<ProductInfoDTO>> call, Response<ResponseModel<ProductInfoDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    handleApiError(response, callback);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ProductInfoDTO>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void deleteProduct(Long productId, ApiCallback<ProductInfoDTO> callback) {
        agencyProductAPI.deleteProduct(productId).enqueue(new Callback<ResponseModel<ProductInfoDTO>>() {
            @Override
            public void onResponse(Call<ResponseModel<ProductInfoDTO>> call, Response<ResponseModel<ProductInfoDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(null);
                } else {
                    handleApiError(response, callback);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ProductInfoDTO>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getAgencyProducts(String statusCode, ApiCallback<List<ProductInfoDTO>> callback) {
        agencyProductAPI.getAgencyProducts(statusCode).enqueue(new Callback<ResponseModel<List<ProductInfoDTO>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<ProductInfoDTO>>> call, Response<ResponseModel<List<ProductInfoDTO>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    handleApiError(response, callback);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<ProductInfoDTO>>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    public void updateProductStatus(long productId, String newStatus, ApiCallback<ProductInfoDTO> apiCallback) {
        agencyProductAPI.updateProductStatus(productId, newStatus)
                .enqueue(new Callback<ResponseModel<ProductInfoDTO>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<ProductInfoDTO>> call, Response<ResponseModel<ProductInfoDTO>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            apiCallback.onSuccess(response.body().getData());
                        } else {
                            handleApiError(response, apiCallback);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<ProductInfoDTO>> call, Throwable t) {
                        apiCallback.onError(t.getMessage());
                    }
                });
    }


}