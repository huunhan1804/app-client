package com.example.dietarysupplementshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.ProductInfoActivity;
import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.model.CartItem;
import com.example.dietarysupplementshop.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartItemAdapter  extends RecyclerView.Adapter<CartItemAdapter.CartViewHolder>{
    private final List<CartItem> productList;
    private Context context;

    private int maxQuantity = 10;

    private OnItemCheckedListener itemCheckedListener;

    public CartItemAdapter(List<CartItem> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_item, parent, false);
        return new CartItemAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem product = productList.get(position);

        holder.productNameTextView.setText(product.getProductName());
        holder.productPriceTextView.setText(product.getProductPrice());

        Picasso.get()
                .load(product.getImageUrl())
                .into(holder.productImageView);


        holder.productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductInfoActivity.class);
                intent.putExtra("productId", product.getProductId());
                context.startActivity(intent);
            }
        });

        holder.increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(holder.quantityTextView.getText().toString());
                if (currentQuantity < maxQuantity) {
                    currentQuantity++;
                    holder.quantityTextView.setText(String.valueOf(currentQuantity));
                }
                if (currentQuantity >= maxQuantity) {
                    holder.increaseQuantityButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        holder.decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(holder.quantityTextView.getText().toString());
                if (currentQuantity > 1) {
                    currentQuantity--;
                    holder.quantityTextView.setText(String.valueOf(currentQuantity));
                }
                if (currentQuantity < maxQuantity) {
                    holder.increaseQuantityButton.setVisibility(View.VISIBLE);
                }
            }
        });



        // Set the checkbox state based on the product's selection status
        holder.productCheckBox.setChecked(product.isSelected());

        // Handle checkbox clicks
        holder.productCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update the product's selection status
            product.setSelected(isChecked);

            // Notify the listener
            if (itemCheckedListener != null) {
                itemCheckedListener.onItemChecked(product, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(productList != null){
            return productList.size();
        }
        return 0;
    }

    public void setOnItemCheckedListener(OnItemCheckedListener listener) {
        this.itemCheckedListener = listener;
    }

    public interface OnItemCheckedListener {
        void onItemChecked(CartItem product, boolean isChecked);
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView quantityTextView;

        ImageButton increaseQuantityButton;

        ImageButton decreaseQuantityButton;

        CheckBox productCheckBox;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            productCheckBox = itemView.findViewById(R.id.productCheckBox);

        }
    }
}
