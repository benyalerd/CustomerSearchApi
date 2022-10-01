package com.example.customer_api.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class searchCustomerRequest {
    private String name;
    private String citizenId;
    private String email;
    private String telephone;
    @NotNull
    private Long userId;
    private Integer page;
    private Integer limit;
}
