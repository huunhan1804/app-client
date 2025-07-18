package com.example.dietarysupplementshop.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.model.ProductSeller;
import com.example.dietarysupplementshop.repositories.ProductRepository;
import com.example.dietarysupplementshop.requests.AddProductRequest;

public class SellerAddProductViewModel extends ViewModel {
    private final ProductRepository productRepository;

    private final MutableLiveData<Boolean> _isAddingProduct = new MutableLiveData<>(false);
    public LiveData<Boolean> isAddingProduct = _isAddingProduct;

    private final MutableLiveData<Boolean> _productAddedSuccessfully = new MutableLiveData<>(false);
    public LiveData<Boolean> productAddedSuccessfully = _productAddedSuccessfully;

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    private final MutableLiveData<ProductSeller> _productToEdit = new MutableLiveData<>();
    public LiveData<ProductSeller> productToEdit = _productToEdit;

    public SellerAddProductViewModel() {
        productRepository = new ProductRepository();
    }

    public void addProduct(AddProductRequest request) {
        _isAddingProduct.setValue(true);
        productRepository.addProduct(request, new ProductRepository.ProductSellerCallback<ProductSeller>() {
            @Override
            public void onSuccess(ProductSeller result) {
                _productAddedSuccessfully.postValue(true);
                _isAddingProduct.postValue(false);
            }

            @Override
            public void onError(Throwable t) {
                _errorMessage.postValue("Lỗi thêm sản phẩm: " + t.getMessage());
                _isAddingProduct.postValue(false);
            }

            @Override
            public void onError(String errorMessage) {
                _errorMessage.postValue(errorMessage);
                _isAddingProduct.postValue(false);
            }
        });
    }

    public void updateProduct(String productId, AddProductRequest request) {
        _isAddingProduct.setValue(true); // Reusing this for loading state
        // Thay thế cuộc gọi ProductApiService bằng ProductRepository
        productRepository.updateProduct(productId, request, new ProductRepository.ProductSellerCallback<ProductSeller>() {
            @Override
            public void onSuccess(ProductSeller result) {
                _productAddedSuccessfully.postValue(true); // Indicate success
                _isAddingProduct.postValue(false);
            }

            @Override
            public void onError(Throwable t) {
                _errorMessage.postValue("Lỗi cập nhật sản phẩm: " + t.getMessage());
                _isAddingProduct.postValue(false);
            }

            @Override
            public void onError(String errorMessage) {
                _errorMessage.postValue(errorMessage);
                _isAddingProduct.postValue(false);
            }
        });
    }

    public void loadProductForEdit(ProductSeller product) {
        _productToEdit.setValue(product);
    }
}