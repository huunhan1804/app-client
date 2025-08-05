package com.example.dietarysupplementshop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dietarysupplementshop.adapter.OrderAgencyAdapter;
import com.example.dietarysupplementshop.model.Order;
import com.example.dietarysupplementshop.viewModel.AgencyOrderViewModel;

import java.util.ArrayList;

public class AgencyOrderFragment extends Fragment implements OrderAgencyAdapter.OnOrderActionButtonClickListener {

    private static final String ARG_STATUS = "status";
    private String status;
    private AgencyOrderViewModel agencyOrderViewModel;
    private RecyclerView recyclerView;
    private OrderAgencyAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyTextView;

    public static AgencyOrderFragment newInstance(String status) {
        AgencyOrderFragment fragment = new AgencyOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getString(ARG_STATUS);
        }
        agencyOrderViewModel = new ViewModelProvider(this).get(AgencyOrderViewModel.class);
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewOrders);
        emptyTextView = view.findViewById(R.id.emptyStateTextView);
        progressBar = view.findViewById(R.id.progressBar);

        setupRecyclerView();
        observeViewModel();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAgencyAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
    }

    private void observeViewModel() {
        agencyOrderViewModel.getAgencyOrders(status).observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        emptyTextView.setVisibility(View.GONE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        if (resource.getData() != null && !resource.getData().isEmpty()) {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyTextView.setVisibility(View.GONE);
                            adapter.setOrderList(resource.getData());
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            emptyTextView.setVisibility(View.VISIBLE);
                            emptyTextView.setText("Không có đơn hàng nào.");
                        }
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        emptyTextView.setVisibility(View.VISIBLE);
                        emptyTextView.setText("Lỗi: " + resource.getMessage());
                        Toast.makeText(getContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        // Lắng nghe các kết quả từ các hành động của người bán
        agencyOrderViewModel.getAgencyOrders(status).observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case SUCCESS:
                        Toast.makeText(getContext(), "Cập nhật trạng thái đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                        agencyOrderViewModel.getAgencyOrders(status);
                        break;
                    case ERROR:
                        Toast.makeText(getContext(), "Lỗi: " + resource.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onActionButton1Click(Order order, int position) {
        // Xử lý nút Hành động 1 dựa trên trạng thái đơn hàng
        switch (order.getOrder_status()) {
            case "PENDING":
                showConfirmDialog("Xác nhận đơn hàng", "Bạn muốn xác nhận đơn hàng #" + order.getOrder_id() + " này?",
                        () -> agencyOrderViewModel.confirmOrder(order.getOrder_id()));
                break;
            case "SHIPPING":
            case "DELIVERED":
            case "CANCELLED":
            case "DELIVERY FAILED":
                // Đối với các trạng thái này, nút 1 là "Xem chi tiết"
                Toast.makeText(getContext(), "Xem chi tiết đơn: " + order.getOrder_id(), Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(getActivity(), AgencyOrderDetailActivity.class);
                // intent.putExtra("orderId", order.getOrder_id());
                // startActivity(intent);
                break;
            case "RETURNED":
                Toast.makeText(getContext(), "Xem yêu cầu trả hàng/hoàn tiền của đơn: " + order.getOrder_id(), Toast.LENGTH_SHORT).show();
                // Có thể mở một dialog hoặc activity mới để hiển thị chi tiết yêu cầu trả hàng
                break;
        }
    }

    @Override
    public void onActionButton2Click(Order order, int position) {
        // Xử lý nút Hành động 2 dựa trên trạng thái đơn hàng
        switch (order.getOrder_status()) {
            case "PENDING":
                showConfirmDialog("Hủy đơn hàng", "Bạn có chắc chắn muốn hủy đơn hàng #" + order.getOrder_id() + " này?",
                        () -> agencyOrderViewModel.cancelOrderByAgency(order.getOrder_id()));
                break;
            case "SHIPPING":
                showConfirmDialog("Xác nhận đã giao hàng", "Bạn xác nhận đơn hàng #" + order.getOrder_id() + " đã được giao thành công?",
                        () -> agencyOrderViewModel.markOrderAsDeliveredByAgency(order.getOrder_id()));
                break;
            case "RETURNED":
                showReturnRefundDialog(order);
                break;
        }
    }

    @Override
    public void onItemClick(Order order, int position) {
        Toast.makeText(getContext(), "Xem chi tiết đơn: " + order.getOrder_id(), Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(getActivity(), AgencyOrderDetailActivity.class);
        // intent.putExtra("orderId", order.getOrder_id());
        // startActivity(intent);
    }

    private void showConfirmDialog(String title, String message, Runnable onConfirm) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Có", (dialog, which) -> onConfirm.run())
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showReturnRefundDialog(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xử lý yêu cầu Trả hàng/Hoàn tiền");
        builder.setMessage("Bạn muốn Đồng ý hoàn tiền hay Từ chối yêu cầu của đơn hàng #" + order.getOrder_id() + "?");

        builder.setPositiveButton("Đồng ý", (dialog, which) -> {
            agencyOrderViewModel.approveReturnRefund(order.getOrder_id());
        });

        builder.setNegativeButton("Từ chối", (dialog, which) -> {
            // Hiển thị dialog nhập lý do từ chối
            showRejectReasonDialog(order);
        });

        builder.setNeutralButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showRejectReasonDialog(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Lý do từ chối");
        final EditText reasonInput = new EditText(getContext());
        reasonInput.setHint("Nhập lý do từ chối trả hàng/hoàn tiền...");
        builder.setView(reasonInput);

        builder.setPositiveButton("Gửi", (dialog, which) -> {
            String reason = reasonInput.getText().toString().trim();
            if (reason.isEmpty()) {
                Toast.makeText(getContext(), "Lý do không được để trống.", Toast.LENGTH_SHORT).show();
            } else {
                agencyOrderViewModel.rejectReturnRefund(order.getOrder_id(), reason);
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}