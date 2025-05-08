package com.example.dietarysupplementshop.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.dietarysupplementshop.interfaces.ProductAPI;
import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.requests.SearchRequest;
import com.example.dietarysupplementshop.responses.ProductInformation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private ProductAPI productAPI;
    private List<Product> cachedBestSellers;
    private List<Product> cachedBestOrders;

    public ProductRepository() {
        productAPI = RetrofitClient.getRetrofitInstance().create(ProductAPI.class);
    }

    public LiveData<ProductInformation> getProductInfo(long productId) {
        MutableLiveData<ProductInformation> data = new MutableLiveData<>();

        productAPI.getProductInfo(productId).enqueue(new Callback<ResponseModel<ProductInformation>>() {
            @Override
            public void onResponse(Call<ResponseModel<ProductInformation>> call, Response<ResponseModel<ProductInformation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Type listType = new TypeToken<ProductInformation>(){}.getType();
                    ProductInformation productInformation = new Gson().fromJson(new Gson().toJson(response.body().getData()), listType);
                    data.setValue(productInformation);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ProductInformation>> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }


    public LiveData<List<Product>> fetchBestSellers() {
        MutableLiveData<List<Product>> data = new MutableLiveData<>();

        // Kiểm tra dữ liệu đã được tải chưa
        if (cachedBestSellers != null) {
            data.setValue(cachedBestSellers);
            return data;
        }

        productAPI.getListBestSellerProduct().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Type listType = new TypeToken<List<Product>>() {}.getType();
                    List<Product> productList = new Gson().fromJson(new Gson().toJson(response.body().getData()), listType);
                    // Lưu trữ dữ liệu đã được tải
                    cachedBestSellers = productList;
                    data.setValue(productList);
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

        // Kiểm tra dữ liệu đã được tải chưa
        if (cachedBestOrders != null) {
            data.setValue(cachedBestOrders);
            return data;
        }

        productAPI.getListBestOrderProduct().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Type listType = new TypeToken<List<Product>>() {}.getType();
                    List<Product> productList = new Gson().fromJson(new Gson().toJson(response.body().getData()), listType);
                    // Lưu trữ dữ liệu đã được tải
                    cachedBestOrders = productList;
                    data.setValue(productList);
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<List<Product>> fetchRelated(long productId) {
        MutableLiveData<List<Product>> data = new MutableLiveData<>();
        productAPI.getListRelatedProduct(productId).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Type listType = new TypeToken<List<Product>>() {}.getType();
                    List<Product> productList = new Gson().fromJson(new Gson().toJson(response.body().getData()), listType);
                    data.setValue(productList);
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<Resource<List<Product>>> fetchProductResult(SearchRequest searchRequest) {
        MutableLiveData<Resource<List<Product>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        productAPI.getListSearchProduct(searchRequest).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Type listType = new TypeToken<List<Product>>() {}.getType();
                    List<Product> productList = new Gson().fromJson(new Gson().toJson(response.body().getData()), listType);
                    data.setValue(Resource.success(productList));
                } else {
                    handleErrorResponse(response, data);
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<List<Product>>> getProductByCategory(Long categoryId) {
        MutableLiveData<Resource<List<Product>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        productAPI.getListProductByCategory(categoryId).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Type listType = new TypeToken<List<Product>>() {}.getType();
                    List<Product> productList = new Gson().fromJson(new Gson().toJson(response.body().getData()), listType);
                    data.setValue(Resource.success(productList));
                } else {
                    handleErrorResponse(response, data);
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }



    private <T> void handleErrorResponse(Response<?> response, MutableLiveData<Resource<T>> data) {
        if (response.errorBody() != null && response.code() != 403 && response.code() != 401) {
            try {
                String errorJsonString = response.errorBody().string();
                ResponseModel<?> errorResponseModel = new Gson().fromJson(errorJsonString, ResponseModel.class);
                data.setValue(Resource.error(errorResponseModel.getMessage(), null));
            } catch (IOException e) {
                data.setValue(Resource.error("Unknown error occurred", null));
            }
        }
    }
}
