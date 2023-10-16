package com.example.dietarysupplementshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.dietarysupplementshop.adapter.AddressAdapter;
import com.example.dietarysupplementshop.model.Address;

import java.util.ArrayList;
import java.util.List;

public class MyAddressActivity extends AppCompatActivity {

    private  RecyclerView rcv_address;
    private List<Address> addressList;

    private AddressAdapter addressAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        rcv_address = findViewById(R.id.rcv_address);
        addressList = new ArrayList<>();
        addressList.add(new Address(1, "Trần Quang Quí", "0945605514", "Ấp Phong Lưu, Xã Tân Hưng, Huyện Cái Nước, Tỉnh Cà Mau", true));
        addressList.add(new Address(1, "Trần Quang Quí", "0945605514", "Ấp Phong Lưu, Xã Tân Hưng, Huyện Cái Nước, Tỉnh Cà Mau", false));
        addressList.add(new Address(1, "Trần Quang Quí", "0945605514", "Ấp Phong Lưu, Xã Tân Hưng, Huyện Cái Nước, Tỉnh Cà Mau", false));
        addressList.add(new Address(1, "Trần Quang Quí", "0945605514", "Ấp Phong Lưu, Xã Tân Hưng, Huyện Cái Nước, Tỉnh Cà Mau", false));
        addressList.add(new Address(1, "Trần Quang Quí", "0945605514", "Ấp Phong Lưu, Xã Tân Hưng, Huyện Cái Nước, Tỉnh Cà Mau", false));
        addressList.add(new Address(1, "Trần Quang Quí", "0945605514", "Ấp Phong Lưu, Xã Tân Hưng, Huyện Cái Nước, Tỉnh Cà Mau", false));
        addressList.add(new Address(1, "Trần Quang Quí", "0945605514", "Ấp Phong Lưu, Xã Tân Hưng, Huyện Cái Nước, Tỉnh Cà Mau", false));

        addressAdapter = new AddressAdapter(getApplicationContext(), addressList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcv_address.setLayoutManager(linearLayoutManager);
        rcv_address.setAdapter(addressAdapter);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}