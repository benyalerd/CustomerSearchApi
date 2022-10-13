package com.example.customer_api.dto.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class userRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
    private String repeatPassword;
}
