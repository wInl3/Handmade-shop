package com.handmade.handmade_shop.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder // ✅ THÊM DÒNG NÀY
public class RegisterRequest { //dùng để tạo/cập nhật sản phẩm
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String role; // ✅ thêm dòng này

}
