package com.example.customer_api.controller;

import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.customer_api.dto.request.checkOTPRequest;
import com.example.customer_api.dto.request.sendOTPRequest;
import com.example.customer_api.dto.request.userRequest;
import com.example.customer_api.dto.response.checkOTPResponse;
import com.example.customer_api.dto.response.sendOTPResponse;
import com.example.customer_api.dto.response.userResponse;
import com.example.customer_api.model.Users;
import com.example.customer_api.service.UsersService;
import com.example.customer_api.validation.CommonValidation;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class userController {
    @Autowired
    private Validator validator;

    @Autowired
    private CommonValidation commonValidation;

    @Autowired
    private UsersService usersService;

    @PostMapping("addUser")
    public ResponseEntity<Object> AddUser(@RequestBody userRequest user)
    {

        userResponse response = new userResponse();
        response.setErrorCode("200");
        response.setErrorMsg("success");
        response.setIsError(false);
        try{
            var violations = validator.validate(user);           
            log.info("violations = {}",violations);

            var addValidate = commonValidation.usersValidate(user);
            if(!violations.isEmpty() || !addValidate)
            {
                response.setIsError(true);
                response.setErrorCode("001");
                response.setErrorMsg("invalid request");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else
            {
                var isexistingEmail = commonValidation.checkExistingUserByEmail(user.getEmail());
                if(isexistingEmail){
                    response.setIsError(true);
                    response.setErrorCode("001");
                    response.setErrorMsg("email is exist");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
                if(!(user.getPassword().equals(user.getRepeatPassword())))
                {
                    response.setIsError(true);
                    response.setErrorCode("001");
                    response.setErrorMsg("password is not match");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
                var mapper = new ModelMapper();
                mapper.getConfiguration()
                        .setMatchingStrategy(MatchingStrategies.STRICT);

                var requetUser = mapper.map(user, Users.class);
                
                var newUser = usersService.registerUser(requetUser);

                if(newUser == null){
                    response.setIsError(true);
                    response.setErrorCode("002");
                    response.setErrorMsg("insert failed");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }

                response.setUserId(newUser.getUserId());
                return ResponseEntity.ok(response);
            }
        }catch(Throwable t){
            log.error("error occur ={}",t.getMessage());
            response.setIsError(true);
            response.setErrorCode("500");
            response.setErrorMsg("exception or server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("login")
    public ResponseEntity<Object> Login(@RequestBody userRequest user)
    {

        userResponse response = new userResponse();
        response.setErrorCode("200");
        response.setErrorMsg("success");
        response.setIsError(false);
        try{
            var violations = validator.validate(user);           
            log.info("violations = {}",violations);

            var addValidate = commonValidation.usersValidate(user);
            if(!violations.isEmpty() || !addValidate)
            {
                response.setIsError(true);
                response.setErrorCode("001");
                response.setErrorMsg("invalid request");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else
            {
                
                var mapper = new ModelMapper();
                mapper.getConfiguration()
                        .setMatchingStrategy(MatchingStrategies.STRICT);

                var newUser = usersService.login(user.getEmail(),user.getPassword());

                if(newUser == null){
                    response.setIsError(true);
                    response.setErrorCode("002");
                    response.setErrorMsg("email or password is incorrect.");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }

                response.setUserId(newUser.getUserId());
                return ResponseEntity.ok(response);
            }
        }catch(Throwable t){
            log.error("error occur ={}",t.getMessage());
            response.setIsError(true);
            response.setErrorCode("500");
            response.setErrorMsg("exception or server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("sendOTP")
    public ResponseEntity<Object> SendOTP(@RequestBody sendOTPRequest request)
    {

        sendOTPResponse response = new sendOTPResponse();
        try{
            var violations = validator.validate(request);           
            log.info("violations = {}",violations);

            if(!violations.isEmpty())
            {
                response.setIsError(true);
                response.setErrorCode("001");
                response.setErrorMsg("invalid request");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else
            {
                response = usersService.sendOTP(request);               
                return ResponseEntity.ok(response);
            
            }
        }catch(Throwable t){
            log.error("error occur ={}",t.getMessage());
            response.setIsError(true);
            response.setErrorCode("500");
            response.setErrorMsg("exception or server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("checkOTP")
    public ResponseEntity<Object> checkOTP(@RequestBody checkOTPRequest request)
    {

        checkOTPResponse response = new checkOTPResponse();
        try{
            var violations = validator.validate(request);           
            log.info("violations = {}",violations);

            if(!violations.isEmpty())
            {
                response.setIsError(true);
                response.setErrorCode("001");
                response.setErrorMsg("invalid request");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else
            {
                response = usersService.checkOTP(request);               
                return ResponseEntity.ok(response);
            
            }
        }catch(Throwable t){
            log.error("error occur ={}",t.getMessage());
            response.setIsError(true);
            response.setErrorCode("500");
            response.setErrorMsg("exception or server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}