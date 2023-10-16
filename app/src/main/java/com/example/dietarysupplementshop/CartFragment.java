package com.example.dietarysupplementshop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.adapter.CartItemAdapter;
import com.example.dietarysupplementshop.adapter.ProductAdapter;
import com.example.dietarysupplementshop.model.CartItem;
import com.example.dietarysupplementshop.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
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

    RelativeLayout EmptyCartItem;
    RelativeLayout HaveCartItem;
    RecyclerView cartRecyclerView;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rcvRelatedProduct = view.findViewById(R.id.recyclerView);
        relatedProduct = new ArrayList<>();
        relatedProduct.add(new Product(1, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        relatedProduct.add(new Product(2, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        relatedProduct.add(new Product(3, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        relatedProduct.add(new Product(4, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        relatedProduct.add(new Product(5, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));
        relatedProduct.add(new Product(6, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫", 0));

        productAdapter = new ProductAdapter(relatedProduct, getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcvRelatedProduct.setLayoutManager(gridLayoutManager);
        rcvRelatedProduct.setAdapter(productAdapter);

        cartRecyclerView = view.findViewById(R.id.cartItem);
        cartItemList = new ArrayList<>();
        cartItemList.add(new CartItem(1, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫"));
        cartItemList.add(new CartItem(2, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫"));
        cartItemList.add(new CartItem(3, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫"));
        cartItemList.add(new CartItem(4, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫"));
        cartItemList.add(new CartItem(5, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫"));
        cartItemList.add(new CartItem(6, "https://dl.dropbox.com/s/t0tjm1ase3p9uj0/OIP.jpg?dl=0", "Cốm Tăng Cân Bạch Mai", "250.000 ₫"));
        cartItemAdapter = new CartItemAdapter(cartItemList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        cartRecyclerView.setLayoutManager(linearLayoutManager);
        cartRecyclerView.setAdapter(cartItemAdapter);

        cartItemAdapter.setOnItemCheckedListener(new CartItemAdapter.OnItemCheckedListener() {
            @Override
            public void onItemChecked(CartItem product, boolean isChecked) {
                // Xử lý sự kiện khi người dùng chọn/deselect sản phẩm
                product.setSelected(isChecked);

                // Cập nhật tổng tiền
                updateTotalPrice();

                // Kiểm tra và cập nhật giao diện dựa trên sự có mặt của sản phẩm trong giỏ hàng
                updateUIBasedOnCartItems();
            }
        });
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView);
        totalItemText = view.findViewById(R.id.totalItemText);
        shippingFeeValue = view.findViewById(R.id.shippingFeeValue);
        updateTotalPrice();

        EmptyCartItem = view.findViewById(R.id.EmptyCartItem);
        HaveCartItem = view.findViewById(R.id.HaveCartItem);
        updateUIBasedOnCartItems();


        return view;
    }

    private void updateTotalPrice() {
        String shippingFee = shippingFeeValue.getText().toString().replaceAll("[^\\d]+", "");
        double shippingFeeValue = Double.parseDouble(shippingFee);
        double total = shippingFeeValue;
        for (CartItem product : cartItemList) {
            if (product.isSelected()) {
                String priceCleaned = product.getProductPrice().replaceAll("[^\\d]+", "");
                double priceValue = Double.parseDouble(priceCleaned);
                total += priceValue * product.getQuantity();
            }
        }
        totalItemText.setText("Product in cart: "+ cartItemList.size()  + " items");
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        totalPriceTextView.setText("Total: " +decimalFormat.format(total));
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
}