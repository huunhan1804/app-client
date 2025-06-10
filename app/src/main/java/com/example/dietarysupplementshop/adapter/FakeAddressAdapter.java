package com.example.dietarysupplementshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.model.Address;

import java.util.List;

public class FakeAddressAdapter  extends RecyclerView.Adapter<FakeAddressAdapter.FakeAddressViewHolder>{
    private final List<Address> addresses;
    public FakeAddressAdapter(List<Address> addresses) {
        this.addresses = addresses;
    }


    @NonNull
    @Override
    public FakeAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_fake, parent, false);
        return new FakeAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FakeAddressViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (addresses != null){
            return addresses.size();
        }
        return 0;
    }

    public static class FakeAddressViewHolder extends RecyclerView.ViewHolder {
        public FakeAddressViewHolder(@NonNull View view){
            super(view);
        }
    }
}
