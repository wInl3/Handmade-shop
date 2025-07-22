package com.handmade.handmade_shop.controller.web;

import com.handmade.handmade_shop.dto.ProductRequest;
import com.handmade.handmade_shop.dto.ProductResponse;
import com.handmade.handmade_shop.service.*;
import lombok.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/seller/products")
@RequiredArgsConstructor
public class SellerProductWebController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String viewMyProducts(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UUID sellerId = productService.getSellerIdByEmail(userDetails.getUsername());
        List<ProductResponse> products = productService.getBySeller(sellerId);
        model.addAttribute("products", products);
        return "seller/product-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("productRequest", new ProductRequest());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("isEdit", false); // ✅ Thêm dòng này
        return "seller/product-form";
    }


    @PostMapping
    public String createProduct(@ModelAttribute ProductRequest request,
                                @AuthenticationPrincipal UserDetails userDetails) {
        List<String> imageUrls = new ArrayList<>();

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            String uploadDir = "uploads"; // Thư mục ngoài static, đã được cấu hình
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) uploadFolder.mkdirs(); // Tạo thư mục nếu chưa có

            for (MultipartFile file : request.getImages()) {
                try {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir, fileName); // uploads/filename.jpg
                    Files.write(filePath, file.getBytes());

                    // Đường dẫn public ra web
                    imageUrls.add("/uploads/" + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        request.setImageUrls(imageUrls);
        productService.createProduct(request, userDetails.getUsername());
        return "redirect:/seller/products";
    }


    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable UUID id, Model model) {
        ProductResponse response = productService.getProductById(id);

        ProductRequest request = new ProductRequest();
        request.setName(response.getName());
        request.setDescription(response.getDescription());
        request.setPrice(response.getPrice());
        request.setQuantity(response.getQuantity());
        request.setCategoryId(response.getCategoryId());

        model.addAttribute("id", id);
        model.addAttribute("productRequest", request);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("isEdit", true); // ✅ Thêm dòng này
        return "seller/product-form";
    }


    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable UUID id,
                                @ModelAttribute ProductRequest request,
                                @AuthenticationPrincipal UserDetails userDetails) {
        productService.updateProduct(id, request, userDetails.getUsername());
        return "redirect:/seller/products";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable UUID id,
                                @AuthenticationPrincipal UserDetails userDetails) {
        productService.deleteProduct(id, userDetails.getUsername());
        return "redirect:/seller/products";
    }
}

