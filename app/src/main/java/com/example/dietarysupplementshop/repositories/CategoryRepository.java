package com.example.dietarysupplementshop.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dietarysupplementshop.interfaces.CategoryAPI;
import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.model.Category;
import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {

    private CategoryAPI categoryAPI;
    private final Gson gson = new Gson();
    public CategoryRepository() {
        categoryAPI = RetrofitClient.getRetrofitInstance().create(CategoryAPI.class);
    }
    public interface CategoryCallback {
        void onSuccess(List<Category> categoryList);
        void onError(Throwable t);
    }

    public void fetchCategories(CategoryCallback callback) {
        categoryAPI.getCategories().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    Type listType = new TypeToken<List<Category>>() {}.getType();
                    List<Category> categories = gson.fromJson(gson.toJson(response.body().getData()), listType);
                    callback.onSuccess(categories);
                } else {
                    callback.onError(new Exception("Empty or invalid response"));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
