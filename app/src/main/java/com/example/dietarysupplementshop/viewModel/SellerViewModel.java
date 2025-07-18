package com.example.dietarysupplementshop.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.interfaces.RetrofitClient; // Sử dụng RetrofitClient
import com.example.dietarysupplementshop.interfaces.SellerAPI;
import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.requests.UpdateStatusRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerViewModel extends ViewModel {

    private final SellerAPI sellerAPI;

    public SellerViewModel() {
        sellerAPI = RetrofitClient.getRetrofitInstance().create(SellerAPI.class);
    }

    public LiveData<Resource<List<Order>>> getSellerOrders(String status) {
        MutableLiveData<Resource<List<Order>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        sellerAPI.getSellerOrders(status).enqueue(new Callback<ResponseModel<List<Order>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<Order>>> call, Response<ResponseModel<List<Order>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body().getData()));
                } else {
                    data.setValue(Resource.error("Lỗi: " + response.code(), null, response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<Order>>> call, Throwable t) {
                data.setValue(Resource.error("Thất bại: " + t.getMessage(), null, null));
            }
        });
        return data;
    }

    public LiveData<Resource<Order>> updateOrderStatus(long orderId, String newStatus) {
        MutableLiveData<Resource<Order>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        UpdateStatusRequest request = new UpdateStatusRequest(newStatus);

        sellerAPI.updateOrderStatus(orderId, request).enqueue(new Callback<ResponseModel<Order>>() {
            @Override
            public void onResponse(Call<ResponseModel<Order>> call, Response<ResponseModel<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error("Cập nhật thất bại: " + response.code(), null, response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<Order>> call, Throwable t) {
                result.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null, null));
            }
        });

        return result;
    }
}