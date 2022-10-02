package com.example.customer_api.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class exportExcelRequest {
    private String name;
    private String citizenId;
    private String email;
    private String telephone;
    @NotNull
    private Long userId;
}
