package com.example.dietarysupplementshop;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.dietarysupplementshop.requests.CreateVoucherRequest;
import com.example.dietarysupplementshop.viewModel.SellerVoucherViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SellerCreateVoucherActivity extends AppCompatActivity {

    private TextInputLayout tilVoucherName, tilVoucherCode, tilVoucherQuantity, tilMinOrderValue;
    private TextInputLayout tilStartDate, tilEndDate;
    private TextInputEditText etStartDate, etEndDate;
    private RadioGroup rgVoucherType, rgDiscountType;
    private LinearLayout llShippingDiscountFields, llProductDiscountFields;
    private TextInputLayout tilShippingDiscountValue, tilDiscountValue, tilMaxDiscountAmount;
    private Spinner spinnerShippingMethods;
    private Button btnSelectProducts, btnSaveVoucher;

    private Calendar startDateCalendar = Calendar.getInstance();
    private Calendar endDateCalendar = Calendar.getInstance();

    private SellerVoucherViewModel sellerVoucherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_create_voucher);

        sellerVoucherViewModel = new ViewModelProvider(this).get(SellerVoucherViewModel.class);

        tilVoucherName = findViewById(R.id.til_voucher_name);
        tilVoucherCode = findViewById(R.id.til_voucher_code);
        tilVoucherQuantity = findViewById(R.id.til_voucher_quantity);
        tilMinOrderValue = findViewById(R.id.til_min_order_value);

        tilStartDate = findViewById(R.id.til_start_date);
        tilEndDate = findViewById(R.id.til_end_date);

        etStartDate = (TextInputEditText) tilStartDate.getEditText();
        etEndDate = (TextInputEditText) tilEndDate.getEditText();

        rgVoucherType = findViewById(R.id.rg_voucher_type);
        llShippingDiscountFields = findViewById(R.id.ll_shipping_discount_fields);
        llProductDiscountFields = findViewById(R.id.ll_product_discount_fields);

        tilShippingDiscountValue = findViewById(R.id.til_shipping_discount_value);
        spinnerShippingMethods = findViewById(R.id.spinner_shipping_methods);

        rgDiscountType = findViewById(R.id.rg_discount_type);
        tilDiscountValue = findViewById(R.id.til_discount_value);
        tilMaxDiscountAmount = findViewById(R.id.til_max_discount_amount);
        btnSelectProducts = findViewById(R.id.btn_select_products);
        btnSaveVoucher = findViewById(R.id.btn_save_voucher);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shipping_methods_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShippingMethods.setAdapter(adapter);

        if (etStartDate != null) {
            etStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(etStartDate, startDateCalendar);
                }
            });
        }

        if (etEndDate != null) {
            etEndDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(etEndDate, endDateCalendar);
                }
            });
        }

        rgVoucherType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_shipping_discount) {
                    llShippingDiscountFields.setVisibility(View.VISIBLE);
                    llProductDiscountFields.setVisibility(View.GONE);
                } else if (checkedId == R.id.rb_product_discount) {
                    llShippingDiscountFields.setVisibility(View.GONE);
                    llProductDiscountFields.setVisibility(View.VISIBLE);
                }
            }
        });

        rgDiscountType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_percentage_discount) {
                    tilDiscountValue.setHint("Giá trị giảm giá (%)");
                    if (tilDiscountValue.getEditText() != null) {
                        tilDiscountValue.getEditText().setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    }
                    tilMaxDiscountAmount.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.rb_fixed_amount_discount) {
                    tilDiscountValue.setHint("Giá trị giảm giá (đ)");
                    if (tilDiscountValue.getEditText() != null) {
                        tilDiscountValue.getEditText().setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    }
                    tilMaxDiscountAmount.setVisibility(View.GONE);
                }
            }
        });

        if (rgDiscountType.getCheckedRadioButtonId() == R.id.rb_percentage_discount) {
            tilDiscountValue.setHint("Giá trị giảm giá (%)");
            tilMaxDiscountAmount.setVisibility(View.VISIBLE);
        } else {
            tilDiscountValue.setHint("Giá trị giảm giá (đ)");
            tilMaxDiscountAmount.setVisibility(View.GONE);
        }

        btnSelectProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SellerCreateVoucherActivity.this, "Mở màn hình chọn sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });

        btnSaveVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVoucher();
            }
        });

        sellerVoucherViewModel.createCoupon(null).observe(this, resource -> { // Pass null or a dummy request
            if (resource == null) return;
            switch (resource.getStatus()) {
                case LOADING:
                    Toast.makeText(this, "Đang tạo voucher...", Toast.LENGTH_SHORT).show();
                    btnSaveVoucher.setEnabled(false);
                    break;
                case SUCCESS:
                    Toast.makeText(this, resource.getData().getMessage(), Toast.LENGTH_LONG).show();
                    btnSaveVoucher.setEnabled(true);
                    finish();
                    break;
                case ERROR:
                    Toast.makeText(this, "Lỗi tạo voucher: " + resource.getMessage(), Toast.LENGTH_LONG).show();
                    btnSaveVoucher.setEnabled(true);
                    break;
            }
        });
    }

    private void showDatePickerDialog(final TextInputEditText targetEditText, final Calendar calendar) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateInView(targetEditText, calendar);
            }
        };

        new DatePickerDialog(SellerCreateVoucherActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateInView(TextInputEditText targetEditText, Calendar calendar) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        targetEditText.setText(sdf.format(calendar.getTime()));
    }

    private void saveVoucher() {
        String voucherName = tilVoucherName.getEditText().getText().toString().trim();
        String voucherCode = tilVoucherCode.getEditText().getText().toString().trim();
        String startDateStr = etStartDate.getText().toString().trim();
        String endDateStr = etEndDate.getText().toString().trim();
        String voucherQuantityStr = tilVoucherQuantity.getEditText().getText().toString().trim();
        String minOrderValueStr = tilMinOrderValue.getEditText().getText().toString().trim();

        if (voucherName.isEmpty() || startDateStr.isEmpty() || endDateStr.isEmpty() || voucherQuantityStr.isEmpty() || minOrderValueStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ các thông tin bắt buộc.", Toast.LENGTH_LONG).show();
            return;
        }

        int voucherQuantity = 0;
        try {
            voucherQuantity = Integer.parseInt(voucherQuantityStr);
            if (voucherQuantity <= 0) {
                Toast.makeText(this, "Số lượng Voucher phải lớn hơn 0.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số lượng Voucher không hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal minOrderValue = BigDecimal.ZERO;
        try {
            minOrderValue = new BigDecimal(minOrderValueStr);
            if (minOrderValue.compareTo(BigDecimal.ZERO) < 0) {
                Toast.makeText(this, "Giá trị đơn hàng tối thiểu không được âm.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá trị đơn hàng tối thiểu không hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }

        Date startDate, endDate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            startDate = sdf.parse(startDateStr);
            endDate = sdf.parse(endDateStr);
            if (startDate == null || endDate == null) throw new ParseException("Date parsing failed", 0);
            if (endDate.before(startDate)) {
                Toast.makeText(this, "Ngày kết thúc phải sau ngày bắt đầu.", Toast.LENGTH_LONG).show();
                return;
            }
            if (startDate.before(new Date()) && !startDateStr.equals(sdf.format(new Date()))) { // Not allowing past dates unless it's "today"
                Toast.makeText(this, "Ngày bắt đầu không được trong quá khứ.", Toast.LENGTH_LONG).show();
                return;
            }

        } catch (ParseException e) {
            Toast.makeText(this, "Định dạng ngày không hợp lệ. Vui lòng sử dụng DD/MM/YYYY.", Toast.LENGTH_LONG).show();
            return;
        }


        CreateVoucherRequest request = new CreateVoucherRequest();
        request.setCouponCode(voucherCode);
        request.setDiscountValue(BigDecimal.ZERO);
        request.setExpiryDate(endDate);
        request.setActivated(true);
        request.setRemainingQuantity(voucherQuantity);
        request.setMinPurchaseAmount(minOrderValue);
        request.setDescription(voucherName);
        request.setMinQuantity(0);
        request.setMaxQuantity(0);

        int selectedVoucherTypeId = rgVoucherType.getCheckedRadioButtonId();

        if (selectedVoucherTypeId == R.id.rb_shipping_discount) {
            request.setCouponType("SHIPPING");
            String shippingDiscountValueStr = tilShippingDiscountValue.getEditText().getText().toString().trim();
            String selectedShippingMethod = spinnerShippingMethods.getSelectedItem().toString();

            if (shippingDiscountValueStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập giá trị giảm vận chuyển.", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                BigDecimal shippingDiscountValue = new BigDecimal(shippingDiscountValueStr);
                if (shippingDiscountValue.compareTo(BigDecimal.ZERO) < 0) {
                    Toast.makeText(this, "Giá trị giảm vận chuyển không được âm.", Toast.LENGTH_SHORT).show();
                    return;
                }
                request.setDiscountValue(shippingDiscountValue);
                request.setDiscountType("AMOUNT"); // Shipping is usually fixed amount discount
                request.setShippingMethod(selectedShippingMethod);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá trị giảm vận chuyển không hợp lệ.", Toast.LENGTH_SHORT).show();
                return;
            }

        } else if (selectedVoucherTypeId == R.id.rb_product_discount) {
            request.setCouponType("DISCOUNT");
            String discountValueStr = tilDiscountValue.getEditText().getText().toString().trim();
            String maxDiscountAmountStr = tilMaxDiscountAmount.getEditText().getText().toString().trim();

            if (discountValueStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập giá trị giảm giá sản phẩm.", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedDiscountTypeId = rgDiscountType.getCheckedRadioButtonId();
            try {
                BigDecimal discountValue = new BigDecimal(discountValueStr);
                if (discountValue.compareTo(BigDecimal.ZERO) < 0) {
                    Toast.makeText(this, "Giá trị giảm giá không được âm.", Toast.LENGTH_SHORT).show();
                    return;
                }
                request.setDiscountValue(discountValue);

                if (selectedDiscountTypeId == R.id.rb_percentage_discount) {
                    request.setDiscountType("PERCENTAGE");
                    if (discountValue.compareTo(new BigDecimal(100)) > 0) {
                        Toast.makeText(this, "Phần trăm giảm giá không được vượt quá 100%.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!maxDiscountAmountStr.isEmpty()) {
                        BigDecimal maxDiscountAmount = new BigDecimal(maxDiscountAmountStr);
                        if (maxDiscountAmount.compareTo(BigDecimal.ZERO) < 0) {
                            Toast.makeText(this, "Mức giảm tối đa không được âm.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        request.setMaxQuantity(maxDiscountAmount.intValue());
                    }
                } else if (selectedDiscountTypeId == R.id.rb_fixed_amount_discount) {
                    request.setDiscountType("AMOUNT");
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá trị giảm giá hoặc mức giảm tối đa không hợp lệ.", Toast.LENGTH_SHORT).show();
                return;
            }

        } else {
            Toast.makeText(this, "Vui lòng chọn loại Voucher.", Toast.LENGTH_SHORT).show();
            return;
        }
        sellerVoucherViewModel.createCoupon(request);
    }
}