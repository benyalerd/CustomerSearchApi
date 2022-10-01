package com.example.customer_api.service;

import java.security.MessageDigest;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.customer_api.model.Users;
import com.example.customer_api.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Transactional
@Slf4j
public class UsersService {
    
    @Autowired
    UserRepository userRepository;
    
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


}
