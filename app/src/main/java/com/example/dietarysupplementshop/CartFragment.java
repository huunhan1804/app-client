package com.example.dietarysupplementshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.adapter.CartItemAdapter;
import com.example.dietarysupplementshop.adapter.FakeProductAdapter;
import com.example.dietarysupplementshop.adapter.ProductAdapter;
import com.example.dietarysupplementshop.constant.Validation;
import com.example.dietarysupplementshop.model.CartItem;
import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.requests.AddToCartRequest;
import com.example.dietarysupplementshop.responses.AccountInformation;
import com.example.dietarysupplementshop.responses.CartInformation;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;
import com.example.dietarysupplementshop.viewModel.ProductViewModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    private ProductAdapter productAdapter;

    private CartItemAdapter cartItemAdapter;

    private List<CartItem> cartItemList;

    private TextView totalPriceTextView, totalItemText, shippingFeeValue;

    private List<Product> relatedProduct;

    private RecyclerView rcvRelatedProduct;

    private AccountViewModel accountViewModel;

    private ProductViewModel productViewModel;

    private RelativeLayout EmptyCartItem;
    private RelativeLayout HaveCartItem;
    private RecyclerView cartRecyclerView;
    private Button checkoutButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        accountViewModel = MyApplication.getInstance().getAccountViewModel();
        productViewModel =  MyApplication.getInstance().getProductViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        //Product related        ??
        rcvRelatedProduct = view.findViewById(R.id.recyclerView);
        FakeProductAdapter fakeProductAdapter = new FakeProductAdapter(productViewModel.createFakeProducts(10));
        rcvRelatedProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rcvRelatedProduct.setAdapter(fakeProductAdapter);
        productViewModel.getListRelatedProduct(1).observe(getViewLifecycleOwner(), products -> {
            relatedProduct = products;
            productAdapter = new ProductAdapter(relatedProduct, getContext());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            rcvRelatedProduct.setLayoutManager(gridLayoutManager);
            rcvRelatedProduct.setAdapter(productAdapter);
        });

        checkoutButton = view.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(view1 -> {
            List<CartItem> selectedItems = getSelectedCartItems();
            if (selectedItems.isEmpty()) {
                Toast.makeText(getContext(), "Please select items to checkout!", Toast.LENGTH_LONG).show();
                return;
            }
            Gson gson = new Gson();
            String selectedItemsJson = gson.toJson(selectedItems);
            Intent intent = new Intent(getContext(), CheckoutActivity.class);
            intent.putExtra("selectedItems", selectedItemsJson);
            startActivity(intent);
        });


        cartRecyclerView = view.findViewById(R.id.cartItem);
        accountViewModel.getAccountInfoResource().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case SUCCESS:
                        ((HomepageActivity) getActivity()).hideProgressBar();
                        if (resource.getData() != null) {
                            AccountInformation accountInformations = resource.getData();

                            cartItemList = accountInformations.getCart_info().getCartItem();
                            cartItemAdapter = new CartItemAdapter(cartItemList, getContext());

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            cartRecyclerView.setLayoutManager(linearLayoutManager);
                            cartRecyclerView.setAdapter(cartItemAdapter);

                            cartItemAdapter.setOnItemCheckedListener((product, isChecked) -> {
                                product.setSelected(isChecked);
                                updateTotalPrice();
                                updateUIBasedOnCartItems();
                            });

                            cartItemAdapter.setOnQuantityChangeListener(new CartItemAdapter.OnQuantityChangeListener() {
                                @Override
                                public void onIncreaseChange(CartItem product, int increaseBy) {
                                    accountViewModel.increaseCartItemQuantity(new AddToCartRequest(product.getProduct_info().getProduct_id(), product.getProduct_variant_info().getProduct_variant_id(), increaseBy));
                                    updateTotalPrice();
                                }

                                @Override
                                public void onDecreaseChange(CartItem product, int decreaseBy) {
                                    accountViewModel.decreaseCartItemQuantity(new AddToCartRequest(product.getProduct_info().getProduct_id(), product.getProduct_variant_info().getProduct_variant_id(), decreaseBy));
                                    updateTotalPrice();
                                }

                                @Override
                                public void onDelete(CartItem product) {
                                    accountViewModel.deleteCartItem(product.getCart_item_id());
                                    updateTotalPrice();
                                }
                            });

                            totalPriceTextView = view.findViewById(R.id.totalPriceTextView);
                            totalItemText = view.findViewById(R.id.totalItemText);
                            totalItemText.setText("Product in cart: "+ accountInformations.getCart_info().getTotal_item() + " items");

                            shippingFeeValue = view.findViewById(R.id.shippingFeeValue);
                            updateTotalPrice();

                            EmptyCartItem = view.findViewById(R.id.EmptyCartItem);
                            HaveCartItem = view.findViewById(R.id.HaveCartItem);
                            updateUIBasedOnCartItems();

                        }
                        break;
                    case ERROR:
                        ((HomepageActivity) getActivity()).hideProgressBar();
                        Toast.makeText(getContext(), resource.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                    case LOADING:
                        ((HomepageActivity) getActivity()).showProgressBar();
                        break;
                }
            }
        });

        return view;
    }

    private void updateTotalPrice() {
        String shippingFee = shippingFeeValue.getText().toString().replaceAll("[^\\d]+", "");
        double shippingFeeValue = Double.parseDouble(shippingFee);
        double total = shippingFeeValue;

        LinearLayout selectedItemsContainer = getView().findViewById(R.id.selectedItemsContainer);
        selectedItemsContainer.removeAllViews();

        for (CartItem product : cartItemList) {
            if (product.isSelected()) {
                String priceCleaned = product.getProduct_variant_info().getSale_price().replaceAll("[^\\d]+", "");
                double priceValue = Double.parseDouble(priceCleaned);
                total += priceValue * product.getQuantity();

                LinearLayout productItem = (LinearLayout) getLayoutInflater().inflate(R.layout.selected_product_item, null);

                TextView productName = productItem.findViewById(R.id.productNameTextView);
                TextView productQuantity = productItem.findViewById(R.id.productQuantityTextView);
                TextView productPrice = productItem.findViewById(R.id.productPriceTextView);

                productName.setText(product.getProduct_info().getProduct_name());
                productQuantity.setText(product.getQuantity() + " x");

                productPrice.setText(Validation.formatPriceToVND(priceValue * product.getQuantity()));
                selectedItemsContainer.addView(productItem);
            }
        }
        totalPriceTextView.setText(Validation.formatPriceToVND(total));
    }

    private void updateUIBasedOnCartItems() {
        if (cartItemList.isEmpty()) {
            HaveCartItem.setVisibility(View.GONE);
            EmptyCartItem.setVisibility(View.VISIBLE);
        } else {
            HaveCartItem.setVisibility(View.VISIBLE);
            EmptyCartItem.setVisibility(View.GONE);
        }
    }
    public List<CartItem> getSelectedCartItems() {
        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : cartItemList) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }


}