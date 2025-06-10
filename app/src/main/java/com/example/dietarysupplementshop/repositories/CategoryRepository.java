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

    public CategoryRepository() {
        categoryAPI = RetrofitClient.getRetrofitInstance().create(CategoryAPI.class);
    }

    public LiveData<List<Category>> fetchCategories() {
        MutableLiveData<List<Category>> data = new MutableLiveData<>();

        categoryAPI.getCategories().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Type listType = new TypeToken<List<Category>>() {}.getType();
                    List<Category> categories = new Gson().fromJson(new Gson().toJson(response.body().getData()), listType);
                    data.setValue(categories);
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
