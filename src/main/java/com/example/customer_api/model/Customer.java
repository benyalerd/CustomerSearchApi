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

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
@Entity
@Data
@Table(name="CUSTOMER")
@EntityListeners(AuditingEntityListener.class)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CUSTOMER_ID")
    private Long customerId;  

    @Column(name="CUSTOMER_NAME")
    @NotNull
    private String customerName;

    @Column(name="CUSTOMER_LASTNAME")
    @NotNull
    private String customerLastname;

    @Column(name="CITIZEN_ID")
    @NotNull
    private String citizenId;

    @Column(name="BIRTH_DATE")
    private Date birthDate;

    @Column(name="EMAIL")
    private String email;

    @Column(name="TELEPHONE")
    private String telephone;

    @Column(name="CREATED_DATE")
    @CreatedDate
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    @Column(name="CREATED_BY")
    @CreatedBy
    private String createdBy;

    @Column(name="UPDATED_DATE")
    @LastModifiedDate
    private Date updatedDate;

    @Column(name="UPDATED_BY")
    @LastModifiedBy
    private String updatedBy;
}
