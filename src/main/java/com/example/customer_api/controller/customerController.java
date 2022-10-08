package com.example.customer_api.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.customer_api.dto.request.addCustomerRequest;
import com.example.customer_api.dto.request.exportExcelRequest;
import com.example.customer_api.dto.request.searchCustomerRequest;
import com.example.customer_api.dto.request.updateCustomerRequest;
import com.example.customer_api.dto.response.customerResponse;
import com.example.customer_api.dto.response.deleteResponse;
import com.example.customer_api.dto.response.exportExcelResponse;
import com.example.customer_api.dto.response.insertResponse;
import com.example.customer_api.dto.response.searchCustomerResponse;
import com.example.customer_api.dto.response.updateResponse;
import com.example.customer_api.helper.ExcelExporter;
import com.example.customer_api.model.Customer;
import com.example.customer_api.service.CustomerService;
import com.example.customer_api.validation.CommonValidation;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/customer")
@Slf4j
public class customerController {
    
    @Autowired
    private Validator validator;

    @Autowired
    private CommonValidation commonValidation;

    @Autowired
    private CustomerService customerService;

    @PostMapping("addCustomer")
    public ResponseEntity<Object> AddCustomer(@RequestBody addCustomerRequest customer)
    {

        insertResponse response = new insertResponse();
        response.setErrorCode("200");
        response.setErrorMsg("success");
        response.setIsEror(false);
        try{
            var violations = validator.validate(customer);           
            log.info("violations = {}",violations);

            var addValidate = commonValidation.addCustomerValidate(customer);
            if(!violations.isEmpty() || !addValidate)
            {
                response.setIsEror(true);
                response.setErrorCode("001");
                response.setErrorMsg("invalid request");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else
            {
                var isExistingUser = commonValidation.checkExistingUser(customer.getUserId());
                if(!isExistingUser){
                    response.setIsEror(true);
                    response.setErrorCode("001");
                    response.setErrorMsg("user id is not found");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
                var isExistingCustomer = commonValidation.checkExistingCustomer(customer.getEmail(),customer.getCitizenId());
                if(isExistingCustomer){
                    response.setIsEror(true);
                    response.setErrorCode("001");
                    response.setErrorMsg("citizen id or email is exist");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
                var mapper = new ModelMapper();
                mapper.getConfiguration()
                        .setMatchingStrategy(MatchingStrategies.STRICT);

                var requetCustomer = mapper.map(customer, Customer.class);
                
                var newCustomer = customerService.addCustomer(requetCustomer);

                if(newCustomer == null){
                    response.setIsEror(true);
                    response.setErrorCode("002");
                    response.setErrorMsg("insert failed");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }

                response.setInsertId(newCustomer.getCustomerId());
                return ResponseEntity.ok(response);
            }
        }catch(Throwable t){
            log.error("error occur ={}",t.getMessage());
            response.setIsEror(true);
            response.setErrorCode("500");
            response.setErrorMsg("exception or server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("update_customer/{customer_id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable("customer_id") Long customer_id ,@RequestBody updateCustomerRequest newCustomer)
    {
       
        updateResponse response = new updateResponse();
        response.setErrorCode("200");
        response.setErrorMsg("success");
        response.setIsEror(false);

        try{

            var violations = validator.validate(newCustomer);
            log.info("violations = {}",violations);

            var updateValidate = commonValidation.updateCustomerValidate(newCustomer);

            if(!violations.isEmpty() || !updateValidate)
            {
                response.setIsEror(true);
                response.setErrorCode("001");
                response.setErrorMsg("invalid request");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else
            {

                var mapper = new ModelMapper();
                mapper.getConfiguration()
                        .setMatchingStrategy(MatchingStrategies.STRICT);
                        var isExistingUser = commonValidation.checkExistingUser(newCustomer.getUserId());
                        if(!isExistingUser){
                            response.setIsEror(true);
                            response.setErrorCode("001");
                            response.setErrorMsg("user id is not found");
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        }
                var customer = customerService.updateCustomer(customer_id,mapper.map(newCustomer,Customer.class));
                if(customer == null)
                {
                    response.setIsEror(true);
                    response.setErrorCode("003");
                    response.setErrorMsg("update failed");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }

                response.setUpdateId(customer.getCustomerId());
                return ResponseEntity.ok(response);
            }
        } catch(Throwable t){

            log.error("error occur={}",t.getMessage());
            response.setIsEror(true);
            response.setErrorCode("500");
            response.setErrorMsg("exception or server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }

    @PostMapping("delete_customer/{customer_id}/{user_id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("customer_id") Long customer_id, @PathVariable("user_id") Long user_id )
    {
       
        deleteResponse response = new deleteResponse();
        response.setErrorCode("200");
        response.setErrorMsg("success");
        response.setIsEror(false);

        try{          
            if(customer_id == null)
            {
                response.setIsEror(true);
                response.setErrorCode("001");
                response.setErrorMsg("invalid request");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else
            {

                var mapper = new ModelMapper();
                mapper.getConfiguration()
                        .setMatchingStrategy(MatchingStrategies.STRICT);
                        var isExistingUser = commonValidation.checkExistingUser(user_id);
                        if(!isExistingUser){
                            response.setIsEror(true);
                            response.setErrorCode("001");
                            response.setErrorMsg("user id is not found");
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        }
                var customer = customerService.deleteCustomer(customer_id);
                if(customer == null)
                {
                    response.setIsEror(true);
                    response.setErrorCode("003");
                    response.setErrorMsg("delete failed");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }

                response.setDeleteId(customer.getCustomerId());
                return ResponseEntity.ok(response);
            }
        } catch(Throwable t){

            log.error("error occur={}",t.getMessage());
            response.setIsEror(true);
            response.setErrorCode("500");
            response.setErrorMsg("exception or server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }

    @PostMapping("searchCustomer")
    public ResponseEntity<Object> searchCustomer(@RequestBody searchCustomerRequest customer)
    {

        searchCustomerResponse response = new searchCustomerResponse();
        response.setErrorCode("200");
        response.setErrorMsg("success");
        response.setIsEror(false);
        try{
            var violations = validator.validate(customer);           
            log.info("violations = {}",violations);

            if(!violations.isEmpty())
            {
                response.setIsEror(true);
                response.setErrorCode("001");
                response.setErrorMsg("invalid request");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else
            {
                var isExistingUser = commonValidation.checkExistingUser(customer.getUserId());
                if(!isExistingUser){
                    response.setIsEror(true);
                    response.setErrorCode("001");
                    response.setErrorMsg("user id is not found");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
                var result =   customerService.searchCustomer(customer);
                log.info("result = {}",result);

                var mapper = new ModelMapper();
                mapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT);

            if(result != null)
            {
            var resultMapping = result.getContent().stream().map(p -> mapper.map(p,customerResponse.class)).collect(Collectors.toList());
            response.setCustomerList(resultMapping);
            response.setTotalRecord(result.getTotalElements());
            }
            
            response.setErrorCode("200");
            response.setErrorMsg("success");
            response.setIsEror(false);
            return ResponseEntity.ok(response);
            }
        }catch(Throwable t){
            log.error("error occur ={}",t.getMessage());
            response.setIsEror(true);
            response.setErrorCode("500");
            response.setErrorMsg("exception or server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response ,@RequestBody exportExcelRequest request) throws IOException {
      
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customers_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<Customer> listUsers = customerService.searchCustomerAll(request);
         
        ExcelExporter excelExporter = new ExcelExporter(listUsers);
         
        excelExporter.export(response);    
         
    }  
    
    @PostMapping("/attachment/customerinfo")
    public ResponseEntity<Object> sendCustomerAttachment(@RequestBody exportExcelRequest request)
    {

        exportExcelResponse response = new exportExcelResponse();
        try{
            var violations = validator.validate(request);           
            log.info("violations = {}",violations);

            if(!violations.isEmpty())
            {
                response.setIsEror(true);
                response.setErrorCode("001");
                response.setErrorMsg("invalid request");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else
            {
                response = customerService.sendCustomerAttachment(request);               
                return ResponseEntity.ok(response);
            
            }
        }catch(Throwable t){
            log.error("error occur ={}",t.getMessage());
            response.setIsEror(true);
            response.setErrorCode("500");
            response.setErrorMsg("exception or server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
