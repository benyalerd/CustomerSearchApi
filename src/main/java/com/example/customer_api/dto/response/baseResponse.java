package com.example.customer_api.dto.response;

import lombok.Data;

@Data
public class baseResponse {
    private Boolean isError;
    private String errorCode;
    private String errorMsg;
}
