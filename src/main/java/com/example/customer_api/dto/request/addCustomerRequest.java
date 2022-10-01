package com.example.customer_api.dto.request;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class addCustomerRequest {
    @NotBlank
    @Length(max = 50)
    private String customerName;
    @NotBlank
    @Length(max = 50)
    private String customerLastname;
    private String citizenId;
    private Date birthDate;
    private String email;
    private String telephone;
    @NotNull
    private Long userId;
}
