package com.example.customer_api.dto.response;

import lombok.Data;

@Data
public class sendOTPResponse extends baseResponse {
    private Integer otpLifeTime;
}
