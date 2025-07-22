package com.handmade.handmade_shop.controller.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestRoleController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Chỉ ADMIN mới thấy được nội dung này!";
    }

    @GetMapping("/seller")
    @PreAuthorize("hasRole('SELLER')")
    public String sellerAccess() {
        return "Chỉ SELLER mới được vào trang này!";
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String customerAccess() {
        return "Nội dung dành cho khách hàng!";
    }

    //url: http://localhost:8080/api/test/customer
}
