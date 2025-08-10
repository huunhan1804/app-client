// File: com/example/dietarysupplementshop/viewModel/CategoryViewModel.java
package com.example.dietarysupplementshop.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dietarysupplementshop.model.Category;
import com.example.dietarysupplementshop.repositories.CategoryRepository;
import java.util.List;

public class CategoryViewModel extends ViewModel {
    private final CategoryRepository repository;
    private final MutableLiveData<List<Category>> _categories = new MutableLiveData<>();
    public LiveData<List<Category>> categories = _categories;

    public CategoryViewModel() {
        repository = new CategoryRepository();
    }

    public void loadCategories() {
        repository.fetchCategories(new CategoryRepository.CategoryCallback() {
            @Override
            public void onSuccess(List<Category> categoryList) {
                _categories.setValue(categoryList);
            }

            @Override
            public void onError(Throwable t) {
                _categories.setValue(null);
            }
        });
    }
}
