package com.example.dietarysupplementshop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.adapter.CustomFilterAdapter;
import com.example.dietarysupplementshop.interfaces.FilterDialogListener;
import com.example.dietarysupplementshop.model.FilterItem;

import java.util.ArrayList;
import java.util.List;

public class FilterDialogFragment extends DialogFragment {

    private List<FilterItem> filterCategoryList;
    private List<FilterItem> filterSortByList;
    private List<FilterItem> filterPriceList;
    private RecyclerView rcvCategoriesFilter;
    private RecyclerView rcvSortBy;
    private RecyclerView rcvPrice;
    private CustomFilterAdapter categoryFilterAdapter;
    private CustomFilterAdapter sortByFilterAdapter;
    private CustomFilterAdapter priceFilterAdapter;

    private FilterDialogListener filterDialogListener;


    private List<FilterItem> selectedItems = new ArrayList<>();

    public void setFilterDialogListener(FilterDialogListener listener) {
        this.filterDialogListener = listener;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_filter_condition, null);

        rcvCategoriesFilter = view.findViewById(R.id.categoryFilterRecyclerView);
        rcvSortBy = view.findViewById(R.id.sortByRecyclerView);
        rcvPrice = view.findViewById(R.id.pricesRecyclerView);

        filterCategoryList = getCategoryFilter();
        filterSortByList = getSortByFilter();
        filterPriceList = getPriceFilter();

        categoryFilterAdapter = new CustomFilterAdapter(filterCategoryList);
        sortByFilterAdapter = new CustomFilterAdapter(filterSortByList);
        priceFilterAdapter = new CustomFilterAdapter(filterPriceList);

        rcvCategoriesFilter.setLayoutManager(new GridLayoutManager(getContext(), 3));


        rcvSortBy.setAdapter(sortByFilterAdapter);
        rcvSortBy.setLayoutManager(new LinearLayoutManager(getContext()));

        rcvPrice.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rcvPrice.setAdapter(priceFilterAdapter);

        rcvCategoriesFilter.setAdapter(categoryFilterAdapter);
        rcvCategoriesFilter.setLayoutManager(new LinearLayoutManager(getContext()));
        setupAutoSpanCount(rcvCategoriesFilter, categoryFilterAdapter);

        rcvSortBy.setAdapter(sortByFilterAdapter);
        rcvSortBy.setLayoutManager(new LinearLayoutManager(getContext()));
        setupAutoSpanCount(rcvSortBy, sortByFilterAdapter);

        rcvPrice.setAdapter(priceFilterAdapter);
        rcvPrice.setLayoutManager(new LinearLayoutManager(getContext()));
        setupAutoSpanCount(rcvPrice, priceFilterAdapter);

        builder.setView(view)
                .setPositiveButton("Apply", (dialog, which) -> {
                    selectedItems.clear();
                    for (FilterItem item : filterCategoryList) {
                        if (item.isChecked()) {
                            selectedItems.add(item);
                        }
                    }

                    for (FilterItem item : filterSortByList) {
                        if (item.isChecked()) {
                            selectedItems.add(item);
                        }
                    }

                    for (FilterItem item : filterPriceList) {
                        if (item.isChecked()) {
                            selectedItems.add(item);
                        }
                    }

                    if (filterDialogListener != null) {
                        filterDialogListener.onFiltersApplied(selectedItems);
                    }

                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    selectedItems.clear();
                    filterDialogListener.onFiltersApplied(selectedItems);
                });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        // safety check
        if (getDialog() == null)
            return;

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private List<FilterItem> getCategoryFilter(){
        List<FilterItem> filterItems = new ArrayList<>();
        filterItems.add(new FilterItem("Digestion", false));
        filterItems.add(new FilterItem("Digestion", false));
        filterItems.add(new FilterItem("Digestion", false));
        filterItems.add(new FilterItem("Digestion", false));
        filterItems.add(new FilterItem("Digestion", false));
        filterItems.add(new FilterItem("Digestion", false));
        return filterItems;
    }
    private List<FilterItem> getSortByFilter(){
        List<FilterItem> filterItems = new ArrayList<>();
        filterItems.add(new FilterItem("Price: High to Low", false));
        filterItems.add(new FilterItem("Price: Low to High", false));
        filterItems.add(new FilterItem("Sold the most", false));
        return filterItems;
    }
    private List<FilterItem> getPriceFilter(){
        List<FilterItem> filterItems = new ArrayList<>();
        filterItems.add(new FilterItem("0 đ - 200.000 đ", false));
        filterItems.add(new FilterItem("200.000 đ - 500.000 đ", false));
        filterItems.add(new FilterItem("500.000 đ - 1.000.000 đ", false));
        filterItems.add(new FilterItem("> 1.000.000 đ", false));
        return filterItems;
    }

    private int getItemWidth(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null || adapter.getItemCount() == 0) {
            return 0;
        }

        // Lấy chiều rộng của parent layout
        int parentWidth = recyclerView.getWidth();

        // Lấy số lượng item trong một hàng (cố định hoặc dựa trên độ dài mỗi item)
        int itemsPerRow = 1; // Đặt số lượng item trên một hàng mặc định là 1
        int totalItemWidth = 0;
        for (int i = 0; i < adapter.getItemCount(); i++) {
            View itemView = recyclerView.getLayoutManager().findViewByPosition(i);
            if (itemView != null) {
                totalItemWidth += itemView.getWidth();
                if (totalItemWidth >= parentWidth) {
                    break; // Đủ chiều rộng cho một hàng
                }
                itemsPerRow++;
            }
        }

        // Tính toán spanCount dựa trên số lượng item trên một hàng
        int spanCount = Math.max(1, itemsPerRow);
        return spanCount;
    }

    private void setupAutoSpanCount(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Loại bỏ lắng nghe để không gọi lại khi không cần thiết
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int spanCount = getItemWidth(recyclerView);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
            }
        });
    }


}