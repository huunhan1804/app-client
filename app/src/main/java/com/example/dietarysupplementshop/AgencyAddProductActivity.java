package com.example.dietarysupplementshop;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.dietarysupplementshop.model.Category;
import com.example.dietarysupplementshop.requests.AddNewProductRequest;
import com.example.dietarysupplementshop.requests.AddProductVariantsRequest;
import com.example.dietarysupplementshop.requests.UpdateProductRequest;
import com.example.dietarysupplementshop.requests.UpdateProductVariantRequest;
import com.example.dietarysupplementshop.responses.ProductFullDTO;
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
    private List<Category> availableCategories = new ArrayList<>(); // Danh sách danh mục động

    private List<Uri> selectedImageUris = new ArrayList<>();
    private ActivityResultLauncher<String> pickMultipleImagesLauncher;
    private AgencyAddProductViewModel viewModel;
    private ProductFullDTO productToEdit;
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

        initViews();
        initLaunchers();
        setupListeners();
        observeViewModel();
        viewModel.loadCategories();

        // GỌI HÀM NÀY ĐỂ LẤY DANH MỤC

        if (getIntent().hasExtra("product_id_to_edit")) {
            long productId = getIntent().getLongExtra("product_id_to_edit", -1);
            if (productId != -1) {
                viewModel.loadProductForEdit(productId);
            }
        } else {
            displaySelectedImages();
            updateButtonState();
        }
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
                        int currentImageCount = selectedImageUris.size();
                        for (Uri uri : uris) {
                            if (currentImageCount < MAX_IMAGE_COUNT) {
                                selectedImageUris.add(uri);
                                currentImageCount++;
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
        btnDisplay.setOnClickListener(v -> uploadImagesAndSubmit());
    }

    private void showCategorySelectionDialog() {
        // **SỬA LỖI**: Logic hiển thị hộp thoại phải được gọi khi dữ liệu đã có sẵn.
        if (availableCategories != null && !availableCategories.isEmpty()) {
            showCategoryDialog(availableCategories);
        } else {
            Toast.makeText(this, "Đang tải danh mục...", Toast.LENGTH_SHORT).show();
            // Không cần return nữa, vì observeViewModel sẽ xử lý
        }
    }

    private void showCategoryDialog(List<Category> categories) {
        String[] categoryNames = categories.stream()
                .map(Category::getCategory_name)
                .toArray(String[]::new);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn danh mục");
        builder.setItems(categoryNames, (dialog, which) -> {
            Category selectedCategory = categories.get(which);
            tvCategoryValue.setText(selectedCategory.getCategory_name());
            selectedCategoryId = selectedCategory.getCategory_id();
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
            ConfiguredProductVariant newVariant = new ConfiguredProductVariant("", 0.0, 0.0, 0, null);
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
                    salePrice = salePriceStr.isEmpty() ? 0.0 : Double.parseDouble(salePriceStr);
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

                Long variantId = (i < configuredProductVariants.size()) ? configuredProductVariants.get(i).variantId : null;
                int soldAmount = (i < configuredProductVariants.size()) ? configuredProductVariants.get(i).soldAmount : 0;

                tempConfiguredVariants.add(new ConfiguredProductVariant(variantName, originPrice, salePrice, quantity, variantId, soldAmount));
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
        if (etVariantName != null) {
            etVariantName.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etVariantName, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private static class ConfiguredProductVariant {
        public String variantName;
        public double originPrice = 0.0;
        public double salePrice = 0.0;
        public int quantityInStock = 0;
        public Long variantId;
        public int soldAmount;

        public ConfiguredProductVariant(String variantName, double originPrice, double salePrice, int quantityInStock, Long variantId) {
            this.variantName = variantName;
            this.originPrice = originPrice;
            this.salePrice = salePrice;
            this.quantityInStock = quantityInStock;
            this.variantId = variantId;
            this.soldAmount = 0;
        }

        public ConfiguredProductVariant(String variantName, double originPrice, double salePrice, int quantityInStock, Long variantId, int soldAmount) {
            this.variantName = variantName;
            this.originPrice = originPrice;
            this.salePrice = salePrice;
            this.quantityInStock = quantityInStock;
            this.variantId = variantId;
            this.soldAmount = soldAmount;
        }
    }

    private void loadProductDataForEdit(ProductFullDTO product) {
        if (product != null) {
            productToEdit = product;
            if (toolbarTitleTextView != null) {
                toolbarTitleTextView.setText(getString(R.string.edit_product_title));
            }

            etProductName.setText(product.getProduct_name());
            etProductDescription.setText(product.getProduct_description());

            Category category = product.getCategory();
            if (category != null && category.getCategory_name() != null) {
                tvCategoryValue.setText(category.getCategory_name());
                selectedCategoryId = category.getCategory_id();
            } else {
                tvCategoryValue.setText(getString(R.string.select_category_value));
                selectedCategoryId = 0;
            }

            configuredProductVariants.clear();

            if (product.getProduct_variant_list() != null && !product.getProduct_variant_list().isEmpty()) {
                for (ProductVariantDTO variantDTO : product.getProduct_variant_list()) {
                    double originPrice = 0.0;
                    double salePrice = 0.0;
                    if (variantDTO.getOrigin_price() != null) {
                        originPrice = Double.parseDouble(variantDTO.getOrigin_price());
                    }
                    if (variantDTO.getSale_price() != null) {
                        salePrice = Double.parseDouble(variantDTO.getSale_price());
                    }

                    configuredProductVariants.add(new ConfiguredProductVariant(
                            variantDTO.getProduct_variant_name(),
                            originPrice,
                            salePrice,
                            variantDTO.getQuantity_in_stock(),
                            variantDTO.getProduct_variant_id(),
                            variantDTO.getSold_amount()
                    ));
                }
            } else {
                double price = 0.0;
                if (product.getProduct_price() != null) {
                    try {
                        String priceString = product.getProduct_price().replaceAll("[^\\d,]", "").replace(",", ".");
                        price = Double.parseDouble(priceString);
                    } catch (NumberFormatException e) {
                        Log.e("ParseError", "Could not parse price: " + product.getProduct_price(), e);
                    }
                }

                configuredProductVariants.add(new ConfiguredProductVariant(
                        "",
                        price,
                        0.0,
                        product.getQuantity_in_stock(),
                        null,
                        product.getSold_amount()
                ));

            }

            updateVariantSummaryUI();

            if (product.getMedia_url() != null && !product.getMedia_url().isEmpty()) {
                selectedImageUris.clear();
                for (String url : product.getMedia_url()) {
                    try {
                        selectedImageUris.add(Uri.parse(url));
                    } catch (Exception e) {
                        Log.e("URI_PARSE_ERROR", "Invalid URL: " + url, e);
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

        if (selectedImageUris.size() < MAX_IMAGE_COUNT) {
            llProductImagesContainer.addView(addImageFrame);
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

        if (productName.isEmpty() || description.isEmpty() || selectedCategoryId == 0 || imageUrls.isEmpty() || configuredProductVariants.isEmpty()) {
            return null;
        }

        int totalQuantity = configuredProductVariants.stream().mapToInt(v -> v.quantityInStock).sum();

        List<AddProductVariantsRequest> addProductVariantsRequests = new ArrayList<>();
        for (ConfiguredProductVariant cpv : configuredProductVariants) {
            addProductVariantsRequests.add(new AddProductVariantsRequest(
                    cpv.variantName, cpv.originPrice, cpv.salePrice, cpv.quantityInStock, cpv.soldAmount));
        }

        return new AddNewProductRequest(
                productName,
                description,
                selectedCategoryId,
                totalQuantity,
                imageUrls,
                addProductVariantsRequests
        );
    }

    private UpdateProductRequest collectUpdateProductData(List<String> imageUrls) {
        String productName = Objects.requireNonNull(etProductName.getText()).toString().trim();
        String description = Objects.requireNonNull(etProductDescription.getText()).toString().trim();

        if (productName.isEmpty() || description.isEmpty() || selectedCategoryId == 0 || imageUrls.isEmpty() || configuredProductVariants.isEmpty()) {
            return null;
        }

        List<UpdateProductVariantRequest> updateProductVariantRequests = new ArrayList<>();
        for (ConfiguredProductVariant cpv : configuredProductVariants) {
            updateProductVariantRequests.add(new UpdateProductVariantRequest(
                    cpv.variantId, cpv.variantName, cpv.originPrice, cpv.salePrice, cpv.quantityInStock, cpv.soldAmount));
        }

        UpdateProductRequest request = new UpdateProductRequest();
        request.setProduct_id(productToEdit.getProduct_id());
        request.setProduct_name(productName);
        request.setProduct_description(description);
        request.setCategory_id(selectedCategoryId);
        request.setImage_urls(imageUrls);
        request.setProduct_variant_list(updateProductVariantRequests);

        return request;
    }


    private void observeViewModel() {
        viewModel.isProcessing.observe(this, isLoading -> {
            btnSave.setEnabled(!isLoading);
            btnDisplay.setEnabled(!isLoading);
        });

        viewModel.actionSuccessful.observe(this, success -> {
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
        viewModel.categories.observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                availableCategories.clear();
                availableCategories.addAll(categories);
            } else {
                Toast.makeText(this, "Không thể tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });


        viewModel.productToEdit.observe(this, product -> {
            if (product != null) {
                loadProductDataForEdit(product);
            }
        });
    }

}