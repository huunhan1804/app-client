package com.example.dietarysupplementshop.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.model.Category;
import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.repositories.CategoryRepository;
import com.example.dietarysupplementshop.repositories.ProductRepository;

import java.util.List;

public class ProductViewModel extends ViewModel {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public ProductViewModel() {
        this.productRepository = new ProductRepository();
        this.categoryRepository = new CategoryRepository();
    }

    public LiveData<List<Product>> getBestSellers() {
        return productRepository.fetchBestSellers();
    }

    public LiveData<List<Product>> getBestOrders() {
        return productRepository.fetchBestOrders();
    }

    public LiveData<List<Category>> getCategories() {
        return categoryRepository.fetchCategories();
    }
}
