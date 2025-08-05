package com.example.dietarysupplementshop;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.dietarysupplementshop.requests.AddNewProductRequest;
import com.example.dietarysupplementshop.requests.AddProductVariantsRequest;
import com.example.dietarysupplementshop.requests.UpdateProductRequest;
import com.example.dietarysupplementshop.responses.ProductInfoDTO;
import com.example.dietarysupplementshop.responses.ProductVariantDTO;
import com.example.dietarysupplementshop.viewModel.AgencyAddProductViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AgencyAddProductActivity extends AppCompatActivity {

    private static final int MAX_IMAGE_COUNT = 5;

    private TextInputEditText etProductName;
    private ImageButton btnBack;
    private LinearLayout llProductImagesContainer;
    private TextInputEditText etProductDescription;
    private ConstraintLayout clCategory;
    private ConstraintLayout clProductClassification;
    private TextView tvCategoryValue;
    private TextView tvClassificationValue;
    private TextView tvPriceValue;
    private TextView tvInventoryValue;
    private Button btnSave, btnDisplay;
    private TextView toolbarTitleTextView;

    private List<Uri> selectedImageUris = new ArrayList<>();
    private ActivityResultLauncher<String> pickMultipleImagesLauncher;
    private AgencyAddProductViewModel viewModel;
    private ProductInfoDTO productToEdit;
    private long selectedCategoryId;

    private List<ConfiguredProductVariant> configuredProductVariants = new ArrayList<>();
    private TableLayout tlVariantInputs;
    private TextView tvNoVariantsHint;
    private Button btnAddVariant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_add_product);

        viewModel = new ViewModelProvider(this).get(AgencyAddProductViewModel.class);

        if (getIntent().hasExtra("product_to_edit")) {
            productToEdit = (ProductInfoDTO) getIntent().getSerializableExtra("product_to_edit");
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
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        etProductName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButtonState();
            }
        });
        etProductDescription.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButtonState();
            }
        });
        clCategory.setOnClickListener(v -> showCategorySelectionDialog());
        clProductClassification.setOnClickListener(v -> showProductVariantsManagementDialog());

        btnSave.setOnClickListener(v -> uploadImagesAndSubmit());
        btnDisplay.setOnClickListener(v -> {
            uploadImagesAndSubmit();
            Toast.makeText(this, "Sản phẩm đã được gửi xét duyệt (mô phỏng)", Toast.LENGTH_LONG).show();
        });
    }

    private void showCategorySelectionDialog() {
        final String[] categories = {"Thực phẩm chức năng", "Vitamin", "Khoáng chất", "Thảo dược", "Dinh dưỡng thể thao", "Sản phẩm giảm cân"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn danh mục");
        builder.setItems(categories, (dialog, which) -> {
            tvCategoryValue.setText(categories[which]);
            selectedCategoryId = which + 1;
            updateButtonState();
        });
        builder.show();
    }

    private void uploadImagesAndSubmit() {
        viewModel.uploadImages(selectedImageUris, new AgencyAddProductViewModel.UploadImageCallback() {
            @Override
            public void onSuccess(List<String> imageUrls) {
                if (productToEdit != null) {
                    UpdateProductRequest request = collectUpdateProductData(imageUrls);
                    if (request != null) {
                        viewModel.updateProduct(productToEdit.getProduct_id(), request);
                    } else {
                        Toast.makeText(AgencyAddProductActivity.this, "Vui lòng nhập đủ thông tin sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AddNewProductRequest request = collectAddNewProductData(imageUrls);
                    if (request != null) {
                        viewModel.createProduct(request);
                    } else {
                        Toast.makeText(AgencyAddProductActivity.this, "Vui lòng nhập đủ thông tin sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onError(String error) {
                Toast.makeText(AgencyAddProductActivity.this, "Lỗi khi upload ảnh: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProductVariantsManagementDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quản lý phân loại, giá và tồn kho");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_product_variants_management, null);
        tlVariantInputs = dialogView.findViewById(R.id.tlVariantInputs);
        tvNoVariantsHint = dialogView.findViewById(R.id.tvNoVariantsHint);
        btnAddVariant = dialogView.findViewById(R.id.btnAddVariant);

        tlVariantInputs.removeAllViews();
        if (!configuredProductVariants.isEmpty()) {
            tvNoVariantsHint.setVisibility(View.GONE);
            for (ConfiguredProductVariant variant : configuredProductVariants) {
                addVariantRowToTable(tlVariantInputs, inflater, variant);
            }
        } else {
            tvNoVariantsHint.setVisibility(View.VISIBLE);
        }

        btnAddVariant.setOnClickListener(v -> {
            ConfiguredProductVariant newVariant = new ConfiguredProductVariant("Duy nhất", 0.0, 0.0, 0, null);
            configuredProductVariants.add(newVariant);
            addVariantRowToTable(tlVariantInputs, inflater, newVariant);
            tvNoVariantsHint.setVisibility(View.GONE);
        });

        builder.setView(dialogView);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            List<ConfiguredProductVariant> tempConfiguredVariants = new ArrayList<>();
            boolean hasInvalidData = false;

            for (int i = 0; i < tlVariantInputs.getChildCount(); i++) {
                TableRow row = (TableRow) tlVariantInputs.getChildAt(i);
                EditText etVariantName = row.findViewById(R.id.etVariantName);
                EditText etVariantListPrice = row.findViewById(R.id.etVariantListPrice);
                EditText etVariantSalePrice = row.findViewById(R.id.etVariantSalePrice);
                EditText etVariantQuantity = row.findViewById(R.id.etVariantQuantity);

                String variantName = etVariantName.getText().toString().trim();
                String listPriceStr = etVariantListPrice.getText().toString().trim();
                String salePriceStr = etVariantSalePrice.getText().toString().trim();
                String quantityStr = etVariantQuantity.getText().toString().trim();

                if (variantName.isEmpty() || listPriceStr.isEmpty() || quantityStr.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đủ thông tin cho tất cả các biến thể.", Toast.LENGTH_SHORT).show();
                    hasInvalidData = true;
                    break;
                }

                double originPrice, salePrice;
                int quantity;

                try {
                    originPrice = Double.parseDouble(listPriceStr);
                    salePrice = salePriceStr.isEmpty() ? originPrice : Double.parseDouble(salePriceStr);
                    quantity = Integer.parseInt(quantityStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Giá hoặc số lượng không hợp lệ.", Toast.LENGTH_SHORT).show();
                    hasInvalidData = true;
                    break;
                }

                if (originPrice <= 0 || salePrice > originPrice || quantity < 0) {
                    Toast.makeText(this, "Dữ liệu biến thể không hợp lệ: Giá bán > 0, Giá khuyến mãi <= Giá bán, Số lượng >= 0.", Toast.LENGTH_SHORT).show();
                    hasInvalidData = true;
                    break;
                }

                tempConfiguredVariants.add(new ConfiguredProductVariant(variantName, originPrice, salePrice, quantity, null, 0));
            }

            if (!hasInvalidData) {
                configuredProductVariants.clear();
                configuredProductVariants.addAll(tempConfiguredVariants);
                updateVariantSummaryUI();
                updateButtonState();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void addVariantRowToTable(TableLayout tlVariantInputs, LayoutInflater inflater, ConfiguredProductVariant variant) {
        TableRow row = (TableRow) inflater.inflate(R.layout.variant_input_row, tlVariantInputs, false);
        EditText etVariantName = row.findViewById(R.id.etVariantName);
        EditText etVariantOriginPrice = row.findViewById(R.id.etVariantListPrice);
        EditText etVariantSalePrice = row.findViewById(R.id.etVariantSalePrice);
        EditText etVariantQuantity = row.findViewById(R.id.etVariantQuantity);

        if (variant != null) {
            etVariantName.setText(variant.variantName);
            etVariantOriginPrice.setText(variant.originPrice > 0 ? String.valueOf(variant.originPrice) : "");
            etVariantSalePrice.setText(variant.salePrice > 0 ? String.valueOf(variant.salePrice) : "");
            etVariantQuantity.setText(variant.quantityInStock >= 0 ? String.valueOf(variant.quantityInStock) : "");
        }
        tlVariantInputs.addView(row);
    }

    private static class ConfiguredProductVariant {
        public String variantName;
        public double originPrice = 0.0;
        public double salePrice = 0.0;
        public int quantityInStock = 0;
        public Long variantId;

        // THAY ĐỔI: Thêm sold_amount vào constructor
        public int soldAmount;

        public ConfiguredProductVariant(String variantName, double originPrice, double salePrice, int quantityInStock, Long variantId) {
            this.variantName = variantName;
            this.originPrice = originPrice;
            this.salePrice = salePrice;
            this.quantityInStock = quantityInStock;
            this.variantId = variantId;
            this.soldAmount = 0;
        }

        // THAY ĐỔI: Thêm constructor đầy đủ
        public ConfiguredProductVariant(String variantName, double originPrice, double salePrice, int quantityInStock, Long variantId, int soldAmount) {
            this.variantName = variantName;
            this.originPrice = originPrice;
            this.salePrice = salePrice;
            this.quantityInStock = quantityInStock;
            this.variantId = variantId;
            this.soldAmount = soldAmount;
        }
    }

    // THAY ĐỔI: Cập nhật hàm collectUpdateProductData
    private UpdateProductRequest collectUpdateProductData(List<String> imageUrls) {
        String productName = Objects.requireNonNull(etProductName.getText()).toString().trim();
        String description = Objects.requireNonNull(etProductDescription.getText()).toString().trim();
        if (productName.isEmpty() || description.isEmpty() || selectedCategoryId == 0 || configuredProductVariants.isEmpty() || imageUrls.isEmpty()) {
            return null;
        }

        double listPrice = 0.0;
        double salePrice = 0.0;
        List<AddProductVariantsRequest> addProductVariantsRequests = new ArrayList<>();

        if (!configuredProductVariants.isEmpty()) {
            listPrice = configuredProductVariants.get(0).originPrice;
            salePrice = configuredProductVariants.get(0).salePrice;

            for (ConfiguredProductVariant cpv : configuredProductVariants) {
                addProductVariantsRequests.add(new AddProductVariantsRequest(
                        cpv.variantName,
                        cpv.originPrice,
                        cpv.salePrice,
                        cpv.quantityInStock,
                        cpv.soldAmount
                ));
            }
        }

        UpdateProductRequest request = new UpdateProductRequest();
        request.setProduct_id(productToEdit.getProduct_id());
        request.setProduct_name(productName);
        request.setProduct_description(description);
        request.setCategory_id(selectedCategoryId);
        request.setImage_urls(imageUrls);
        request.setProduct_list_price(listPrice);
        request.setProduct_sale_price(salePrice);
        request.setProduct_variant_list(addProductVariantsRequests);

        return request;
    }

    private void loadProductDataForEdit() {
        if (productToEdit != null) {
            if (toolbarTitleTextView != null) {
                toolbarTitleTextView.setText(getString(R.string.edit_product_title));
            }

            etProductName.setText(productToEdit.getProduct_name());
            etProductDescription.setText(productToEdit.getProduct_description());

            if (productToEdit.getCategory() != null) {
                tvCategoryValue.setText(productToEdit.getCategory().getCategory_name());
                selectedCategoryId = productToEdit.getCategory().getCategory_id();
            }

            configuredProductVariants.clear();

            if (productToEdit.getProduct_variant_list() != null && !productToEdit.getProduct_variant_list().isEmpty()) {
                for (ProductVariantDTO variantDTO : productToEdit.getProduct_variant_list()) {
                    configuredProductVariants.add(new ConfiguredProductVariant(
                            variantDTO.getProduct_variant_name(),
                            Double.parseDouble(variantDTO.getList_price()),
                            Double.parseDouble(variantDTO.getSale_price()),
                            variantDTO.getInventory_quantity(),
                            variantDTO.getProduct_variant_id()));
                }
            } else {
                configuredProductVariants.add(new ConfiguredProductVariant(
                        "Duy nhất",
                        Double.parseDouble(productToEdit.getProduct_list_price()),
                        Double.parseDouble(productToEdit.getProduct_sale_price()),
                        productToEdit.getQuantity_in_stock(),
                        null));
            }

            updateVariantSummaryUI();

            if (productToEdit.getMedia_url() != null && !productToEdit.getMedia_url().isEmpty()) {
                selectedImageUris.clear();
                for (String url : productToEdit.getMedia_url()) {
                    try {
                        selectedImageUris.add(Uri.parse(url));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            displaySelectedImages();
            updateButtonState();
        }
    }

    private void updateButtonState() {
        boolean isProductNameValid = !Objects.requireNonNull(etProductName.getText()).toString().trim().isEmpty();
        boolean isDescriptionValid = !Objects.requireNonNull(etProductDescription.getText()).toString().trim().isEmpty();
        boolean isCategorySelected = !tvCategoryValue.getText().toString().equals(getString(R.string.select_category_value));
        boolean areImagesSelected = !selectedImageUris.isEmpty();

        boolean areVariantsConfigured = !configuredProductVariants.isEmpty();
        boolean areAllVariantPricesAndQuantitiesValid = true;
        if (areVariantsConfigured) {
            for (ConfiguredProductVariant variant : configuredProductVariants) {
                if (variant.originPrice <= 0 || variant.quantityInStock < 0 || (variant.salePrice > 0 && variant.salePrice > variant.originPrice)) {
                    areAllVariantPricesAndQuantitiesValid = false;
                    break;
                }
            }
        } else {
            areAllVariantPricesAndQuantitiesValid = false;
        }

        boolean canSave = isProductNameValid && isDescriptionValid && isCategorySelected && areImagesSelected &&
                areVariantsConfigured && areAllVariantPricesAndQuantitiesValid;

        btnSave.setEnabled(canSave);
        btnSave.setTextColor(canSave ? getResources().getColor(R.color.color_app, null) : getResources().getColor(android.R.color.darker_gray, null));
        btnSave.setBackgroundResource(R.drawable.rounded_button_border);
        btnSave.setBackgroundTintList(null);

        boolean canSubmitForReview = canSave;
        btnDisplay.setEnabled(canSubmitForReview);
        btnDisplay.setBackgroundTintList(getResources().getColorStateList(canSubmitForReview ? R.color.color_app : android.R.color.darker_gray, null));
        btnDisplay.setTextColor(getResources().getColor(android.R.color.white, null));
    }


    private void displaySelectedImages() {
        llProductImagesContainer.removeAllViews();

        FrameLayout addImageFrame = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.add_image_thumbnail_placeholder, llProductImagesContainer, false);
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

        List<String> variantNames = new ArrayList<>();
        for (ConfiguredProductVariant variant : configuredProductVariants) {
            variantNames.add(variant.variantName);
        }
        tvClassificationValue.setText(android.text.TextUtils.join(", ", variantNames));

        long totalQuantity = 0;
        for (ConfiguredProductVariant variant : configuredProductVariants) {
            totalQuantity += variant.quantityInStock;
        }

        ConfiguredProductVariant firstVariant = configuredProductVariants.get(0);
        double priceToShow = (firstVariant.salePrice > 0 && firstVariant.salePrice < firstVariant.originPrice) ? firstVariant.salePrice : firstVariant.originPrice;

        tvPriceValue.setText(String.format("%,.0f VNĐ", priceToShow));
        tvInventoryValue.setText(totalQuantity + " sản phẩm");
    }

    private AddNewProductRequest collectAddNewProductData(List<String> imageUrls) {
        String productName = Objects.requireNonNull(etProductName.getText()).toString().trim();
        String description = Objects.requireNonNull(etProductDescription.getText()).toString().trim();
        if (productName.isEmpty() || description.isEmpty() || selectedCategoryId == 0 || configuredProductVariants.isEmpty() || imageUrls.isEmpty()) {
            return null;
        }

        double listPrice = 0.0;
        double salePrice = 0.0;

        List<AddProductVariantsRequest> addProductVariantsRequests = new ArrayList<>();
        if (!configuredProductVariants.isEmpty()) {
            // THAY ĐỔI: Lấy giá từ biến thể đầu tiên để đưa vào request chính
            listPrice = configuredProductVariants.get(0).originPrice;
            salePrice = configuredProductVariants.get(0).salePrice;

            for (ConfiguredProductVariant cpv : configuredProductVariants) {
                addProductVariantsRequests.add(new AddProductVariantsRequest(
                        cpv.variantName,
                        cpv.originPrice,
                        cpv.salePrice,
                        cpv.quantityInStock,
                        0 // sold_amount mặc định là 0 khi thêm mới
                ));
            }
        }

        return new AddNewProductRequest(
                productName,
                description,
                selectedCategoryId,
                imageUrls,
                listPrice,
                salePrice,
                addProductVariantsRequests
        );
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
    }
}