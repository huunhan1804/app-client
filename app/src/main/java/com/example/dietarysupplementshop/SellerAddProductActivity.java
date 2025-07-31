package com.example.dietarysupplementshop;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.dietarysupplementshop.model.ProductSeller;
import com.example.dietarysupplementshop.requests.AddProductRequest;
import com.example.dietarysupplementshop.requests.ProductVariantRequest;
import com.example.dietarysupplementshop.viewModel.SellerAddProductViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SellerAddProductActivity extends AppCompatActivity {
    private static final int MAX_IMAGE_COUNT = 5;
    private String currentUploadPurpose;

    private TextInputEditText etProductName;
    private ImageButton btnBack;
    private LinearLayout llProductImagesContainer;

    private TextInputEditText etProductDescription;
    private ConstraintLayout clCategory;
    private ConstraintLayout clProductClassification;
    private ConstraintLayout clShippingFee;

    private TextView tvCategoryValue;
    private TextView tvClassificationValue;
    private TextView tvPriceValue;
    private TextView tvInventoryValue;
    private TextView tvShippingValue;

    private Button btnUploadDeclarationFile, btnUploadFoodSafetyFile, btnUploadOtherFiles;
    private TextView tvDeclarationFileName, tvFoodSafetyFileName, tvOtherFilesName;
    private TextInputEditText etDeclarationNumber, etDeclarationDate;
    private Button btnSave, btnDisplay;
    private TextView toolbarTitleTextView;

    private List<Uri> selectedImageUris = new ArrayList<>();
    private ActivityResultLauncher<String> pickMultipleImagesLauncher;
    private ActivityResultLauncher<String[]> pickFileLauncher;

    private SellerAddProductViewModel viewModel;
    private ProductSeller productToEdit;

    private List<ConfiguredProductVariant> configuredProductVariants = new ArrayList<>();
    private List<AttributeInputRowData> attributeInputRowDatas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_product);

        viewModel = new ViewModelProvider(this).get(SellerAddProductViewModel.class);

        if (getIntent().hasExtra("product_to_edit")) {
            productToEdit = (ProductSeller) getIntent().getSerializableExtra("product_to_edit");
            viewModel.loadProductForEdit(productToEdit);
        }

        initViews();
        initLaunchers();
        setupListeners();
        observeViewModel();
        loadProductDataForEdit();
        updateButtonState();
        updateVariantSummaryUI();
        displaySelectedImages();
    }

    private void initViews() {
        etProductName = findViewById(R.id.etProductName);
        btnBack = findViewById(R.id.btnBack);
        llProductImagesContainer = findViewById(R.id.llProductImagesContainer);
        etProductDescription = findViewById(R.id.etProductDescription);
        clCategory = findViewById(R.id.clCategory);
        clProductClassification = findViewById(R.id.clProductClassification);
        tvCategoryValue = findViewById(R.id.tvCategoryValue);
        tvClassificationValue = findViewById(R.id.tvClassificationValue);
        tvPriceValue = findViewById(R.id.tvPriceValue);
        tvInventoryValue = findViewById(R.id.tvInventoryValue);
        btnSave = findViewById(R.id.btnSave);
        btnDisplay = findViewById(R.id.btnDisplay);
        toolbarTitleTextView = findViewById(R.id.add_product_title_text);
        tvCategoryValue.setText(getString(R.string.select_category_value));
        tvClassificationValue.setText(getString(R.string.set_classification_value));
        tvPriceValue.setText(getString(R.string.set_price_value));
        tvInventoryValue.setText(getString(R.string.default_inventory_value));
        tvShippingValue.setText(getString(R.string.shipping_fee_value));
    }

    private void initLaunchers() {
        pickMultipleImagesLauncher = registerForActivityResult(
                new ActivityResultContracts.GetMultipleContents(),
                uris -> {
                    if (uris != null && !uris.isEmpty()) {
                        for (Uri uri : uris) {
                            if (selectedImageUris.size() < MAX_IMAGE_COUNT) {
                                selectedImageUris.add(uri);
                            } else {
                                Toast.makeText(this, "Đã đạt giới hạn " + MAX_IMAGE_COUNT + " ảnh.", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        displaySelectedImages();
                        updateButtonState();
                    }
                }
        );

        pickFileLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        String fileName = getFileName(uri);
                        if ("declaration".equals(currentUploadPurpose)) {
                            tvDeclarationFileName.setText(fileName);
                        } else if ("food_safety".equals(currentUploadPurpose)) {
                            tvFoodSafetyFileName.setText(fileName);
                        } else if ("other_files".equals(currentUploadPurpose)) {
                            tvOtherFilesName.setText(fileName);
                        }
                        updateButtonState();
                    }
                    currentUploadPurpose = null;
                }
        );
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        etProductName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButtonState();
            }
        });


        clCategory.setOnClickListener(v -> showCategorySelectionDialog());
        clProductClassification.setOnClickListener(v -> showProductVariantsManagementDialog());
        clShippingFee.setOnClickListener(v -> showShippingFeeInputDialog()); // Vẫn giữ riêng shipping fee

        btnUploadDeclarationFile.setOnClickListener(v -> {
            currentUploadPurpose = "declaration";
            pickFileLauncher.launch(new String[]{"application/pdf", "image/*", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"});
        });

        btnUploadFoodSafetyFile.setOnClickListener(v -> {
            currentUploadPurpose = "food_safety";
            pickFileLauncher.launch(new String[]{"application/pdf", "image/*"});
        });

        btnUploadOtherFiles.setOnClickListener(v -> {
            currentUploadPurpose = "other_files";
            pickFileLauncher.launch(new String[]{"application/pdf", "image/*", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
        });

        etDeclarationDate.setOnClickListener(v -> showDatePickerDialog());

        etProductDescription.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButtonState();
            }
        });
        etDeclarationNumber.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButtonState();
            }
        });
        etDeclarationDate.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButtonState();
            }
        });

        btnSave.setOnClickListener(v -> {
            AddProductRequest request = collectProductData();
            if (request != null) {
                if (productToEdit != null) {
                    viewModel.updateProduct(productToEdit.getProductId(), request);
                } else {
                    viewModel.addProduct(request);
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });

        btnDisplay.setOnClickListener(v -> {
            AddProductRequest request = collectProductData();
            if (request != null) {
                if (productToEdit != null) {
                    viewModel.updateProduct(productToEdit.getProductId(), request);
                } else {
                    viewModel.addProduct(request);
                }
                Toast.makeText(this, "Sản phẩm đã được gửi xét duyệt (mô phỏng)", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin sản phẩm để gửi xét duyệt", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCategorySelectionDialog() {
        final String[] categories = {"Thực phẩm chức năng", "Vitamin", "Khoáng chất", "Thảo dược", "Dinh dưỡng thể thao", "Sản phẩm giảm cân"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn danh mục");
        builder.setItems(categories, (dialog, which) -> {
            tvCategoryValue.setText(categories[which]);
            updateButtonState();
        });
        builder.show();
    }

    private void showProductVariantsManagementDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quản lý phân loại, giá và tồn kho");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_product_variants_management, null); // Layout cho dialog biến thể

        LinearLayout llAttributeInputsContainer = dialogView.findViewById(R.id.llAttributeInputsContainer);
        Button btnAddAttribute = dialogView.findViewById(R.id.btnAddAttribute);
        Button btnGenerateVariants = dialogView.findViewById(R.id.btnGenerateVariants);
        TableLayout tlVariantInputs = dialogView.findViewById(R.id.tlVariantInputs);
        TextView tvNoVariantsHint = dialogView.findViewById(R.id.tvNoVariantsHint);

        List<AttributeInputRow> currentAttributeInputRows = new ArrayList<>();

        if (!attributeInputRowDatas.isEmpty()) {
            for (AttributeInputRowData data : attributeInputRowDatas) {
                AttributeInputRow row = new AttributeInputRow(llAttributeInputsContainer, inflater, data.name, data.values);
                currentAttributeInputRows.add(row);
            }
        } else {
            currentAttributeInputRows.add(new AttributeInputRow(llAttributeInputsContainer, inflater, "", ""));
        }

        if (!configuredProductVariants.isEmpty()) {
            tlVariantInputs.removeAllViews();
            tlVariantInputs.addView(createVariantHeaderRow(inflater));
            tvNoVariantsHint.setVisibility(View.GONE);
            for (ConfiguredProductVariant cpv : configuredProductVariants) {
                addVariantRowToTable(tlVariantInputs, inflater, cpv);
            }
        } else {
            tvNoVariantsHint.setVisibility(View.VISIBLE);
        }

        btnAddAttribute.setOnClickListener(v -> {
            currentAttributeInputRows.add(new AttributeInputRow(llAttributeInputsContainer, inflater, "", ""));
        });

        btnGenerateVariants.setOnClickListener(v -> {
            tlVariantInputs.removeAllViews();
            tlVariantInputs.addView(createVariantHeaderRow(inflater));
            configuredProductVariants.clear();
            tvNoVariantsHint.setVisibility(View.GONE);

            Map<String, List<String>> attributesMap = new HashMap<>();
            for (AttributeInputRow row : currentAttributeInputRows) {
                String attrName = row.etAttributeName.getText().toString().trim();
                String attrValuesStr = row.etAttributeValues.getText().toString().trim();

                if (!attrName.isEmpty() && !attrValuesStr.isEmpty()) {
                    List<String> values = new ArrayList<>();
                    String[] vals = attrValuesStr.split(",");
                    for (String val : vals) {
                        if (!val.trim().isEmpty()) {
                            values.add(val.trim());
                        }
                    }
                    if (!values.isEmpty()) {
                        attributesMap.put(attrName, values);
                    }
                }
            }

            if (attributesMap.isEmpty()) {
                Toast.makeText(SellerAddProductActivity.this, "Vui lòng nhập ít nhất một thuộc tính và giá trị.", Toast.LENGTH_SHORT).show();
                tvNoVariantsHint.setVisibility(View.VISIBLE);
                return;
            }

            List<Map<String, String>> generatedCombinations = generateCombinations(attributesMap);

            if (generatedCombinations.isEmpty()) {
                Toast.makeText(SellerAddProductActivity.this, "Không thể tạo biến thể từ các thuộc tính đã cho.", Toast.LENGTH_SHORT).show();
                tvNoVariantsHint.setVisibility(View.VISIBLE);
                return;
            }

            for (Map<String, String> combination : generatedCombinations) {
                ConfiguredProductVariant existingConfig = findConfiguredVariant(combination);
                addVariantRowToTable(tlVariantInputs, inflater, existingConfig != null ? existingConfig : new ConfiguredProductVariant(combination));
            }
        });

        builder.setView(dialogView);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            configuredProductVariants.clear();
            attributeInputRowDatas.clear();

            for (AttributeInputRow row : currentAttributeInputRows) {
                String name = row.etAttributeName.getText().toString().trim();
                String values = row.etAttributeValues.getText().toString().trim();
                if (!name.isEmpty() && !values.isEmpty()) {
                    attributeInputRowDatas.add(new AttributeInputRowData(name, values));
                }
            }

            for (int i = 1; i < tlVariantInputs.getChildCount(); i++) {
                TableRow row = (TableRow) tlVariantInputs.getChildAt(i);
                Map<String, String> combination = (Map<String, String>) row.getTag(R.id.tag_variant_combination);

                if (combination == null) {
                    Toast.makeText(SellerAddProductActivity.this, "Lỗi dữ liệu biến thể.", Toast.LENGTH_SHORT).show();
                    return;
                }

                EditText etVariantOriginPrice = row.findViewById(R.id.etVariantOriginPrice);
                EditText etVariantSalePrice = row.findViewById(R.id.etVariantSalePrice);
                EditText etVariantQuantity = row.findViewById(R.id.etVariantQuantity);

                double originPrice = 0.0;
                double salePrice = 0.0;
                int quantity = 0;

                try {
                    String originPriceStr = etVariantOriginPrice.getText().toString().trim();
                    if (!originPriceStr.isEmpty()) {
                        originPrice = Double.parseDouble(originPriceStr);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(SellerAddProductActivity.this, "Giá gốc không hợp lệ cho: " + getVariantNameFromCombination(combination), Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String salePriceStr = etVariantSalePrice.getText().toString().trim();
                    if (!salePriceStr.isEmpty()) {
                        salePrice = Double.parseDouble(salePriceStr);
                    } else {
                        salePrice = originPrice;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(SellerAddProductActivity.this, "Giá bán không hợp lệ cho: " + getVariantNameFromCombination(combination), Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String quantityStr = etVariantQuantity.getText().toString().trim();
                    if (!quantityStr.isEmpty()) {
                        quantity = Integer.parseInt(quantityStr);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(SellerAddProductActivity.this, "Số lượng tồn kho không hợp lệ cho: " + getVariantNameFromCombination(combination), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (originPrice <= 0) {
                    Toast.makeText(SellerAddProductActivity.this, "Giá gốc phải lớn hơn 0 cho: " + getVariantNameFromCombination(combination), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (salePrice <= 0) {
                    salePrice = originPrice;
                }
                if (salePrice > originPrice) {
                    Toast.makeText(SellerAddProductActivity.this, "Giá bán không được lớn hơn giá gốc cho: " + getVariantNameFromCombination(combination), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (quantity < 0) {
                    Toast.makeText(SellerAddProductActivity.this, "Số lượng tồn kho không được âm cho: " + getVariantNameFromCombination(combination), Toast.LENGTH_SHORT).show();
                    return;
                }

                configuredProductVariants.add(new ConfiguredProductVariant(combination, originPrice, salePrice, quantity));
            }
            updateVariantSummaryUI();
            updateButtonState();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void addVariantRowToTable(TableLayout tlVariantInputs, LayoutInflater inflater, ConfiguredProductVariant variant) {
        TableRow row = (TableRow) inflater.inflate(R.layout.variant_input_row, tlVariantInputs, false);
        TextView tvVariantName = row.findViewById(R.id.tvVariantName);
        EditText etVariantOriginPrice = row.findViewById(R.id.etVariantOriginPrice);
        EditText etVariantSalePrice = row.findViewById(R.id.etVariantSalePrice);
        EditText etVariantQuantity = row.findViewById(R.id.etVariantQuantity);

        tvVariantName.setText(getVariantNameFromCombination(variant.attributes));

        etVariantOriginPrice.setText(variant.originPrice > 0 ? String.valueOf(variant.originPrice) : "");
        etVariantSalePrice.setText(variant.salePrice > 0 ? String.valueOf(variant.salePrice) : "");
        etVariantQuantity.setText(variant.quantityInStock >= 0 ? String.valueOf(variant.quantityInStock) : "");

        row.setTag(R.id.tag_variant_combination, variant.attributes);
        tlVariantInputs.addView(row);
    }

    private String getVariantNameFromCombination(Map<String, String> combination) {
        StringBuilder variantNameBuilder = new StringBuilder();
        List<String> sortedAttributeNames = new ArrayList<>(combination.keySet());
        Collections.sort(sortedAttributeNames);
        for (String attrName : sortedAttributeNames) {
            if (variantNameBuilder.length() > 0) {
                variantNameBuilder.append(" / ");
            }
            variantNameBuilder.append(combination.get(attrName));
        }
        return variantNameBuilder.toString();
    }

    private class AttributeInputRow {
        EditText etAttributeName;
        EditText etAttributeValues;
        ImageButton btnRemove;
        LinearLayout rowLayout;

        AttributeInputRow(LinearLayout parentLayout, LayoutInflater inflater, String name, String values) {
            rowLayout = (LinearLayout) inflater.inflate(R.layout.attribute_input_row, parentLayout, false);
            etAttributeName = rowLayout.findViewById(R.id.etAttributeName);
            etAttributeValues = rowLayout.findViewById(R.id.etAttributeValues);
            btnRemove = rowLayout.findViewById(R.id.btnRemoveAttribute);

            etAttributeName.setText(name);
            etAttributeValues.setText(values);

            btnRemove.setOnClickListener(v -> {
                parentLayout.removeView(rowLayout);
            });

            parentLayout.addView(rowLayout);
        }
    }

    private static class AttributeInputRowData {
        String name;
        String values;

        AttributeInputRowData(String name, String values) {
            this.name = name;
            this.values = values;
        }
    }

    private List<Map<String, String>> generateCombinations(Map<String, List<String>> attributesMap) {
        List<Map<String, String>> combinations = new ArrayList<>();
        if (attributesMap.isEmpty()) {
            combinations.add(new HashMap<>());
            return combinations;
        }

        String firstAttributeName = attributesMap.keySet().iterator().next();
        List<String> firstAttributeValues = attributesMap.get(firstAttributeName);

        Map<String, List<String>> remainingAttributesMap = new HashMap<>(attributesMap);
        remainingAttributesMap.remove(firstAttributeName);

        List<Map<String, String>> subCombinations = generateCombinations(remainingAttributesMap);

        for (String value : firstAttributeValues) {
            for (Map<String, String> subCombination : subCombinations) {
                Map<String, String> newCombination = new HashMap<>(subCombination);
                newCombination.put(firstAttributeName, value);
                combinations.add(newCombination);
            }
        }
        return combinations;
    }

    private ConfiguredProductVariant findConfiguredVariant(Map<String, String> combination) {
        if (configuredProductVariants == null) return null;
        for (ConfiguredProductVariant variant : configuredProductVariants) {
            if (variant.attributes.equals(combination)) {
                return variant;
            }
        }
        return null;
    }

    private static class ConfiguredProductVariant {
        public Map<String, String> attributes = new HashMap<>();
        public double originPrice = 0.0;
        public double salePrice = 0.0;
        public int quantityInStock = 0;

        public ConfiguredProductVariant(Map<String, String> attributes) {
            this.attributes = attributes;
        }

        public ConfiguredProductVariant(Map<String, String> attributes, double originPrice, double salePrice, int quantityInStock) {
            this.attributes = attributes;
            this.originPrice = originPrice;
            this.salePrice = salePrice;
            this.quantityInStock = quantityInStock;
        }
    }

    private TableRow createVariantHeaderRow(LayoutInflater inflater) {
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        headerRow.setBackgroundResource(R.drawable.rounded_card_background);
        headerRow.setPadding(0, 8, 0, 8);

        TextView tvHeaderVariant = new TextView(this);
        tvHeaderVariant.setText("Biến thể");
        tvHeaderVariant.setPadding(8, 0, 8, 0);
        tvHeaderVariant.setTextSize(14);
        tvHeaderVariant.setTypeface(null, android.graphics.Typeface.BOLD);
        headerRow.addView(tvHeaderVariant);

        TextView tvHeaderOriginPrice = new TextView(this);
        tvHeaderOriginPrice.setText("Giá gốc");
        tvHeaderOriginPrice.setPadding(8, 0, 8, 0);
        tvHeaderOriginPrice.setTextSize(14);
        tvHeaderOriginPrice.setTypeface(null, android.graphics.Typeface.BOLD);
        tvHeaderOriginPrice.setGravity(Gravity.END);
        headerRow.addView(tvHeaderOriginPrice);

        TextView tvHeaderSalePrice = new TextView(this);
        tvHeaderSalePrice.setText("Giá bán");
        tvHeaderSalePrice.setPadding(8, 0, 8, 0);
        tvHeaderSalePrice.setTextSize(14);
        tvHeaderSalePrice.setTypeface(null, android.graphics.Typeface.BOLD);
        tvHeaderSalePrice.setGravity(Gravity.END);
        headerRow.addView(tvHeaderSalePrice);

        TextView tvHeaderQuantity = new TextView(this);
        tvHeaderQuantity.setText("SL");
        tvHeaderQuantity.setPadding(8, 0, 8, 0);
        tvHeaderQuantity.setTextSize(14);
        tvHeaderQuantity.setTypeface(null, android.graphics.Typeface.BOLD);
        tvHeaderQuantity.setGravity(Gravity.END);
        headerRow.addView(tvHeaderQuantity);

        return headerRow;
    }


    private void showShippingFeeInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nhập phí vận chuyển");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 20, 30, 0);

        RadioGroup radioGroup = new RadioGroup(this);
        RadioButton rbFree = new RadioButton(this);
        rbFree.setText("Miễn phí");
        rbFree.setTag("Miễn phí");
        radioGroup.addView(rbFree);

        RadioButton rbFixed = new RadioButton(this);
        rbFixed.setText("Phí cố định");
        rbFixed.setTag("Phí cố định");
        radioGroup.addView(rbFixed);

        final EditText etFixedFee = new EditText(this);
        etFixedFee.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etFixedFee.setHint("Nhập số tiền (VD: 25000)");
        etFixedFee.setVisibility(View.GONE);
        layout.addView(radioGroup);
        layout.addView(etFixedFee);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            if (checkedRadioButton != null) {
                if ("Phí cố định".equals(checkedRadioButton.getTag())) {
                    etFixedFee.setVisibility(View.VISIBLE);
                } else {
                    etFixedFee.setVisibility(View.GONE);
                }
            }
        });

        String currentShipping = tvShippingValue.getText().toString();
        if ("Miễn phí".equals(currentShipping)) {
            rbFree.setChecked(true);
        } else if (currentShipping.startsWith("Phí cố định")) {
            rbFixed.setChecked(true);
            etFixedFee.setVisibility(View.VISIBLE);
            String fee = currentShipping.replace("Phí cố định (", "").replace(" VNĐ)", "").replace(",", "").trim();
            etFixedFee.setText(fee);
        }

        builder.setView(layout);

        builder.setPositiveButton("OK", (dialog, which) -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadioButton = radioGroup.findViewById(selectedId);
                String selectedOption = (String) selectedRadioButton.getTag();
                if ("Miễn phí".equals(selectedOption)) {
                    tvShippingValue.setText("Miễn phí");
                } else if ("Phí cố định".equals(selectedOption)) {
                    String feeStr = etFixedFee.getText().toString().trim();
                    if (!feeStr.isEmpty()) {
                        try {
                            double fee = Double.parseDouble(feeStr);
                            if (fee < 0) {
                                Toast.makeText(this, "Phí cố định không được âm.", Toast.LENGTH_SHORT).show();
                                tvShippingValue.setText(getString(R.string.shipping_fee_value));
                            } else {
                                tvShippingValue.setText(String.format("Phí cố định (%,.0f VNĐ)", fee));
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Phí cố định không hợp lệ.", Toast.LENGTH_SHORT).show();
                            tvShippingValue.setText(getString(R.string.shipping_fee_value));
                        }
                    } else {
                        Toast.makeText(this, "Vui lòng nhập phí cố định.", Toast.LENGTH_SHORT).show();
                        tvShippingValue.setText(getString(R.string.shipping_fee_value));
                    }
                }
            } else {
                tvShippingValue.setText(getString(R.string.shipping_fee_value));
            }
            updateButtonState();
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }


    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etDeclarationDate.setText(date);
                    updateButtonState();
                }, year, month, day);
        datePickerDialog.show();
    }

    private void updateButtonState() {
        boolean isProductNameValid = !Objects.requireNonNull(etProductName.getText()).toString().trim().isEmpty();
        boolean isDescriptionValid = !Objects.requireNonNull(etProductDescription.getText()).toString().trim().isEmpty();
        boolean isCategorySelected = !tvCategoryValue.getText().toString().equals(getString(R.string.select_category_value));
        boolean areImagesSelected = !selectedImageUris.isEmpty();

        boolean areVariantsConfigured = !configuredProductVariants.isEmpty();
        boolean areAllVariantPricesValid = true;
        boolean areAllVariantQuantitiesValid = true;
        if (areVariantsConfigured) {
            for (ConfiguredProductVariant variant : configuredProductVariants) {
                if (variant.originPrice <= 0 || variant.quantityInStock < 0 || (variant.salePrice > 0 && variant.salePrice > variant.originPrice)) {
                    areAllVariantPricesValid = false;
                    areAllVariantQuantitiesValid = false;
                    break;
                }
            }
        } else {
            areAllVariantPricesValid = false;
            areAllVariantQuantitiesValid = false;
        }

        boolean isShippingFeeSet = !tvShippingValue.getText().toString().equals(getString(R.string.shipping_fee_value));

        boolean isDeclarationFileUploaded = !tvDeclarationFileName.getText().toString().equals(getString(R.string.no_file_selected));
        boolean isDeclarationNumberEntered = !Objects.requireNonNull(etDeclarationNumber.getText()).toString().trim().isEmpty();
        boolean isDeclarationDateEntered = !Objects.requireNonNull(etDeclarationDate.getText()).toString().trim().isEmpty();
        boolean isFoodSafetyFileUploaded = !tvFoodSafetyFileName.getText().toString().equals(getString(R.string.no_file_selected));

        boolean canSave = isProductNameValid && isDescriptionValid && isCategorySelected && areImagesSelected &&
                areVariantsConfigured && areAllVariantPricesValid && areAllVariantQuantitiesValid && isShippingFeeSet;
        btnSave.setEnabled(canSave);
        btnSave.setTextColor(canSave ? getResources().getColor(R.color.color_app, null) : getResources().getColor(android.R.color.darker_gray, null));
        btnSave.setBackgroundResource(R.drawable.rounded_button_border);
        btnSave.setBackgroundTintList(null);


        boolean canSubmitForReview = canSave && isDeclarationFileUploaded && isDeclarationNumberEntered &&
                isDeclarationDateEntered && isFoodSafetyFileUploaded;
        btnDisplay.setEnabled(canSubmitForReview);
        btnDisplay.setBackgroundTintList(getResources().getColorStateList(canSubmitForReview ? R.color.color_app : android.R.color.darker_gray, null));
        btnDisplay.setTextColor(getResources().getColor(android.R.color.white, null));
    }

    private void displaySelectedImages() {
        llProductImagesContainer.removeAllViews();

        FrameLayout addImageFrame = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.add_image_thumbnail_placeholder, llProductImagesContainer, false); // Ép kiểu FrameLayout là OK
        addImageFrame.setOnClickListener(v -> {
            if (selectedImageUris.size() < MAX_IMAGE_COUNT) {
                pickMultipleImagesLauncher.launch("image/*");
            } else {
                Toast.makeText(this, "Đã đạt giới hạn " + MAX_IMAGE_COUNT + " ảnh.", Toast.LENGTH_SHORT).show();
            }
        });
        llProductImagesContainer.addView(addImageFrame);


        for (Uri uri : selectedImageUris) {

            View imageItemView = LayoutInflater.from(this).inflate(R.layout.item_selected_media_thumbnail, llProductImagesContainer, false);
            ImageView imageView = imageItemView.findViewById(R.id.iv_media_thumbnail);
            ImageView deleteIcon = imageItemView.findViewById(R.id.iv_remove_media);

            Glide.with(this)
                    .load(uri)
                    .into(imageView);

            deleteIcon.setOnClickListener(v -> {
                selectedImageUris.remove(uri);
                displaySelectedImages();
                updateButtonState();
            });
            llProductImagesContainer.addView(imageItemView);
        }

        if (selectedImageUris.size() >= MAX_IMAGE_COUNT) {
            addImageFrame.setVisibility(View.GONE);
        } else {
            addImageFrame.setVisibility(View.VISIBLE);
        }
    }


    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void updateVariantSummaryUI() {
        if (configuredProductVariants.isEmpty()) {
            tvClassificationValue.setText(getString(R.string.set_classification_value));
            tvPriceValue.setText(getString(R.string.set_price_value));
            tvInventoryValue.setText(getString(R.string.default_inventory_value));
            return;
        }

        Set<String> attributeNames = new HashSet<>();
        Map<String, Set<String>> attributeValuesMap = new HashMap<>();
        for (ConfiguredProductVariant variant : configuredProductVariants) {
            for (Map.Entry<String, String> entry : variant.attributes.entrySet()) {
                attributeNames.add(entry.getKey());
                attributeValuesMap.computeIfAbsent(entry.getKey(), k -> new HashSet<>()).add(entry.getValue());
            }
        }
        StringBuilder classificationSummary = new StringBuilder();
        List<String> sortedAttributeNames = new ArrayList<>(attributeNames);
        Collections.sort(sortedAttributeNames);

        for (String attrName : sortedAttributeNames) {
            if (classificationSummary.length() > 0) {
                classificationSummary.append("; ");
            }
            List<String> sortedValues = new ArrayList<>(attributeValuesMap.get(attrName));
            Collections.sort(sortedValues);
            classificationSummary.append(attrName).append(": ").append(String.join(", ", sortedValues));
        }
        tvClassificationValue.setText(classificationSummary.toString());

        double minPrice = Double.MAX_VALUE;
        double maxPrice = Double.MIN_VALUE;
        long totalQuantity = 0;

        for (ConfiguredProductVariant variant : configuredProductVariants) {
            double currentPrice = (variant.salePrice > 0 && variant.salePrice < variant.originPrice) ? variant.salePrice : variant.originPrice;
            if (currentPrice < minPrice) minPrice = currentPrice;
            if (currentPrice > maxPrice) maxPrice = currentPrice;
            totalQuantity += variant.quantityInStock;
        }

        if (minPrice == Double.MAX_VALUE) {
            tvPriceValue.setText(getString(R.string.set_price_value));
        } else if (minPrice == maxPrice) {
            tvPriceValue.setText(String.format("%,.0f VNĐ", minPrice));
        } else {
            tvPriceValue.setText(String.format("%,.0f - %,.0f VNĐ", minPrice, maxPrice));
        }

        tvInventoryValue.setText(totalQuantity + " sản phẩm");
    }


    private AddProductRequest collectProductData() {
        String productName = Objects.requireNonNull(etProductName.getText()).toString().trim();
        String description = Objects.requireNonNull(etProductDescription.getText()).toString().trim();
        String category = tvCategoryValue.getText().toString();
        String shippingFee = tvShippingValue.getText().toString();

        String declarationFileName = tvDeclarationFileName.getText().toString();
        String foodSafetyFileName = tvFoodSafetyFileName.getText().toString();
        String otherFilesName = tvOtherFilesName.getText().toString();

        String declarationNumber = Objects.requireNonNull(etDeclarationNumber.getText()).toString().trim();
        String declarationDate = Objects.requireNonNull(etDeclarationDate.getText()).toString().trim();

        List<ProductVariantRequest> productVariantRequests = new ArrayList<>();
        for (ConfiguredProductVariant cpv : configuredProductVariants) {
            String variantName = getVariantNameFromCombination(cpv.attributes);
            productVariantRequests.add(new ProductVariantRequest(variantName, cpv.originPrice, cpv.salePrice, cpv.quantityInStock));
        }

        if (!isProductDataValid(productName, description, category, selectedImageUris,
                productVariantRequests, shippingFee, declarationFileName,
                declarationNumber, declarationDate, foodSafetyFileName)) {
            return null;
        }

//        return new AddProductRequest(
//                productName,
//                description,
//                category,
//                productVariantRequests,
//                selectedImageUris,
//        );
    }

    private boolean isProductDataValid(String productName, String description, String category,
                                       List<Uri> images, List<ProductVariantRequest> variants, String shippingFee,
                                       String declarationFileName, String declarationNumber, String declarationDate,
                                       String foodSafetyFileName) {
        if (productName.isEmpty() || description.isEmpty() ||
                category.equals(getString(R.string.select_category_value)) ||
                images.isEmpty() ||
                variants.isEmpty() ||
                shippingFee.equals(getString(R.string.shipping_fee_value))) {
            return false;
        }

        for (ProductVariantRequest variant : variants) {
            if (variant.getOriginPrice() <= 0) {
                return false;
            }
            if (variant.getSalePrice() > 0 && variant.getSalePrice() > variant.getOriginPrice()) {
                return false;
            }
            if (variant.getQuantityInStock() < 0) {
                return false;
            }
        }
        return true;
    }

    private void loadProductDataForEdit() {
        if (productToEdit != null) {
            if (toolbarTitleTextView != null) {
                toolbarTitleTextView.setText(getString(R.string.edit_product_title));
            }

            etProductName.setText(productToEdit.getProductName());
            etProductDescription.setText(productToEdit.getDescription());
            tvCategoryValue.setText(productToEdit.getCategory());

            configuredProductVariants.clear();
            attributeInputRowDatas.clear();

            if (productToEdit.getMinPrice() > 0 || productToEdit.getStockQuantity() >= 0) {
                Map<String, String> singleAttribute = new HashMap<>();
                singleAttribute.put("Phiên bản", "Duy nhất");
                configuredProductVariants.add(new ConfiguredProductVariant(
                        singleAttribute,
                        productToEdit.getMinPrice(),
                        productToEdit.getMinPrice(),
                        productToEdit.getStockQuantity()));

                attributeInputRowDatas.add(new AttributeInputRowData("Phiên bản", "Duy nhất"));
            }
            updateVariantSummaryUI();


            if (productToEdit.getImageUrl() != null && !productToEdit.getImageUrl().isEmpty()) {
                selectedImageUris.clear();
                try {
                    selectedImageUris.add(Uri.parse(productToEdit.getImageUrl()));
                    displaySelectedImages();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            tvDeclarationFileName.setText(getString(R.string.no_file_selected));
            tvFoodSafetyFileName.setText(getString(R.string.no_file_selected));
            tvOtherFilesName.setText(getString(R.string.no_file_selected));
            etDeclarationNumber.setText("");
            etDeclarationDate.setText("");

            updateButtonState();
        }
    }

    private void observeViewModel() {
        viewModel.isAddingProduct.observe(this, isLoading -> {
            btnSave.setEnabled(!isLoading);
            btnDisplay.setEnabled(!isLoading);
        });

        viewModel.productAddedSuccessfully.observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Thao tác sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.errorMessage.observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.productToEdit.observe(this, product -> {
            if (product != null) {
                if (toolbarTitleTextView != null) {
                    toolbarTitleTextView.setText(getString(R.string.edit_product_title));
                }

                etProductName.setText(product.getProductName());
                etProductDescription.setText(product.getDescription());
                tvCategoryValue.setText(product.getCategory());

                configuredProductVariants.clear();
                attributeInputRowDatas.clear();

                if (product.getMinPrice() > 0 || product.getStockQuantity() >= 0) {
                    Map<String, String> singleAttribute = new HashMap<>();
                    singleAttribute.put("Phiên bản", "Duy nhất");
                    configuredProductVariants.add(new ConfiguredProductVariant(
                            singleAttribute,
                            product.getMinPrice(),
                            product.getMinPrice(),
                            product.getStockQuantity()));

                    attributeInputRowDatas.add(new AttributeInputRowData("Phiên bản", "Duy nhất"));
                }
                updateVariantSummaryUI();

                if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                    selectedImageUris.clear();
                    try {
                        selectedImageUris.add(Uri.parse(product.getImageUrl()));
                        displaySelectedImages();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                tvDeclarationFileName.setText(getString(R.string.no_file_selected));
                tvFoodSafetyFileName.setText(getString(R.string.no_file_selected));
                tvOtherFilesName.setText(getString(R.string.no_file_selected));
                etDeclarationNumber.setText("");
                etDeclarationDate.setText("");

                updateButtonState();
            }
        });
    }
}