package com.example.dietarysupplementshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.model.Product;

import java.util.List;

public class FakeProductAdapter extends RecyclerView.Adapter<FakeProductAdapter.FakeProductViewHolder> {
    private final List<Product> productList;

    public FakeProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public FakeProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_fake, parent, false);
        return new FakeProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FakeProductViewHolder holder, int position) {
        // No data binding needed as this is just a placeholder
    }

    @Override
    public int getItemCount() {
        if(productList != null){
            return productList.size();
        }
        return 0;
    }

    public static class FakeProductViewHolder extends RecyclerView.ViewHolder {
        public FakeProductViewHolder(@NonNull View itemView) {
            super(itemView);
            // No views to bind as this is just a placeholder
        }
    }
}

