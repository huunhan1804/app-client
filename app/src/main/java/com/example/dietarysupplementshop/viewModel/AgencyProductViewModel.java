package com.example.dietarysupplementshop.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.repositories.AgencyProductRepository;
import com.example.dietarysupplementshop.responses.ProductInfoDTO;

import java.util.List;

public class AgencyProductViewModel extends ViewModel {
    private final AgencyProductRepository productRepository;
    private final MutableLiveData<List<ProductInfoDTO>> _allAgencyProducts = new MutableLiveData<>();
    public LiveData<List<ProductInfoDTO>> allAgencyProducts = _allAgencyProducts;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    public AgencyProductViewModel() {
        productRepository = new AgencyProductRepository();
    }

    public void loadAllAgencyProducts() {
        _isLoading.setValue(true);
        productRepository.getAgencyProducts(null, new AgencyProductRepository.ApiCallback<List<ProductInfoDTO>>() {
            @Override
            public void onSuccess(List<ProductInfoDTO> result) {
                _allAgencyProducts.postValue(result);
                _isLoading.postValue(false);
            }
            @Override
            public void onError(String errorMessage) {
                _errorMessage.postValue(errorMessage);
                _isLoading.postValue(false);
            }
        });
    }

    public void updateProductStatus(long productId, String newStatus) {
        _isLoading.setValue(true);
        productRepository.updateProductStatus(productId, newStatus,
                new AgencyProductRepository.ApiCallback<ProductInfoDTO>() {
                    @Override
                    public void onSuccess(ProductInfoDTO result) {
                        loadAllAgencyProducts();
                        _isLoading.postValue(false);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        _errorMessage.postValue(errorMessage);
                        _isLoading.postValue(false);
                    }
                });
    }
    public void deleteProduct(long productId) {
        _isLoading.setValue(true);
        productRepository.deleteProduct(productId, new AgencyProductRepository.ApiCallback<ProductInfoDTO>() {
            @Override
            public void onSuccess(ProductInfoDTO result) {
                loadAllAgencyProducts();
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