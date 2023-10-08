package com.example.dietarysupplementshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private final List<Category> mCategoryList;

    public CategoryAdapter(List<Category> mCategoryList) {
        this.mCategoryList = mCategoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category;
        category = mCategoryList.get(position);
        if(category == null){
            return;
        }
        holder.category_image.setImageResource(category.getImage());
        holder.category_name.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        if(mCategoryList != null){
            return mCategoryList.size();
        }
        return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView category_image;
        private final TextView category_name;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            category_image = itemView.findViewById(R.id.categoryImageView);
            category_name = itemView.findViewById(R.id.categoryTextView);
        }
    }
}
