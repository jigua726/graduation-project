package com.example.blogcommunity.controller;

import com.example.blogcommunity.dto.request.PasswordResetRequest;
import com.example.blogcommunity.dto.request.PasswordUpdateRequest;
import com.example.blogcommunity.dto.response.ApiResponse;
import com.example.blogcommunity.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    private final UserService userService;

    public PasswordController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/reset/request")
    public ResponseEntity<ApiResponse<String>> requestPasswordReset(@Valid @RequestBody PasswordResetRequest request) {
        userService.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("密码重置链接已发送，请检查邮箱"));
    }

    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody PasswordUpdateRequest request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success("密码重置成功"));
    }

    @GetMapping("/reset/validate/{token}")
    public ResponseEntity<ApiResponse<Boolean>> validateResetToken(@PathVariable String token) {
        boolean valid = userService.validateResetToken(token);
        return ResponseEntity.ok(ApiResponse.success(valid));
    }
}
