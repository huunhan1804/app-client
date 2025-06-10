package com.example.dietarysupplementshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.ProductInfoActivity;
import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.SearchResultActivity;
import com.example.dietarysupplementshop.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private final List<Category> mCategoryList;
    private Context context;

    public CategoryAdapter(List<Category> mCategoryList, Context context) {
        this.mCategoryList = mCategoryList;
        this.context = context;
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
        holder.category_name.setText(category.getCategory_name());
        Picasso.get()
                .load(category.getCategory_url())
                .into(holder.category_image);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, SearchResultActivity.class);
            intent.putExtra("categoryId",category.getCategory_id());
            intent.putExtra("categoryName",category.getCategory_name());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

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
