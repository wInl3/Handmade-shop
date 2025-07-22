package com.handmade.handmade_shop.controller.web;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.entity.*;
import com.handmade.handmade_shop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/home")
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) UUID categoryId,
                       Model model) {

        ProductFilterRequest filter = new ProductFilterRequest();
        filter.setPage(page);
        filter.setCategoryId(categoryId);

        Page<ProductResponse> products = productService.filterProducts(filter);
        List<Category> categories = categoryService.getAll(); // đảm bảo không null

        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("categories", categories); // truyền lên view
        model.addAttribute("selectedCategoryId", categoryId);

        return "home";
    }



}
