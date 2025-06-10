package com.example.dietarysupplementshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

import com.example.dietarysupplementshop.model.Address;
import com.example.dietarysupplementshop.responses.ProductInformation;
import com.example.dietarysupplementshop.responses.ProductVariantDTO;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PopupEventHandling  extends AppCompatActivity {
    private Context context;
    private View popupView;
    private int maxQuantity = 10;

    private static final int REQUEST_CODE_ADDRESS_LIST = 1001;

    private PopupWindow popupWindow;

    private TextView quantityTextView;

    private int code;

    private ProductInformation productInformation;
    private Address address;

    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productPriceTextView;

    private TextView fullnameText, phoneText, addressText;


    public PopupEventHandling(Context context, View popupView, int code, ProductInformation productInformation, Address address) {
        this.context = context;
        this.popupView = popupView;
        this.code = code;
        this.productInformation = productInformation;
        this.address = address;
        initUI();
    }

    private void initUI() {
        fullnameText = popupView.findViewById(R.id.fullnameText);
        phoneText = popupView.findViewById(R.id.phoneText);
        addressText = popupView.findViewById(R.id.addressText);

        fullnameText.setText(address.getFullname());
        phoneText.setText(address.getPhone());
        addressText.setText(address.getAddress_detail());

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


        ImageView addressListIcon = popupView.findViewById(R.id.addressListIcon);
        addressListIcon.setOnClickListener(view -> {
            Intent intent = new Intent(context, AddressListActivity.class);
            ((Activity) context).startActivityForResult(intent, REQUEST_CODE_ADDRESS_LIST);
        });

        RelativeLayout defaultAddress = popupView.findViewById(R.id.defaultAddress);
        defaultAddress.setOnClickListener(view -> {
            Intent intent = new Intent(context, AddressListActivity.class);
            ((Activity) context).startActivityForResult(intent, REQUEST_CODE_ADDRESS_LIST);
        });


    }




    private void handleContinueButtonClick() {
        RadioGroup radioGroup = popupView.findViewById(R.id.radioGroupProductVariants);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1 && productInformation.getProduct_variant_list() != null && !productInformation.getProduct_variant_list().isEmpty()) {
            Toast.makeText(context, "Please select a product variant before adding to cart.", Toast.LENGTH_LONG).show();
            return;
        }

        RadioButton selectedRadioButton = radioGroup.findViewById(selectedId);
        ProductVariantDTO selectedVariant = (ProductVariantDTO) selectedRadioButton.getTag();

        int quantity = Integer.parseInt(quantityTextView.getText().toString());
        TextView name = popupView.findViewById(R.id.fullnameText);
        TextView phone = popupView.findViewById(R.id.phoneText);
        TextView addressDetail = popupView.findViewById(R.id.addressText);

        if (onActivityResultListener != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("productId", productInformation.getProduct_id());
            resultIntent.putExtra("productPrice", selectedVariant != null ? selectedVariant.getSale_price() : productInformation.getProduct_price());
            resultIntent.putExtra("productVariantId", selectedVariant.getProduct_variant_id());
            resultIntent.putExtra("quantity", quantity);
            resultIntent.putExtra("name", name.getText());
            resultIntent.putExtra("phone", phone.getText());
            resultIntent.putExtra("address", addressDetail.getText());
            onActivityResultListener.onActivityResult(code, Activity.RESULT_OK, resultIntent);
            closePopup();
        }

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



    private void closePopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }
    private OnActivityResultListener onActivityResultListener;

    public void setOnActivityResultListener(OnActivityResultListener listener) {
        this.onActivityResultListener = listener;
    }
    public interface OnActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }


}

