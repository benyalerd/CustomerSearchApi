package com.example.customer_api.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class searchCustomerResponse extends baseResponse{
    private List<customerResponse> customerList;
    private Long totalRecord;
}
