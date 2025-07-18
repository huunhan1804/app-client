package com.example.dietarysupplementshop.services;

import com.example.dietarysupplementshop.model.ProductSeller;
import com.example.dietarysupplementshop.requests.AddProductRequest;
import com.example.dietarysupplementshop.requests.ProductVariantRequest; // Import lớp request biến thể mới

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductApiService {

    private static ProductApiService instance;
    private final List<ProductSeller> mockProducts;
    private final ExecutorService executorService;

    private ProductApiService() {
        mockProducts = new ArrayList<>();
        executorService = Executors.newSingleThreadExecutor();
        generateMockProducts();
    }

    public static synchronized ProductApiService getInstance() {
        if (instance == null) {
            instance = new ProductApiService();
        }
        return instance;
    }

    private void generateMockProducts() {
        mockProducts.add(new ProductSeller("SP001", "[Ảnh thật] Suit 3 Chi Tiết Cổ Rời Yếm Ren Rời Cho Bé", "https://i.imgur.com/example_image1.jpg", 140000.0, 290000.0, 97, 3, "Còn hàng",
                "Mô tả chi tiết về bộ suit 3 chi tiết cổ rời yếm ren rời dành cho bé.", "Thời trang trẻ em", "Phí cố định (25000 VNĐ)"));
        mockProducts.add(new ProductSeller("SP002", "Sữa Tăng Cơ Whey Protein Iso-XP", "https://i.imgur.com/example_image2.jpg", 1200000.0, 1200000.0, 50, 10, "Còn hàng",
                "Sản phẩm sữa tăng cơ cao cấp, hỗ trợ phục hồi và phát triển cơ bắp.", "Dinh dưỡng thể thao", "Miễn phí"));
        mockProducts.add(new ProductSeller("SP003", "Bột Ngũ Cốc Giảm Cân Cao Cấp", "https://i.imgur.com/example_image3.jpg", 350000.0, 350000.0, 0, 25, "Hết hàng",
                "Bột ngũ cốc dinh dưỡng, hỗ trợ quá trình giảm cân an toàn và hiệu quả.", "Thực phẩm chức năng", "Phí cố định (20000 VNĐ)"));
        mockProducts.add(new ProductSeller("SP004", "Creatine Monohydrate 300g", "https://i.imgur.com/example_image4.jpg", 400000.0, 400000.0, 10, 0, "Chờ duyệt",
                "Creatine giúp tăng cường sức mạnh, hiệu suất tập luyện và phục hồi cơ.", "Dinh dưỡng thể thao", "Miễn phí"));
        mockProducts.add(new ProductSeller("SP005", "Quần Áo Trẻ Em In Hình Sai Quy Định", "https://i.imgur.com/example_image5.jpg", 80000.0, 80000.0, 5, 0, "Vi phạm",
                "Sản phẩm quần áo trẻ em có in hình ảnh không phù hợp với quy định về văn hóa.", "Thời trang trẻ em", "Phí cố định (25000 VNĐ)"));
        mockProducts.add(new ProductSeller("SP006", "Thực Phẩm Chức Năng Bổ Não", "https://i.imgur.com/example_image6.jpg", 600000.0, 600000.0, 20, 5, "Ẩn",
                "Thực phẩm chức năng hỗ trợ cải thiện trí nhớ, tăng cường tuần hoàn não.", "Thực phẩm chức năng", "Miễn phí"));
        mockProducts.add(new ProductSeller("SP007", "Vitamin C Hàng Nhái", "https://i.imgur.com/example_image7.jpg", 100000.0, 100000.0, 0, 0, "Vi phạm",
                "Sản phẩm Vitamin C không có nguồn gốc rõ ràng, nghi ngờ hàng giả.", "Vitamin", "Phí cố định (15000 VNĐ)"));
        mockProducts.add(new ProductSeller("SP008", "Găng Tay Tập Gym (còn 0 cái)", "https://i.imgur.com/example_image8.jpg", 150000.0, 150000.0, 0, 15, "Hết hàng",
                "Găng tay tập gym chuyên nghiệp, chống trượt, bảo vệ tay.", "Phụ kiện thể thao", "Phí cố định (20000 VNĐ)"));
        mockProducts.add(new ProductSeller("SP009", "Dầu cá Omega-3", "https://i.imgur.com/example_image9.jpg", 250000.0, 250000.0, 75, 12, "Còn hàng",
                "Dầu cá Omega-3 tinh khiết, hỗ trợ sức khỏe tim mạch và thị lực.", "Khoáng chất", "Miễn phí"));
        mockProducts.add(new ProductSeller("SP010", "Băng đô thể thao", "https://i.imgur.com/example_image10.jpg", 50000.0, 50000.0, 0, 50, "Hết hàng",
                "Băng đô thể thao thấm hút mồ hôi tốt, co giãn thoải mái.", "Phụ kiện thể thao", "Phí cố định (15000 VNĐ)"));
        mockProducts.add(new ProductSeller("SP011", "Protein Bar vị Socola", "https://i.imgur.com/example_image11.jpg", 30000.0, 30000.0, 100, 5, "Còn hàng",
                "Thanh protein vị socola thơm ngon, cung cấp năng lượng nhanh.", "Dinh dưỡng thể thao", "Miễn phí"));
        mockProducts.add(new ProductSeller("SP012", "Bộ dụng cụ tập yoga", "https://i.imgur.com/example_image12.jpg", 450000.0, 450000.0, 0, 8, "Hết hàng",
                "Bộ dụng cụ tập yoga đa năng, phù hợp cho người mới bắt đầu.", "Phụ kiện thể thao", "Phí cố định (30000 VNĐ)"));
    }

    public void getAllSellerProducts(ProductCallback<List<ProductSeller>> callback) {
        executorService.execute(() -> {
            try {
                Thread.sleep(500); // Simulate network delay
                callback.onSuccess(new ArrayList<>(mockProducts));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                callback.onError(e);
            }
        });
    }

    public void addProduct(AddProductRequest request, ProductCallback<ProductSeller> callback) {
        executorService.execute(() -> {
            try {
                Thread.sleep(500); // Simulate network delay

                double minPrice = Double.MAX_VALUE;
                double maxPrice = Double.MIN_VALUE;
                int totalStock = 0;

                if (request.getVariants() != null && !request.getVariants().isEmpty()) {
                    for (ProductVariantRequest variant : request.getVariants()) {
                        double currentPrice = (variant.getSalePrice() > 0 && variant.getSalePrice() < variant.getOriginPrice()) ? variant.getSalePrice() : variant.getOriginPrice();
                        if (currentPrice < minPrice) minPrice = currentPrice;
                        if (currentPrice > maxPrice) maxPrice = currentPrice;
                        totalStock += variant.getQuantityInStock();
                    }
                } else {

                    minPrice = 0.0;
                    maxPrice = 0.0;
                    totalStock = 0;
                }

                ProductSeller newProduct = new ProductSeller(
                        UUID.randomUUID().toString(), // Generate a unique ID
                        request.getProductName(),
                        request.getProductImageUris() != null && !request.getProductImageUris().isEmpty() ? request.getProductImageUris().get(0).toString() : "",
                        minPrice,
                        maxPrice,
                        totalStock,
                        0,
                        "Chờ duyệt",
                        request.getDescription(),
                        request.getCategory(),
                        request.getShippingFee()
                );
                mockProducts.add(newProduct);
                callback.onSuccess(newProduct);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                callback.onError(e);
            }
        });
    }

    public void updateProductStatus(String productId, String newStatus, ProductCallback<ProductSeller> callback) {
        executorService.execute(() -> {
            try {
                Thread.sleep(300);
                for (ProductSeller product : mockProducts) {
                    if (product.getProductId().equals(productId)) {
                        product.setProductStatus(newStatus);
                        callback.onSuccess(product);
                        return;
                    }
                }
                callback.onError(new Exception("Product not found"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                callback.onError(e);
            }
        });
    }

    public void updateProduct(String productId, AddProductRequest request, ProductCallback<ProductSeller> callback) {
        executorService.execute(() -> {
            try {
                Thread.sleep(500);

                double minPrice = Double.MAX_VALUE;
                double maxPrice = Double.MIN_VALUE;
                int totalStock = 0;

                if (request.getVariants() != null && !request.getVariants().isEmpty()) {
                    for (ProductVariantRequest variant : request.getVariants()) {
                        double currentPrice = (variant.getSalePrice() > 0 && variant.getSalePrice() < variant.getOriginPrice()) ? variant.getSalePrice() : variant.getOriginPrice();
                        if (currentPrice < minPrice) minPrice = currentPrice;
                        if (currentPrice > maxPrice) maxPrice = currentPrice;
                        totalStock += variant.getQuantityInStock();
                    }
                } else {
                    minPrice = 0.0;
                    maxPrice = 0.0;
                    totalStock = 0;
                }

                for (int i = 0; i < mockProducts.size(); i++) {
                    if (mockProducts.get(i).getProductId().equals(productId)) {
                        ProductSeller updatedProduct = new ProductSeller(
                                productId,
                                request.getProductName(),
                                request.getProductImageUris() != null && !request.getProductImageUris().isEmpty() ? request.getProductImageUris().get(0).toString() : "",
                                minPrice,
                                maxPrice,
                                totalStock,
                                mockProducts.get(i).getSoldQuantity(),
                                "Chờ duyệt",
                                request.getDescription(),
                                request.getCategory(),
                                request.getShippingFee()
                        );
                        mockProducts.set(i, updatedProduct);
                        callback.onSuccess(updatedProduct);
                        return;
                    }
                }
                callback.onError(new Exception("Product not found for update"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                callback.onError(e);
            }
        });
    }

    public void deleteProduct(String productId, ProductCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                Thread.sleep(300);
                boolean removed = mockProducts.removeIf(p -> p.getProductId().equals(productId));
                if (removed) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(new Exception("Product not found for deletion"));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                callback.onError(e);
            }
        });
    }

    public interface ProductCallback<T> {
        void onSuccess(T result);

        void onError(Throwable t);
    }
}