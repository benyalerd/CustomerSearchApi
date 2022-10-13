package com.example.customer_api.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.customer_api.dto.request.exportExcelRequest;
import com.example.customer_api.dto.request.searchCustomerRequest;
import com.example.customer_api.dto.response.exportExcelResponse;
import com.example.customer_api.helper.MailService;
import com.example.customer_api.helper.PdfGenerate;
import com.example.customer_api.model.Customer;
import com.example.customer_api.repository.CustomerRepository;
import com.example.customer_api.repository.UserRepository;
import com.example.customer_api.service.search.CustomerSpecification;

import lombok.extern.slf4j.Slf4j;

@Component
@Transactional
@Slf4j
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PdfGenerate pdfGen;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

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

     public exportExcelResponse sendCustomerAttachment(exportExcelRequest request)throws Throwable{
        exportExcelResponse response = new exportExcelResponse();
        response.setErrorCode("200");
        response.setErrorMsg("success");
        response.setIsError(false);
        response.setIsExportSuccess(true);
        
        var user = userRepository.findById(request.getUserId()).get();
        if(user == null){
            response.setIsError(true);
            response.setErrorCode("001");
            response.setErrorMsg("user id is not found");
            response.setIsExportSuccess(false);
            return response;
        }
        else{
            var customerList = searchCustomerAll(request);

            Map<String, Object> data = new HashMap<>();
            data.put("customerList",customerList);

            pdfGen.generatePdfFile("customer.html", data,"\\customer_information.pdf");

            String mailTo = user.getEmail();
            Map<String, String> model = new HashMap<>();
            model.put("email", mailTo);
            mailService.sendCustomerMail(mailTo, model);    
           
        }
        return response;
       
        }
    
}
