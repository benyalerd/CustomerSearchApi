package com.example.customer_api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Data;
@Entity
@Data
@Table(name="USERS")
@EntityListeners(AuditingEntityListener.class)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Long userId;   

    @Column(name="EMAIL")
    @NotNull
    private String email;

    @Column(name="PASSWORD")
    @NotNull
    private String password;

    @Column(name="OTP")
    private String otp;
    
    @Column(name="EXPIRE_DATE_OTP")
    private Date expireDateOTP;

    @Column(name="CREATED_DATE")
    @CreatedDate
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    @Column(name="UPDATED_DATE")
    @LastModifiedDate
    private Date updatedDate;

}

