package com.example.customer_api.service;

import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.customer_api.dto.request.checkOTPRequest;
import com.example.customer_api.dto.request.sendOTPRequest;
import com.example.customer_api.dto.response.checkOTPResponse;
import com.example.customer_api.dto.response.sendOTPResponse;
import com.example.customer_api.helper.MailService;
import com.example.customer_api.model.Users;
import com.example.customer_api.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Transactional
@Slf4j
public class UsersService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;
    
    public Users registerUser(Users user)throws Throwable{
        
        String hashPassword = hashPassword(user.getPassword());
        user.setPassword(hashPassword);
        Users newUser = userRepository.save(user);
        return newUser;
    }

    public Users login(String email, String password)
    throws Throwable{
    log.info("login service...");
    log.info("email={} password={}",email,password);

    var user = userRepository.findByEmail(email);

    if(user != null)
    {
        String loginPassword = hashPassword(password);
        log.info("Hash password={}", loginPassword);
        log.info("DB password ={}", user.getPassword());
        if(user.getPassword().equals(loginPassword)){
        return user;
           
    }
    }
    return null;
}

    public sendOTPResponse sendOTP(sendOTPRequest request)throws Throwable{
    sendOTPResponse response = new sendOTPResponse();
    response.setErrorCode("200");
    response.setErrorMsg("success");
    response.setIsError(false);
    
    var user = userRepository.findById(request.getUserId()).get();
    if(user == null){
        response.setIsError(true);
        response.setErrorCode("001");
        response.setErrorMsg("user id is not found");
        return response;
    }
    else{
        String mailTo = user.getEmail();
        String otp = getRandomNumberString();
        Date otpExpire = generateOtpExpireDate();

        Map<String, String> model = new HashMap<>();
        model.put("email", mailTo);
        model.put("otp", otp);
        mailService.sendOTPMail(mailTo, model);

        user.setOtp(otp);
        user.setExpireDateOTP(otpExpire);
        userRepository.save(user);

        response.setOtpLifeTime(3);
    }
    return response;
   
    }

    public checkOTPResponse checkOTP(checkOTPRequest request)
    throws Throwable{
   checkOTPResponse response = new checkOTPResponse();
   response.setErrorCode("200");
    response.setErrorMsg("success");
    response.setIsError(false);
    
    var user = userRepository.findById(request.getUserId()).get();
    if(user == null){
        response.setIsError(true);
        response.setErrorCode("001");
        response.setErrorMsg("user id is not found");
        return response;
    }
    else{
        if(user.getOtp().equals(request.getOtp())){
            Date dateNow = new Date(System.currentTimeMillis());
            if(user.getExpireDateOTP().compareTo(dateNow) >=0){
                response.setIsValidOTP(true);
            }
            else{
                response.setIsValidOTP(false);
            }
        }
        else
        {
            response.setIsValidOTP(false);
        }
    }
    return response;
   
}

    {/*Helper Method */}
    public String hashPassword(String password)
    throws Throwable {
/* MessageDigest instance for MD5. */
MessageDigest m = MessageDigest.getInstance("MD5");

/* Add plain-text password bytes to digest using MD5 update() method. */
m.update(password.getBytes());

/* Convert the hash value into bytes */
byte[] bytes = m.digest();

/* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
StringBuilder s = new StringBuilder();
for(int i=0; i< bytes.length ;i++)
{
    s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
}

/* Complete hashed password in hexadecimal format */
String encryptedpassword = s.toString();
return encryptedpassword;
}

    public String getRandomNumberString() {
    Random rnd = new Random();
    int number = rnd.nextInt(999999);

    return String.format("%06d", number);
    }

    public Date generateOtpExpireDate(){
        {/*Minute*/}
    Integer otpLifeTime = 3;

    Calendar date = Calendar.getInstance();
    long timeInSecs = date.getTimeInMillis();
    Date afterAdding3Mins = new Date(timeInSecs + (otpLifeTime * 60 * 1000));
    return afterAdding3Mins;
    }

    
}
