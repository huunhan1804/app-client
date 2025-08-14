package com.example.dietarysupplementshop.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.repositories.AgencyProductRepository;
import com.example.dietarysupplementshop.responses.ProductFullDTO;
import com.example.dietarysupplementshop.responses.ProductInfoDTO;

import java.util.List;

public class AgencyProductViewModel extends ViewModel {
    private final AgencyProductRepository productRepository;

    private final MutableLiveData<List<ProductInfoDTO>> _approvedProducts = new MutableLiveData<>();
    public LiveData<List<ProductInfoDTO>> approvedProducts = _approvedProducts;

    private final MutableLiveData<List<ProductInfoDTO>> _pendingProducts = new MutableLiveData<>();
    public LiveData<List<ProductInfoDTO>> pendingProducts = _pendingProducts;

    private final MutableLiveData<List<ProductInfoDTO>> _rejectedProducts = new MutableLiveData<>();
    public LiveData<List<ProductInfoDTO>> rejectedProducts = _rejectedProducts;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    public AgencyProductViewModel() {
        productRepository = new AgencyProductRepository();
    }

    public void loadProductsByStatus(String statusCode) {
        _isLoading.setValue(true);
        productRepository.getAgencyProducts(statusCode, new AgencyProductRepository.ApiCallback<List<ProductInfoDTO>>() {
            @Override
            public void onSuccess(List<ProductInfoDTO> result) {
                if ("APPROVED".equals(statusCode)) {
                    _approvedProducts.postValue(result);
                } else if ("PENDING".equals(statusCode)) {
                    _pendingProducts.postValue(result);
                } else if ("REJECTED".equals(statusCode)) {
                    _rejectedProducts.postValue(result);
                }
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
        productRepository.deleteProduct(productId, new AgencyProductRepository.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                _isLoading.postValue(false);
                loadAllTabs();
            }

            @Override
            public void onError(String errorMessage) {
                _errorMessage.postValue(errorMessage);
                _isLoading.postValue(false);
            }
        });
    }

    public void loadAllTabs() {
        loadProductsByStatus("APPROVED");
        loadProductsByStatus("PENDING");
        loadProductsByStatus("REJECTED");
    }
}