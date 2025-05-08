package com.example.dietarysupplementshop;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dietarysupplementshop.requests.AddToCartRequest;
import com.example.dietarysupplementshop.responses.ProductInformation;
import com.example.dietarysupplementshop.responses.ProductVariantDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddToCartHandling {
    private Context context;
    private View popupView;
    private PopupWindow popupWindow;

    private TextView quantityTextView;

    private ProductInformation productInformation;

    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productPriceTextView;

    private int code;

    public AddToCartHandling(Context context, View popupView, int code, ProductInformation productInformation) {
        this.context = context;
        this.popupView = popupView;
        this.code = code;
        this.productInformation = productInformation;
        initUI();
    }


    private void initUI() {
        productImageView = popupView.findViewById(R.id.imageProduct);
        productNameTextView = popupView.findViewById(R.id.productNameTextView);
        productPriceTextView = popupView.findViewById(R.id.productPriceTextView);

        for (String image : productInformation.getMedia_url()){
            Picasso.get()
                    .load(image)
                    .into(productImageView);
        }

        productNameTextView.setText(productInformation.getProduct_name());
        productPriceTextView.setText(productInformation.getProduct_price());

        RadioGroup radioGroup = popupView.findViewById(R.id.radioGroupProductVariants);
        populateProductVariants(radioGroup, productInformation.getProduct_variant_list());

        Button continueButton = popupView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(view -> handleContinueButtonClick());

        ImageButton backButton = popupView.findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> closePopup());

        ImageButton increaseQuantityButton = popupView.findViewById(R.id.increaseQuantityButton);
        ImageButton decreaseQuantityButton = popupView.findViewById(R.id.decreaseQuantityButton);
        quantityTextView = popupView.findViewById(R.id.quantityTextView);

        increaseQuantityButton.setOnClickListener(view -> {
            int currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
            if (currentQuantity < productInformation.getQuantity_in_stock()) {
                currentQuantity++;
                quantityTextView.setText(String.valueOf(currentQuantity));
            }
            if (currentQuantity >= productInformation.getQuantity_in_stock()) {
                increaseQuantityButton.setVisibility(View.INVISIBLE);
            }
        });

        decreaseQuantityButton.setOnClickListener(view -> {
            int currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
            if (currentQuantity > 1) {
                currentQuantity--;
                quantityTextView.setText(String.valueOf(currentQuantity));
            }
            if (currentQuantity < productInformation.getQuantity_in_stock()) {
                increaseQuantityButton.setVisibility(View.VISIBLE);
            }
        });



    }

    private void populateProductVariants(RadioGroup radioGroup, List<ProductVariantDTO> productVariants) {
        for (ProductVariantDTO variant : productVariants) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(variant.getProduct_variant_name());
            radioButton.setTag(variant);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    updateUIForVariant(variant);
                }
            });
            radioGroup.addView(radioButton);
        }
    }

    private void updateUIForVariant(ProductVariantDTO variant) {
        // Update product image
        if(variant.getProduct_variant_image_url() != null){
            Picasso.get()
                    .load(variant.getProduct_variant_image_url())
                    .into(productImageView);
        }

        // Update product price
        productPriceTextView.setText(variant.getSale_price());

        // Update quantity buttons based on stock
        ImageButton increaseQuantityButton = popupView.findViewById(R.id.increaseQuantityButton);
        increaseQuantityButton.setOnClickListener(view -> {
            int currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
            if (currentQuantity < variant.getQuantity_in_stock()) {
                currentQuantity++;
                quantityTextView.setText(String.valueOf(currentQuantity));
            }
            if (currentQuantity >= variant.getQuantity_in_stock()) {
                increaseQuantityButton.setVisibility(View.INVISIBLE);
            }
        });
    }



    private void handleContinueButtonClick() {
        int quantity = Integer.parseInt(quantityTextView.getText().toString());

        // Get the selected variant
        RadioGroup radioGroup = popupView.findViewById(R.id.radioGroupProductVariants);
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // Check if a variant is selected
        if (selectedId == -1 && productInformation.getProduct_variant_list() != null && !productInformation.getProduct_variant_list().isEmpty()) {
            // No variant is selected, but there are available variants. Show an error message.
            Toast.makeText(context, "Please select a product variant before adding to cart.", Toast.LENGTH_LONG).show();
            return;
        }

        RadioButton selectedRadioButton = radioGroup.findViewById(selectedId);
        ProductVariantDTO selectedVariant = (ProductVariantDTO) selectedRadioButton.getTag();

        // Create the request
        AddToCartRequest addToCartRequest = new AddToCartRequest(
                productInformation.getProduct_id(),
                selectedVariant.getProduct_variant_id(),
                quantity
        );

        if (onActivityResultListener != null) {
            // You might need to adjust this part to pass relevant data back
            Intent resultIntent = new Intent();
            resultIntent.putExtra("addToCartRequest", addToCartRequest); // Assuming AddToCartRequest is Serializable or Parcelable
            onActivityResultListener.onActivityResult(code, RESULT_OK, resultIntent);
        }

        closePopup();
    }


    private void closePopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }
    private AddToCartHandling.OnActivityResultListener onActivityResultListener;

    public void setOnActivityResultListener(AddToCartHandling.OnActivityResultListener listener) {
        this.onActivityResultListener = listener;
    }
    public interface OnActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
