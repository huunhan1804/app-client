package com.example.dietarysupplementshop.viewModel;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.model.Category;
import com.example.dietarysupplementshop.repositories.CategoryRepository;
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
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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

import com.example.dietarysupplementshop.repositories.AgencyProductRepository;
import com.example.dietarysupplementshop.requests.AddNewProductRequest;
import com.example.dietarysupplementshop.requests.UpdateProductRequest;
import com.example.dietarysupplementshop.responses.ProductFullDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


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

import com.example.dietarysupplementshop.repositories.AgencyProductRepository;
import com.example.dietarysupplementshop.requests.AddNewProductRequest;
import com.example.dietarysupplementshop.requests.UpdateProductRequest;
import com.example.dietarysupplementshop.responses.ProductFullDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AgencyAddProductViewModel extends ViewModel {
    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final MutableLiveData<List<Category>> _categories = new MutableLiveData<>();
    public LiveData<List<Category>> categories = _categories;
    private final AgencyProductRepository productRepository;
    private final FirebaseStorage storage;
    private final MutableLiveData<Boolean> _isProcessing = new MutableLiveData<>(false);
    public LiveData<Boolean> isProcessing = _isProcessing;
    private final MutableLiveData<Boolean> _actionSuccessful = new MutableLiveData<>(false);
    public LiveData<Boolean> actionSuccessful = _actionSuccessful;
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> errorMessage = _errorMessage;
    private final MutableLiveData<ProductFullDTO> _productToEdit = new MutableLiveData<>();
    public LiveData<ProductFullDTO> productToEdit = _productToEdit;
    public interface UploadImageCallback {
        void onSuccess(List<String> imageUrls);

        void onError(String error);
    }
    public void setProcessing(boolean isProcessing) {
        if (isProcessing) {
            _isProcessing.setValue(true);
        } else {
            _isProcessing.setValue(false);
        }
    }

    public AgencyAddProductViewModel() {
        productRepository = new AgencyProductRepository();
        storage = FirebaseStorage.getInstance();
    }

    public void uploadImages(List<Uri> newImageUris, List<String> existingImageUrls, @NonNull final UploadImageCallback callback) {
        // allImageUrls sẽ chứa các URL của ảnh cũ
        final List<String> allImageUrls = new ArrayList<>(existingImageUrls);
        // Nếu không có ảnh mới, chỉ cần trả về danh sách ảnh cũ
        if (newImageUris.isEmpty()) {
            callback.onSuccess(allImageUrls);
            return;
        }

        List<Task<Uri>> uploadTasks = new ArrayList<>();
        for (Uri uri : newImageUris) {
            StorageReference imageRef = storage.getReference().child("product_images/" + UUID.randomUUID().toString());
            uploadTasks.add(imageRef.putFile(uri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return imageRef.getDownloadUrl();
            }));
        }

        Tasks.whenAllSuccess(uploadTasks)
                .addOnSuccessListener(list -> {
                    for (Object uri : list) {
                        allImageUrls.add(uri.toString());
                    }
                    callback.onSuccess(allImageUrls);
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void createProduct(AddNewProductRequest request) {
        _isProcessing.setValue(true);
        productRepository.createProduct(request, new AgencyProductRepository.ApiCallback<ProductFullDTO>() {
            @Override
            public void onSuccess(ProductFullDTO result) {
                _actionSuccessful.postValue(true);
                _isProcessing.postValue(false);
            }

            @Override
            public void onError(String errorMessage) {
                _errorMessage.postValue(errorMessage);
                _isProcessing.postValue(false);
            }
        });
    }

    public void updateProduct(Long productId, UpdateProductRequest request) {
        _isProcessing.setValue(true);
        productRepository.updateProduct(productId, request, new AgencyProductRepository.ApiCallback<ProductFullDTO>() {
            @Override
            public void onSuccess(ProductFullDTO result) {
                _actionSuccessful.postValue(true);
                _isProcessing.postValue(false);
            }

            @Override
            public void onError(String errorMessage) {
                _errorMessage.postValue(errorMessage);
                _isProcessing.postValue(false);
            }
        });
    }

    public void loadProductForEdit(Long productId) {
        _isProcessing.setValue(true);
        productRepository.getProductDetails(productId, new AgencyProductRepository.ApiCallback<ProductFullDTO>() {
            @Override
            public void onSuccess(ProductFullDTO result) {
                _productToEdit.postValue(result);
                _isProcessing.postValue(false);
            }

            @Override
            public void onError(String errorMessage) {
                _errorMessage.postValue(errorMessage);
                _isProcessing.postValue(false);
            }
        });
    }

    public void loadCategories() {
        categoryRepository.fetchCategories().observeForever(data -> {
            _categories.setValue(data);
        });
    }
}