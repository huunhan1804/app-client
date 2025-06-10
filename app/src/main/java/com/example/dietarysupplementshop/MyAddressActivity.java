package com.example.dietarysupplementshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dietarysupplementshop.adapter.AddressAdapter;
import com.example.dietarysupplementshop.adapter.FakeAddressAdapter;
import com.example.dietarysupplementshop.model.Address;
import com.example.dietarysupplementshop.model.Product;
import com.example.dietarysupplementshop.viewModel.AccountViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyAddressActivity extends AppCompatActivity {

    private  RecyclerView rcv_address;
    private List<Address> addressList;

    private AddressAdapter addressAdapter;
    private AccountViewModel accountViewModel;

    private FrameLayout frameLayout;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        accountViewModel = MyApplication.getInstance().getAccountViewModel();

        rcv_address = findViewById(R.id.rcv_address);

        FakeAddressAdapter fakeAddressAdapter = new FakeAddressAdapter(createFakeAddress(10));
        rcv_address.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcv_address.setAdapter(fakeAddressAdapter);

        accountViewModel.getAddressListResource().observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case SUCCESS:
                        if (resource.getData() != null) {
                            addressList = resource.getData();
                            addressAdapter = new AddressAdapter(this, getApplicationContext(), addressList);
                            rcv_address.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            rcv_address.setAdapter(addressAdapter);
                        }
                        break;
                    case ERROR:
                        Toast.makeText(this, resource.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                    case LOADING:
                        break;
                }
            }
        });



        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        Button btnAdd = findViewById(R.id.btn_add_address);
        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddressInfoActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void showProgressBar() {
        frameLayout = findViewById(R.id.frameLoading);
        animationView = findViewById(R.id.animationView);
        frameLayout.setVisibility(View.VISIBLE);
        animationView.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        frameLayout = findViewById(R.id.frameLoading);
        animationView = findViewById(R.id.animationView);
        frameLayout.setVisibility(View.GONE);
        animationView.setVisibility(View.GONE);
    }

    public List<Address> createFakeAddress(int count) {
        List<Address> fakeList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            fakeList.add(new Address());
        }
        return fakeList;
    }
}