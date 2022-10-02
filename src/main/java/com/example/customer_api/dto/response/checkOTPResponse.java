package com.example.customer_api.dto.response;

import lombok.Data;

@Data
public class checkOTPResponse extends baseResponse {
    private Boolean IsValidOTP;
}
