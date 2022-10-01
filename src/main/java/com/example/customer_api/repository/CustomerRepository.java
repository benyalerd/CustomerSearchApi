package com.example.customer_api.repository;

import org.springframework.stereotype.Repository;

import com.example.customer_api.model.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer,Long>,JpaSpecificationExecutor<Customer> {
    public Customer findByEmailOrCitizenId(String email, String citizenId);
    public Page<Customer>findAll(Specification<Customer> spec,Pageable pageable);
}
