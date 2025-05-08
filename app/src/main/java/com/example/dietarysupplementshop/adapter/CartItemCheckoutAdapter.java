package com.example.dietarysupplementshop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.AddressListActivity;
import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.responses.ProductInformation;
import com.example.dietarysupplementshop.responses.ProductVariantDTO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartItemCheckoutAdapter extends RecyclerView.Adapter<CartItemCheckoutAdapter.CartItemCheckoutViewHolder> {

    private List<ProductInformation> productList;
    private Context context;

    public CartItemCheckoutAdapter(List<ProductInformation> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartItemCheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_checkout, parent, false);
        return new CartItemCheckoutViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemCheckoutViewHolder holder, int position) {
        ProductInformation productInformation = productList.get(position);
        Picasso.get().load(productInformation.getMedia_url().get(0)).into(holder.productImageView);

        holder.productNameTextView.setText(productInformation.getProduct_name());
        holder.productPriceTextView.setText(productInformation.getProduct_price());

        holder.increaseQuantityButton.setOnClickListener(view -> {
            int currentQuantity = Integer.parseInt(holder.quantityTextView.getText().toString());
            if (currentQuantity < productInformation.getQuantity_in_stock()) {
                currentQuantity++;
                holder.quantityTextView.setText(String.valueOf(currentQuantity));
            }
            if (currentQuantity >= productInformation.getQuantity_in_stock()) {
                holder.increaseQuantityButton.setVisibility(View.INVISIBLE);
            }
        });

        holder.decreaseQuantityButton.setOnClickListener(view -> {
            int currentQuantity = Integer.parseInt(holder.quantityTextView.getText().toString());
            if (currentQuantity > 1) {
                currentQuantity--;
                holder.quantityTextView.setText(String.valueOf(currentQuantity));
            }
            if (currentQuantity < productInformation.getQuantity_in_stock()) {
                holder.increaseQuantityButton.setVisibility(View.VISIBLE);
            }
        });

        RadioGroup radioGroup = holder.itemView.findViewById(R.id.radioGroupProductVariants);
        radioGroup.removeAllViews(); // Clear existing views if any
        for (ProductVariantDTO variant : productInformation.getProduct_variant_list()) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(variant.getProduct_variant_name());
            radioButton.setTag(variant);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Update product image
                    if(variant.getProduct_variant_image_url() != null){
                        Picasso.get().load(variant.getProduct_variant_image_url()).into(holder.productImageView);
                    }
                    // Update product price
                    holder.productPriceTextView.setText(variant.getSale_price());

                    holder.increaseQuantityButton.setOnClickListener(view -> {
                        int currentQuantity = Integer.parseInt(holder.quantityTextView.getText().toString());
                        if (currentQuantity < variant.getQuantity_in_stock()) {
                            currentQuantity++;
                            holder.quantityTextView.setText(String.valueOf(currentQuantity));
                        }
                        if (currentQuantity >= variant.getQuantity_in_stock()) {
                            holder.increaseQuantityButton.setVisibility(View.INVISIBLE);
                        }
                    });

                    productInformation.setProduct_variant_list(new ArrayList<>());
                    productInformation.getProduct_variant_list().add(variant);

                    notifyItemChanged(position);
                }
            });
            radioGroup.addView(radioButton);
        }

    }
    public void updateProductAtPosition(ProductInformation product, int position) {
        productList.set(position, product);
        notifyItemChanged(position);
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class CartItemCheckoutViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        ImageButton increaseQuantityButton, decreaseQuantityButton;
        TextView quantityTextView;

        public CartItemCheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.imageProduct);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
        }
    }
}
