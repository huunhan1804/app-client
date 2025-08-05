package com.example.dietarysupplementshop.viewModel;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.example.dietarysupplementshop.repositories.AgencyProductRepository; // Sử dụng repository mới
import com.example.dietarysupplementshop.requests.AddNewProductRequest;
import com.example.dietarysupplementshop.requests.AddProductVariantsRequest; // Thêm import
import com.example.dietarysupplementshop.requests.UpdateProductRequest;
import com.example.dietarysupplementshop.responses.ProductInfoDTO;
import com.example.dietarysupplementshop.model.ProductAgency;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AgencyAddProductViewModel extends ViewModel {
    private final AgencyProductRepository productRepository; // Sử dụng repository mới
    private final FirebaseStorage storage;

    // LiveData giữ nguyên
    private final MutableLiveData<Boolean> _isAddingProduct = new MutableLiveData<>(false);
    public LiveData<Boolean> isAddingProduct = _isAddingProduct;

    private final MutableLiveData<Boolean> _productAddedSuccessfully = new MutableLiveData<>(false);
    public LiveData<Boolean> productAddedSuccessfully = _productAddedSuccessfully;

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;

    private final MutableLiveData<ProductInfoDTO> _productToEdit = new MutableLiveData<>();
    public LiveData<ProductInfoDTO> productToEdit = _productToEdit;

    public interface UploadImageCallback {
        void onSuccess(List<String> imageUrls);
        void onError(String error);
    }

    public AgencyAddProductViewModel() {
        productRepository = new AgencyProductRepository(); // Khởi tạo repository mới
        storage = FirebaseStorage.getInstance();
    }

    public void uploadImages(List<Uri> imageUris, @NonNull final UploadImageCallback callback) {
        // Giữ nguyên logic upload ảnh
    }

    public void createProduct(AddNewProductRequest request) {
        _isAddingProduct.setValue(true);
        productRepository.createProduct(request, new AgencyProductRepository.ApiCallback<ProductInfoDTO>() {
            @Override
            public void onSuccess(ProductInfoDTO result) {
                _productAddedSuccessfully.postValue(true);
                _isAddingProduct.postValue(false);
            }
            @Override
            public void onError(String errorMessage) {
                _errorMessage.postValue(errorMessage);
                _isAddingProduct.postValue(false);
            }
        });
    }

    public void updateProduct(Long productId, UpdateProductRequest request) {
        _isAddingProduct.setValue(true);
        productRepository.updateProduct(productId, request, new AgencyProductRepository.ApiCallback<ProductInfoDTO>() {
            @Override
            public void onSuccess(ProductInfoDTO result) {
                _productAddedSuccessfully.postValue(true);
                _isAddingProduct.postValue(false);
            }
            @Override
            public void onError(String errorMessage) {
                _errorMessage.postValue(errorMessage);
                _isAddingProduct.postValue(false);
            }
        });
    }

    public void loadProductForEdit(ProductInfoDTO product) {
        _productToEdit.setValue(product);
    }
}