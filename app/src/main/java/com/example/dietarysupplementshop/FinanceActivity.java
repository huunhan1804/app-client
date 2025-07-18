package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast; // Dùng để hiển thị thông báo tạm thời

import androidx.appcompat.app.AppCompatActivity;

public class FinanceActivity extends AppCompatActivity {

    private ImageView backArrow;
    private TextView toolbarTitle;
    private TextView saveButton;
    private TextView tvAccountBalance;
    private Button btnWithdraw;
    private TextView tvViewAllTransactions;
    private LinearLayout llPaymentMethods;
    private LinearLayout llTaxInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_finance);

        backArrow = findViewById(R.id.back_arrow);
        toolbarTitle = findViewById(R.id.toolbar_title);
        saveButton = findViewById(R.id.save_button);
        tvAccountBalance = findViewById(R.id.tv_account_balance);
        btnWithdraw = findViewById(R.id.btn_withdraw);
        tvViewAllTransactions = findViewById(R.id.tv_view_all_transactions);
        llPaymentMethods = findViewById(R.id.ll_payment_methods);
        llTaxInformation = findViewById(R.id.ll_tax_information);


        toolbarTitle.setText("Tài Chính");

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FinanceActivity.this, "Lưu thông tin tài chính", Toast.LENGTH_SHORT).show();
            }
        });

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FinanceActivity.this, "Chuyển đến trang rút tiền", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(FinanceActivity.this, WithdrawActivity.class);
                // startActivity(intent);
            }
        });

        tvViewAllTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FinanceActivity.this, "Chuyển đến lịch sử giao dịch chi tiết", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(FinanceActivity.this, TransactionHistoryActivity.class);
                // startActivity(intent);
            }
        });

        llPaymentMethods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FinanceActivity.this, "Chuyển đến cài đặt phương thức thanh toán", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(FinanceActivity.this, PaymentMethodsActivity.class);
                // startActivity(intent);
            }
        });

        llTaxInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FinanceActivity.this, "Chuyển đến trang thông tin thuế", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(FinanceActivity.this, TaxInformationActivity.class);
                // startActivity(intent);
            }
        });


        // tvAccountBalance.setText("5.250.000 đ");
    }
}