package com.example.dietarysupplementshop.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.model.Category;
import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.repositories.CategoryRepository;
import com.example.dietarysupplementshop.repositories.ProductRepository;
import com.example.dietarysupplementshop.repositories.Resource;
import com.example.dietarysupplementshop.requests.SearchRequest;
import com.example.dietarysupplementshop.responses.ProductInformation;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private MutableLiveData<ProductInformation> productInfo;

    private MutableLiveData<List<Product>> bestSellers;
    private MutableLiveData<List<Product>> bestOrders;

    private MutableLiveData<List<Product>> relatedProduct;
    private MutableLiveData<List<Category>> categories;

    public ProductViewModel() {
        this.productRepository = new ProductRepository();
        this.categoryRepository = new CategoryRepository();
    }

    public LiveData<List<Product>> getBestSellers() {
        if (bestSellers == null) {
            bestSellers = new MutableLiveData<>();
            loadBestSellers();
        }
        return bestSellers;
    }

    private void loadBestSellers() {
        productRepository.fetchBestSellers().observeForever(data -> {
            bestSellers.setValue(data);
        });
    }

    public LiveData<List<Product>> getBestOrders() {
        if (bestOrders == null) {
            bestOrders = new MutableLiveData<>();
            loadBestOrders();
        }
        return bestOrders;
    }

    private void loadBestOrders() {
        productRepository.fetchBestOrders().observeForever(data -> {
            bestOrders.setValue(data);
        });
    }

    public LiveData<List<Category>> getCategories() {
        if (categories == null) {
            categories = new MutableLiveData<>();
            loadCategories();
        }
        return categories;
    }

    private void loadCategories() {
        categoryRepository.fetchCategories().observeForever(data -> {
            categories.setValue(data);
        });
    }

    public List<Product> createFakeProducts(int count) {
        List<Product> fakeList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            fakeList.add(new Product());
        }
        return fakeList;
    }

    public LiveData<ProductInformation> getProductInformation(long productId) {
        if (productInfo == null) {
            productInfo = new MutableLiveData<>();
            loadProductInformation(productId);
        }
        return productInfo;
    }

    private void loadProductInformation(long productId) {
        productRepository.getProductInfo(productId).observeForever(info -> {
            productInfo.setValue(info);
        });
    }

    public LiveData<List<Product>> getListRelatedProduct(long productId){
        if (relatedProduct == null){
            relatedProduct = new MutableLiveData<>();
            loadRelatedProduct(productId);
        }
        return relatedProduct;
    }

    private void loadRelatedProduct(long productId) {
        productRepository.fetchRelated(productId).observeForever(data -> {
            relatedProduct.setValue(data);
        });
    }

    public LiveData<Resource<List<Product>>> getListSearchProduct(String searchKey){
        SearchRequest request = new SearchRequest(searchKey);
       return productRepository.fetchProductResult(request);
    }

    public LiveData<Resource<List<Product>>> getProductByCategory(Long categoryId){
       return productRepository.getProductByCategory(categoryId);
    }

}

