package com.example.customer_api.dto.response;

import java.sql.Date;


import lombok.Data;

@Data
public class customerResponse {
    private String customerName;
    private String customerLastname;
    private String citizenId;
    private Date birthDate;
    private String email;
    private String telephone;
    private Long customerId;
}
