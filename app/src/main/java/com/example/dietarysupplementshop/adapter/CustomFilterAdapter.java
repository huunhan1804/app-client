package com.example.dietarysupplementshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.R;
import com.example.dietarysupplementshop.model.FilterItem;

import java.util.List;

public class CustomFilterAdapter extends RecyclerView.Adapter<CustomFilterAdapter.ViewHolder> {

    private List<FilterItem> filterItems;

    public CustomFilterAdapter(List<FilterItem> filterItems) {
        this.filterItems = filterItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FilterItem item = filterItems.get(position);
        holder.textView.setText(item.getName());
        holder.checkBox.setChecked(item.isChecked());

        int itemViewWidth = holder.itemView.getWidth();

        // Xử lý sự kiện chọn/bỏ chọn
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setChecked(!item.isChecked());
                holder.checkBox.setChecked(item.isChecked());

                // Cập nhật trạng thái ImageView tùy chỉnh dựa trên isChecked()
                if (item.isChecked()) {
                    holder.customImageView.setImageResource(R.drawable.approved_ic);
                } else {
                    holder.customImageView.setImageResource(R.drawable.unchecked_ic);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public ImageView customImageView;
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkBox);
            customImageView = view.findViewById(R.id.customImageView);
            textView = view.findViewById(R.id.textView);
        }
    }
}

