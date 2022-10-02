package com.example.customer_api.dto.response;

import lombok.Data;

@Data
public class exportExcelResponse extends baseResponse {
    private Boolean isExportSuccess;
}
