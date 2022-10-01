package com.example.customer_api.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class deleteCustomerRequest {
    @NotNull
    private Long customerId;
    @NotNull
    private Long userId;
}
