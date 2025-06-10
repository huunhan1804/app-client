package com.example.dietarysupplementshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.ProductInfoActivity;
import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.model.CartItem;
import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.responses.ProductVariantDTO;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

public class CartItemAdapter  extends RecyclerView.Adapter<CartItemAdapter.CartViewHolder>{
    private final List<CartItem> productList;
    private Context context;
    private OnItemCheckedListener itemCheckedListener;

    private OnQuantityChangeListener quantityChangeListener;


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
        holder.quantityTextView.setText(String.valueOf(product.getQuantity()));
        holder.productNameTextView.setText(product.getProduct_info().getProduct_name());
        holder.productPriceTextView.setText(product.getProduct_variant_info().getSale_price());

        if(product.getProduct_variant_info().getProduct_variant_image_url() == null){
            for (String image : product.getProduct_info().getMedia_url()){
                Picasso.get()
                        .load(image)
                        .into(holder.productImageView);
            }
        } else {
            Picasso.get()
                    .load(product.getProduct_variant_info().getProduct_variant_image_url())
                    .into(holder.productImageView);
        }

        holder.productImageView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductInfoActivity.class);
            intent.putExtra("productId", product.getProduct_info().getProduct_id());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        ProductVariantDTO currentVariant = product.getProduct_variant_info();

        List<String> variantNames = product.getProduct_info().getProduct_variant_list()
                .stream()
                .map(ProductVariantDTO::getProduct_variant_name)
                .collect(Collectors.toList());

        int selectedIndex = variantNames.indexOf(currentVariant.getProduct_variant_name());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, variantNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.productVariantSpinner.setAdapter(adapter);

        if (selectedIndex != -1) {
            holder.productVariantSpinner.setSelection(selectedIndex);
        }

        holder.productVariantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int spinnerPosition, long id) {
                String selectedVariant = parentView.getItemAtPosition(spinnerPosition).toString();
                // Do something with the selected variant...
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Optional: Do something when nothing is selected
            }
        });

        holder.increaseQuantityButton.setOnClickListener(view -> {
            int currentQuantity = Integer.parseInt(holder.quantityTextView.getText().toString());
            if (currentQuantity < product.getProduct_info().getQuantity_in_stock()) {
                currentQuantity++;
                holder.quantityTextView.setText(String.valueOf(currentQuantity));
            }
            if (currentQuantity >= product.getProduct_info().getQuantity_in_stock()) {
                holder.increaseQuantityButton.setVisibility(View.INVISIBLE);
            }

            if (quantityChangeListener != null) {
                quantityChangeListener.onIncreaseChange(product, 1);
            }


        });

        holder.decreaseQuantityButton.setOnClickListener(view -> {
            int currentQuantity = Integer.parseInt(holder.quantityTextView.getText().toString());
            if (currentQuantity > 1) {
                currentQuantity--;
                holder.quantityTextView.setText(String.valueOf(currentQuantity));
            }
            if (currentQuantity < product.getProduct_info().getQuantity_in_stock()) {
                holder.increaseQuantityButton.setVisibility(View.VISIBLE);
            }
            if (quantityChangeListener != null) {
                quantityChangeListener.onDecreaseChange(product, 1);
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

        holder.btnRemoveCartItem.setOnClickListener(view -> {
            if (quantityChangeListener != null) {
                quantityChangeListener.onDelete(product);
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

    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }


    public interface OnItemCheckedListener {
        void onItemChecked(CartItem product, boolean isChecked);
    }

    public interface OnQuantityChangeListener {
        void onIncreaseChange(CartItem product, int quantity);
        void onDecreaseChange(CartItem product, int quantity);
        void onDelete(CartItem product);
    }


    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView quantityTextView;

        ImageButton increaseQuantityButton;

        ImageButton decreaseQuantityButton;

        ImageButton btnRemoveCartItem;

        CheckBox productCheckBox;

        Spinner productVariantSpinner;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            productCheckBox = itemView.findViewById(R.id.productCheckBox);
            productVariantSpinner = itemView.findViewById(R.id.productVariantSpinner);
            btnRemoveCartItem = itemView.findViewById(R.id.btnRemoveCartItem);

        }
    }
}
