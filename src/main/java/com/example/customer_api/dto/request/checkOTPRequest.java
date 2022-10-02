package com.example.customer_api.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class checkOTPRequest {
    @NotNull
    private Long userId;
    @NotBlank
    private String otp;
}
