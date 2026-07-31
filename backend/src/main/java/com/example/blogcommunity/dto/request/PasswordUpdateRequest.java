package com.example.blogcommunity.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PasswordUpdateRequest {

    @NotBlank(message = "token不能为空")
    private String token;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, message = "密码长度至少为6个字符")
    private String newPassword;
}
