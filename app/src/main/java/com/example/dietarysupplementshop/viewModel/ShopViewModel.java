package com.example.dietarysupplementshop.viewModel;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.dietarysupplementshop.interfaces.RetrofitClient;
import com.example.dietarysupplementshop.interfaces.ShopAPI;
import com.example.dietarysupplementshop.model.PagedResponse;
import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.model.ResponseModel;
import com.example.dietarysupplementshop.model.Shop;
import com.example.dietarysupplementshop.repositories.Resource;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopViewModel extends ViewModel {
    private static final boolean USE_FAKE_DATA = false;
    private final ShopAPI shopAPI;

    public ShopViewModel() {
        this.shopAPI = RetrofitClient.getRetrofitInstance().create(ShopAPI.class);
    }

    public LiveData<Resource<Shop>> getShopDetails(long shopId) {
        MutableLiveData<Resource<Shop>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        if (USE_FAKE_DATA) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Shop fakeShop = new Shop(shopId, "Thực phẩm chức năng ABC (Test)", "https://picsum.photos/id/1025/200");
                data.setValue(Resource.success(fakeShop));
            }, 500);
            return data;
        }
        shopAPI.getShopDetails(shopId).enqueue(new Callback<ResponseModel<Shop>>() {
            @Override
            public void onResponse(Call<ResponseModel<Shop>> call, Response<ResponseModel<Shop>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body().getData()));
                } else {
                    data.setValue(Resource.error("Lỗi tải thông tin shop", null, response.code()));
                }
            }
            @Override
            public void onFailure(Call<ResponseModel<Shop>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null, null));
            }
        });
        return data;
    }

    public LiveData<Resource<PagedResponse<Product>>> getShopProducts(long shopId, int page) {
        MutableLiveData<Resource<PagedResponse<Product>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        if (USE_FAKE_DATA) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                PagedResponse<Product> fakePagedResponse = new PagedResponse<>(createFakeShopProducts());
                data.setValue(Resource.success(fakePagedResponse));
            }, 1000);
            return data;
        }
        shopAPI.getShopProducts(shopId, page, 20).enqueue(new Callback<ResponseModel<PagedResponse<Product>>>() {
            @Override
            public void onResponse(Call<ResponseModel<PagedResponse<Product>>> call, Response<ResponseModel<PagedResponse<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body().getData()));
                } else {
                    data.setValue(Resource.error("Lỗi tải sản phẩm của shop", null, response.code()));
                }
            }
            @Override
            public void onFailure(Call<ResponseModel<PagedResponse<Product>>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null, null));
            }
        });
        return data;
    }

    private List<Product> createFakeShopProducts() {
        List<Product> productList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Product p = new Product(
                    (long) i,
                    "Sản phẩm của Shop số " + i,
                    String.format("%,d đ", 100000 + i * 5000),
                    "https://picsum.photos/200/300?random=" + i,
                    4.5
            );
            productList.add(p);
        }
        return productList;
    }
}