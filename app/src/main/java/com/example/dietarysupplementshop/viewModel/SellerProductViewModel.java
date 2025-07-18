package com.example.dietarysupplementshop.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.model.ProductSeller;
import com.example.dietarysupplementshop.repositories.ProductRepository;
import java.util.List;

public class SellerProductViewModel extends ViewModel {
    private final ProductRepository productRepository;
    private final MutableLiveData<List<ProductSeller>> _allSellerProducts = new MutableLiveData<>();
    public LiveData<List<ProductSeller>> allSellerProducts = _allSellerProducts;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    public SellerProductViewModel() {
        productRepository = new ProductRepository();
        loadAllSellerProducts();
    }

    public void loadAllSellerProducts() {
        _isLoading.setValue(true);
        // Thay thế cuộc gọi ProductApiService bằng ProductRepository
        productRepository.getAllSellerProducts(new ProductRepository.ProductSellerCallback<List<ProductSeller>>() {
            @Override
            public void onSuccess(List<ProductSeller> result) {
                _allSellerProducts.postValue(result);
                _isLoading.postValue(false);
            }

            @Override
            public void onError(Throwable t) {
                _errorMessage.postValue("Lỗi tải sản phẩm: " + t.getMessage());
                _isLoading.postValue(false);
            }

            @Override
            public void onError(String errorMessage) {
                _errorMessage.postValue(errorMessage);
                _isLoading.postValue(false);
            }
        });
    }

    public void updateProductStatus(String productId, String newStatus) {
        _isLoading.setValue(true);
        productRepository.updateProductStatus(productId, newStatus, new ProductRepository.ProductSellerCallback<ProductSeller>() {
            @Override
            public void onSuccess(ProductSeller result) {
                loadAllSellerProducts(); // Tải lại danh sách sau khi cập nhật
            }

            @Override
            public void onError(Throwable t) {
                _errorMessage.postValue("Lỗi cập nhật trạng thái: " + t.getMessage());
                _isLoading.postValue(false);
            }

            @Override
            public void onError(String errorMessage) {
                _errorMessage.postValue(errorMessage);
                _isLoading.postValue(false);
            }
        });
    }
}