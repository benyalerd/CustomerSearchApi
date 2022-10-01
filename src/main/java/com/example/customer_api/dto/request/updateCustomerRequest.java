package com.example.customer_api.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class updateCustomerRequest {
    @NotBlank
    @Length(max = 50)
    private String customerName;
    @NotBlank
    @Length(max = 50)
    private String customerLastname;
    private String telephone;
    @NotNull
    private Long userId;
}
