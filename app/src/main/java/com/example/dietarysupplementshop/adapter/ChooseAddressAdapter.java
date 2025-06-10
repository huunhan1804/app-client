package com.example.dietarysupplementshop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.AddressInfoActivity;
import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.model.Address;

import java.util.List;

public class ChooseAddressAdapter extends RecyclerView.Adapter<ChooseAddressAdapter.ChooseAddressViewHolder> {
    private List<Address> addressList;
    private Context context;

    private Activity activity;

    private int selectedPosition = -1;

    private OnAddressSelectedListener onAddressSelectedListener;


    public ChooseAddressAdapter(Activity activity, Context context, List<Address> addressList, OnAddressSelectedListener onAddressSelectedListener) {
        this.activity = activity;
        this.context = context;
        this.addressList = addressList;
        this.onAddressSelectedListener = onAddressSelectedListener;
    }

    public interface OnAddressSelectedListener {
        void onAddressSelected(Address address);
    }

    public void setData(List<Address> newAddressList) {
        this.addressList = newAddressList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChooseAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_choose_address, parent, false);
        return new ChooseAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseAddressViewHolder holder, int position) {
        Address address = addressList.get(position);
        if (address != null) {
            holder.fullnameTextView.setText(address.getFullname());
            holder.phoneTextView.setText(address.getPhone());

            String addressInfo = address.getAddress_detail();
            if (addressInfo != null && !addressInfo.isEmpty()) {
                String[] parts = addressInfo.split(",", 2);
                if (parts.length > 1) {
                    holder.detailAddressTextView.setText(parts[0].trim());
                    holder.addressTextView.setText(parts[1].trim());
                } else {
                    holder.detailAddressTextView.setVisibility(View.GONE);
                    holder.addressTextView.setText(addressInfo);
                }
            } else {
                holder.detailAddressTextView.setText("");
                holder.addressTextView.setText("");
            }

            if (address.getIs_default()) {
                holder.defaultAddressTextView.setVisibility(View.VISIBLE);
            } else {
                holder.defaultAddressTextView.setVisibility(View.GONE);
            }

            holder.editAddressButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddressInfoActivity.class);
                intent.putExtra("addressId", address.getAddress_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                activity.finish();
            });


            holder.radioSelectAddress.setChecked(position == selectedPosition);
            holder.radioSelectAddress.setOnClickListener(view -> {
                selectedPosition = position;
                onAddressSelectedListener.onAddressSelected(address);
            });

        }
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public Address getSelectedAddress() {
        if (selectedPosition != -1 && selectedPosition < addressList.size()) {
            return addressList.get(selectedPosition);
        }
        return null;
    }


    public static class ChooseAddressViewHolder extends RecyclerView.ViewHolder {

        TextView fullnameTextView, phoneTextView, detailAddressTextView, addressTextView, defaultAddressTextView;
        ImageButton editAddressButton;
        RadioButton radioSelectAddress;

        public ChooseAddressViewHolder(@NonNull View itemView) {
            super(itemView);
            fullnameTextView = itemView.findViewById(R.id.fullnameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            detailAddressTextView = itemView.findViewById(R.id.detailAddressTextView);
            addressTextView = itemView.findViewById(R.id.AddressTextView);
            defaultAddressTextView = itemView.findViewById(R.id.defaultAddressTextView);
            editAddressButton = itemView.findViewById(R.id.editAddressButton);
            radioSelectAddress = itemView.findViewById(R.id.radioSelectAddress);
        }
    }
}

