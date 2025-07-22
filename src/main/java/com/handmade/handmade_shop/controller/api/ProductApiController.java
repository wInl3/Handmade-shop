package com.handmade.handmade_shop.controller.api;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.service.ImageService;
import com.handmade.handmade_shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductService productService;

    private final ImageService imageService;


    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request,
                                                  Authentication authentication) {
        String sellerEmail = authentication.getName();
        ProductResponse response = productService.createProduct(request, sellerEmail);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll(@RequestParam(required = false) UUID categoryId,
                                                        @RequestParam(required = false) UUID sellerId) {
        List<ProductResponse> products;

        if (categoryId != null) {
            products = productService.getByCategory(categoryId);
        } else if (sellerId != null) {
            products = productService.getBySeller(sellerId);
        } else {
            products = productService.getAll();
        }

        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       Authentication authentication) {
        String sellerEmail = authentication.getName();
        productService.deleteProduct(id, sellerEmail);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable UUID id,
                                                  @RequestBody ProductRequest request,
                                                  Authentication authentication) {
        String sellerEmail = authentication.getName();
        ProductResponse updated = productService.updateProduct(id, request, sellerEmail);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadImage(@PathVariable UUID id,
                                              @RequestParam("file") MultipartFile file,
                                              Authentication authentication) throws IOException {
        String sellerEmail = authentication.getName();
        String imagePath = imageService.uploadImage(id, file, sellerEmail);
        return ResponseEntity.ok("Image uploaded: " + imagePath);
    }

//    @GetMapping("/filter")
//    public ResponseEntity<Page<ProductResponse>> filterProducts(@ModelAttribute ProductFilterRequest request) {
//        Page<ProductResponse> responsePage = productService.filterProducts(request);
//        return ResponseEntity.ok(responsePage);
//    }


    //GET: http://localhost:8080/products?sellerId=2efa13d2-5d37-4d9a-9656-3a00fc8d5cdc
    //PUT: http://localhost:8080/products
    //POST: http://localhost:8080/products/740833be-2ee4-448e-b105-cc1585700d49/image
    //http://localhost:8080/products/740833be-2ee4-448e-b105-cc1585700d49 (delete)
    //{
    //  "name": "Đèn ngủ gỗ mini",
    //  "description": "Đèn ngủ làm bằng gỗ thủ công, ánh sáng vàng ấm áp.",
    //  "price": 250000,
    //  "quantity": 15,
    //  "categoryId": "4b602921-1978-405e-bfe3-64b55c6dc570",
    //  "imageUrls": []
    //}

    //GET: http://localhost:8080/products/filter?categoryId=94da2b5e-d7de-4dc8-9d17-b62534de0ae1&minPrice=100000&sortBy=price&sortDir=asc&page=0&size=5


}
