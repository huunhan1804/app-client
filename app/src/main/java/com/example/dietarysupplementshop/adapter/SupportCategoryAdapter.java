package com.example.dietarysupplementshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.model.SupportCategory;

import java.util.List;

public class SupportCategoryAdapter extends RecyclerView.Adapter<SupportCategoryAdapter.ViewHolder> {
    private final Context context;
    private final List<SupportCategory> categoryList;
    private final OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Long categoryId);
    }

    public SupportCategoryAdapter(Context context, List<SupportCategory> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_support_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SupportCategory category = categoryList.get(position);
        holder.categoryName.setText(category.getSupportCategoryName());
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category.getSupportCategoryId()));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }
}
