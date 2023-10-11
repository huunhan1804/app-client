package com.example.dietarysupplementshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddToCartHandling extends AppCompatActivity {
    private Context context;
    private View popupView;
    private int maxQuantity = 10;

    private PopupWindow popupWindow;

    private TextView quantityTextView;

    private int code;

    public AddToCartHandling(Context context, View popupView, int code) {
        this.context = context;
        this.popupView = popupView;
        this.code = code;
        initUI();
    }

    private void initUI() {
        Button continueButton = popupView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý khi người dùng nhấn nút "Continue" trong pop-up
                handleContinueButtonClick();
            }
        });

        ImageButton backButton = popupView.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closePopup();
            }
        });

        ImageButton increaseQuantityButton = popupView.findViewById(R.id.increaseQuantityButton);
        ImageButton decreaseQuantityButton = popupView.findViewById(R.id.decreaseQuantityButton);
        quantityTextView = popupView.findViewById(R.id.quantityTextView);

        increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
                if (currentQuantity < maxQuantity) {
                    currentQuantity++;
                    quantityTextView.setText(String.valueOf(currentQuantity));
                }
                if (currentQuantity >= maxQuantity) {
                    increaseQuantityButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
                if (currentQuantity > 1) {
                    currentQuantity--;
                    quantityTextView.setText(String.valueOf(currentQuantity));
                }
                if (currentQuantity < maxQuantity) {
                    increaseQuantityButton.setVisibility(View.VISIBLE);
                }
            }
        });



    }

    private void handleContinueButtonClick() {

        int quantity = Integer.parseInt(quantityTextView.getText().toString());

        Intent resultIntent = new Intent();
        resultIntent.putExtra("productId", quantity);
        resultIntent.putExtra("quantity", quantity);
        ((Activity) context).setResult(RESULT_OK, resultIntent);

        if (onActivityResultListener != null) {
            onActivityResultListener.onActivityResult(code, Activity.RESULT_OK, resultIntent);
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
