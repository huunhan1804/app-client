package com.example.dietarysupplementshop.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dietarysupplementshop.interfaces.ProductAPI;
import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private ProductAPI productAPI;

    public ProductRepository() {
        productAPI = RetrofitClient.getRetrofitInstance().create(ProductAPI.class);
    }

    public LiveData<List<Product>> fetchBestSellers() {
        MutableLiveData<List<Product>> data = new MutableLiveData<>();
        productAPI.getListBestSellerProduct().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        Type listType = new TypeToken<List<Product>>() {}.getType();
                        List<Product> productList = new Gson().fromJson(new Gson().toJson(response.body().getData()), listType);
                        data.setValue(productList);
                    }

                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Product>> fetchBestOrders() {
        MutableLiveData<List<Product>> data = new MutableLiveData<>();
        productAPI.getListBestOrderProduct().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        Type listType = new TypeToken<List<Product>>() {}.getType();
                        List<Product> productList = new Gson().fromJson(new Gson().toJson(response.body().getData()), listType);
                        data.setValue(productList);
                    }

                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

}
