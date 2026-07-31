package com.example.blogcommunity.dto.request;

import javax.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @Email(message = "邮箱格式不正确")
    private String email;

    private String nickname;

    private String avatar;

    private String bio;
}