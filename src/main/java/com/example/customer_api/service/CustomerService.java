package com.example.customer_api.service;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.customer_api.dto.request.exportExcelRequest;
import com.example.customer_api.dto.request.searchCustomerRequest;
import com.example.customer_api.helper.ExcelExporter;
import com.example.customer_api.model.Customer;
import com.example.customer_api.repository.CustomerRepository;
import com.example.customer_api.service.search.CustomerSpecification;

import lombok.extern.slf4j.Slf4j;

@Component
@Transactional
@Slf4j
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;

    public Customer addCustomer(Customer customer)throws Throwable{
        
        Customer newCustomer = customerRepository.save(customer);
        return newCustomer;
    }

    public Customer updateCustomer(Long customerId,Customer customer)throws Throwable{
        var exitingCustomer = customerRepository.findById(customerId).get();
        if(exitingCustomer == null) return null;

        if(customer.getCustomerName()!=null)exitingCustomer.setCustomerName(customer.getCustomerName());;
        if(customer.getCustomerLastname()!=null)exitingCustomer.setCustomerLastname(customer.getCustomerLastname());
        if(customer.getEmail()!=null)exitingCustomer.setEmail(customer.getEmail());;
        if(customer.getTelephone()!=null)exitingCustomer.setTelephone(customer.getTelephone());

        return customerRepository.save(exitingCustomer);
    }

    public Customer deleteCustomer(Long customerId)throws Throwable{
        var exitingCustomer = customerRepository.findById(customerId).get();
        if(exitingCustomer == null) return null;
        customerRepository.delete(exitingCustomer);
        return exitingCustomer;
    }

    public Page<Customer> searchCustomer(searchCustomerRequest request)throws Throwable{
        Specification<Customer> spec = new CustomerSpecification(request);
        Page<Customer> listCustomer =customerRepository.findAll(spec,PageRequest.of(request.getPage(),request.getLimit()));
        return listCustomer;
     }

     public List<Customer> searchCustomerAll(exportExcelRequest request)throws IOException{

        searchCustomerRequest req = new searchCustomerRequest();
        req.setCitizenId(request.getCitizenId());
        req.setEmail(request.getEmail());
        req.setName(request.getName());
        req.setTelephone(request.getTelephone());

        Specification<Customer> spec = new CustomerSpecification(req);
        List<Customer> listCustomer =customerRepository.findAll(spec);
        
        return listCustomer;
     }

}
