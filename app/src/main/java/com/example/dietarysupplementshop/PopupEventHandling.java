package com.example.dietarysupplementshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class PopupEventHandling  extends AppCompatActivity {
    private Context context;
    private View popupView;
    private int maxQuantity = 10;

    private static final int REQUEST_CODE_ADDRESS_LIST = 1001;

    private PopupWindow popupWindow;

    private TextView quantityTextView;

    private int code;


    public PopupEventHandling(Context context, View popupView, int code) {
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

        // Xử lý sự kiện khi người dùng nhấn vào nút tăng số lượng
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


        ImageView addressListIcon = popupView.findViewById(R.id.addressListIcon);
        addressListIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddressListActivity.class);
                ((Activity) context).startActivityForResult(intent, REQUEST_CODE_ADDRESS_LIST);
            }
        });

        RelativeLayout defaultAddress = popupView.findViewById(R.id.defaultAddress);
        defaultAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddressListActivity.class);
                ((Activity) context).startActivityForResult(intent, REQUEST_CODE_ADDRESS_LIST);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADDRESS_LIST && resultCode == RESULT_OK) {
            if (data != null) {
                String selectedAddress = data.getStringExtra("selectedAddress");

            }
        }
    }


    private void handleContinueButtonClick() {

        int quantity = Integer.parseInt(quantityTextView.getText().toString());
        TextView name = popupView.findViewById(R.id.fullnameText);
        TextView phone = popupView.findViewById(R.id.phoneText);
        TextView addressDetail = popupView.findViewById(R.id.addressText);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("productId", quantity);
        resultIntent.putExtra("quantity", quantity);
        resultIntent.putExtra("name", name.getText());
        resultIntent.putExtra("phone", phone.getText());
        resultIntent.putExtra("address", addressDetail.getText());
        ((Activity) context).setResult(RESULT_OK, resultIntent);

        if (onActivityResultListener != null) {
            onActivityResultListener.onActivityResult(code, Activity.RESULT_OK, resultIntent);
        }

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

