package com.example.dietarysupplementshop;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast; // Thêm import cho Toast

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date; // Thêm import cho Date

public class SellerOverviewProductsFragment extends Fragment {

    private Calendar startCalendar, endCalendar;

    public SellerOverviewProductsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller_overview_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        TextView tvSelectedTimeFrameProducts = view.findViewById(R.id.tv_selected_time_frame_products);
        tvSelectedTimeFrameProducts.setOnClickListener(v -> showTimeFrameSelectionDialog(tvSelectedTimeFrameProducts));

        if (startCalendar == null) startCalendar = Calendar.getInstance();
        if (endCalendar == null) endCalendar = Calendar.getInstance();

        tvSelectedTimeFrameProducts.setText("7 ngày qua");
    }

    private void showTimeFrameSelectionDialog(final TextView targetTextView) {
        final String[] options = {"Tất cả", "Hôm nay", "Hôm qua", "7 ngày qua", "30 ngày qua", "Tùy chỉnh..."};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn khung thời gian");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedOption = options[which];
                targetTextView.setText(selectedOption);

                switch (selectedOption) {
                    case "Tất cả":
                        loadDataForSelectedTimeFrame("all");
                        break;
                    case "Hôm nay":
                        loadDataForSelectedTimeFrame("today");
                        break;
                    case "Hôm qua":
                        loadDataForSelectedTimeFrame("yesterday");
                        break;
                    case "7 ngày qua":
                        loadDataForSelectedTimeFrame("last7days");
                        break;
                    case "30 ngày qua":
                        loadDataForSelectedTimeFrame("last30days");
                        break;
                    case "Tùy chỉnh...":
                        showCustomDateRangePicker(targetTextView);
                        break;
                }
            }
        });
        builder.show();
    }

    private void showCustomDateRangePicker(final TextView targetTextView) {
        DatePickerDialog startDatePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    startCalendar.set(year, month, dayOfMonth);
                    DatePickerDialog endDatePickerDialog = new DatePickerDialog(getContext(),
                            (view1, year1, month1, dayOfMonth1) -> {
                                endCalendar.set(year1, month1, dayOfMonth1);

                                if (endCalendar.before(startCalendar)) {
                                    Toast.makeText(getContext(), "Ngày kết thúc phải sau ngày bắt đầu", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String dateRange = "Từ " + sdf.format(startCalendar.getTime()) + " đến " + sdf.format(endCalendar.getTime());
                                targetTextView.setText(dateRange);

                                loadDataForCustomRange(startCalendar.getTime(), endCalendar.getTime());

                            }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH));
                    endDatePickerDialog.setTitle("Chọn ngày kết thúc");
                    endDatePickerDialog.show();
                }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH));
        startDatePickerDialog.setTitle("Chọn ngày bắt đầu");
        startDatePickerDialog.show();
    }

    private void loadDataForSelectedTimeFrame(String timeFrame) {
        Toast.makeText(getContext(), "Tải dữ liệu sản phẩm cho: " + timeFrame, Toast.LENGTH_SHORT).show();
        // TextView tvTotalProducts = getView().findViewById(R.id.tv_total_products);
        // if (tvTotalProducts != null) {
        //     tvTotalProducts.setText("Dữ liệu mới dựa trên " + timeFrame);
        // }
    }

    private void loadDataForCustomRange(Date startDate, Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Toast.makeText(getContext(), "Tải dữ liệu sản phẩm từ " + sdf.format(startDate) + " đến " + sdf.format(endDate), Toast.LENGTH_SHORT).show();
    }
}