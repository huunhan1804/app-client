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

import java.text.DecimalFormat;

public class ConfirmPopupHandling extends AppCompatActivity {
    private Context context;
    private View popupView;

    private PopupWindow popupWindow;
    private int code;

    private String quantity;
    private String price;

    private String name;

    private String phone;

    private String address;

    private TextView quantityValueTextView;
    private TextView subTotalValueTextView;
    private TextView shippingFeeValueTextView;
    private TextView totalValueTextView;
    private TextView fullnameTextView;
    private TextView phoneTextView;
    private TextView addressTextView;
    private Button confirmButton;


    public ConfirmPopupHandling(Context context, View popupView, int code, String quantity, String price, String name, String phone, String address) {
        this.context = context;
        this.popupView = popupView;
        this.code = code;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.phone = phone;
        this.address = address;
        initUI();
        updateUI();
    }


    private void initUI() {
        ImageButton backButton = popupView.findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> closePopup());

        quantityValueTextView = popupView.findViewById(R.id.quantityValue);
        subTotalValueTextView = popupView.findViewById(R.id.subTotalValue);
        shippingFeeValueTextView = popupView.findViewById(R.id.shippingFeeValue);
        totalValueTextView = popupView.findViewById(R.id.totalValue);
        fullnameTextView = popupView.findViewById(R.id.fullnameText);
        phoneTextView = popupView.findViewById(R.id.phoneText);
        addressTextView = popupView.findViewById(R.id.addressText);
        confirmButton = popupView.findViewById(R.id.confirmButton);


        Button confirmButton = popupView.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(view -> {
            sendProductData();
            closePopup();
        });


    }

    private void updateUI() {
        // Xử lý chuỗi quantity và price để bỏ đi ký tự không phải số
        String quantityCleaned = quantity.replaceAll("[^\\d]+", "");
        String priceCleaned = price.replaceAll("[^\\d]+", "");

        // Chuyển đổi chuỗi đã làm sạch thành kiểu số
        int quantityValue = Integer.parseInt(quantityCleaned);
        double priceValue = Double.parseDouble(priceCleaned);

        // Tính giá trị subtotal và phí vận chuyển
        double subtotal = quantityValue * priceValue;
        double shippingFee = 20000.0; // Số tiền phí vận chuyển

        // Tính tổng giá trị và định dạng lại để hiển thị
        double total = subtotal + shippingFee;

        // Định dạng lại giá trị và hiển thị lên TextView
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");

        quantityValueTextView.setText(decimalFormat.format(quantityValue));
        subTotalValueTextView.setText(decimalFormat.format(subtotal) + " đ");
        shippingFeeValueTextView.setText(decimalFormat.format(shippingFee) + " đ");
        totalValueTextView.setText(decimalFormat.format(total) + " đ");

        fullnameTextView.setText(name);
        phoneTextView.setText(phone);
        addressTextView.setText(address);
    }

    private void sendProductData() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("subTotal", subTotalValueTextView.getText().toString());
        resultIntent.putExtra("totalBill", totalValueTextView.getText().toString());
        String shippingInfo  = "Name: " + name + "\n" +
                "Phone: " + phone + "\n" +
                "Address: " + address;
        resultIntent.putExtra("address", shippingInfo);

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

    private ConfirmPopupHandling.OnActivityResultListener onActivityResultListener;

    public void setOnActivityResultListener(ConfirmPopupHandling.OnActivityResultListener listener) {
        this.onActivityResultListener = listener;
    }
    public interface OnActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

}
