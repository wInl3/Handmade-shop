package com.handmade.handmade_shop.controller.web;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.service.ProductService;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product/{id}")
    public String viewProductDetail(@PathVariable UUID id, Model model) {
        ProductResponse product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product-detail";
    }

}
