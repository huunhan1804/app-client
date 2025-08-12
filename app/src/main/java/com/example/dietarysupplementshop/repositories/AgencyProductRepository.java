package com.example.dietarysupplementshop.repositories;

import com.example.dietarysupplementshop.interfaces.AgencyProductAPI;
import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.AddNewProductRequest;
import com.example.dietarysupplementshop.requests.UpdateProductRequest;
import com.example.dietarysupplementshop.responses.ProductFullDTO;
import com.example.dietarysupplementshop.responses.ProductInfoDTO;
import com.example.dietarysupplementshop.responses.ShopInfoDTO;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

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
                ResponseModel<?> errorResponse = new Gson().fromJson(response.errorBody().string(), ResponseModel.class);
                if (errorResponse != null && errorResponse.getMessage() != null) {
                    errorMessage = errorResponse.getMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        callback.onError(errorMessage);
    }
    public void getShopInfo(ApiCallback<ShopInfoDTO> callback) {
        agencyProductAPI.getShopInfo().enqueue(new Callback<ResponseModel<ShopInfoDTO>>() {
            @Override
            public void onResponse(Call<ResponseModel<ShopInfoDTO>> call, Response<ResponseModel<ShopInfoDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    handleApiError(response, callback);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ShopInfoDTO>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getAgencyProducts(String statusCode, ApiCallback<List<ProductInfoDTO>> callback) {
        agencyProductAPI.getAgencyProductsByStatus(statusCode).enqueue(new Callback<ResponseModel<List<ProductInfoDTO>>>() {
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

    public void getProductDetails(Long productId, ApiCallback<ProductFullDTO> callback) {
        agencyProductAPI.getProductDetails(productId).enqueue(new Callback<ResponseModel<ProductFullDTO>>() {
            @Override
            public void onResponse(Call<ResponseModel<ProductFullDTO>> call, Response<ResponseModel<ProductFullDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    handleApiError(response, (ApiCallback) callback);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ProductFullDTO>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void createProduct(AddNewProductRequest request, ApiCallback<ProductFullDTO> callback) {
        agencyProductAPI.createProduct(request).enqueue(new Callback<ResponseModel<ProductFullDTO>>() {
            @Override
            public void onResponse(Call<ResponseModel<ProductFullDTO>> call, Response<ResponseModel<ProductFullDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    handleApiError(response, (ApiCallback) callback);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ProductFullDTO>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void updateProduct(Long productId, UpdateProductRequest request, ApiCallback<ProductFullDTO> callback) {
        agencyProductAPI.updateProduct(productId, request).enqueue(new Callback<ResponseModel<ProductFullDTO>>() {
            @Override
            public void onResponse(Call<ResponseModel<ProductFullDTO>> call, Response<ResponseModel<ProductFullDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getData());
                } else {
                    handleApiError(response, (ApiCallback) callback);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ProductFullDTO>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void deleteProduct(Long productId, ApiCallback<Void> callback) {
        agencyProductAPI.deleteProduct(productId).enqueue(new Callback<ResponseModel<Void>>() {
            @Override
            public void onResponse(Call<ResponseModel<Void>> call, Response<ResponseModel<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(null);
                } else {
                    handleApiError(response, (ApiCallback) callback);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<Void>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void disableSellingProduct(long productId, ApiCallback<ProductInfoDTO> apiCallback) {
        agencyProductAPI.disableSellingProduct(productId)
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