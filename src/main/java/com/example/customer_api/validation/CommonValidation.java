package com.example.customer_api.validation;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.customer_api.dto.request.addCustomerRequest;
import com.example.customer_api.dto.request.updateCustomerRequest;
import com.example.customer_api.dto.request.userRequest;
import com.example.customer_api.repository.CustomerRepository;
import com.example.customer_api.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Transactional
@Slf4j
public class CommonValidation {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Boolean addCustomerValidate(addCustomerRequest request){
       
        if(!CheckEmptyOrNull(request.getEmail())){
           if(!checkEmail(request.getEmail())){
              return false;
           }
        }
        if(!CheckEmptyOrNull(request.getTelephone())){
            if(!checkTelephone(request.getTelephone())){
               return false;
            }
         }
         if(!CheckEmptyOrNull(request.getCitizenId()))
         {
         if(!checkCitizen(request.getCitizenId())){          
               return false;            
         }
        }
        if(request.getBirthDate() != null)
        {
         if(!checkBirthDate(request.getBirthDate())){          
            return false;            
      }
    }
        return true;
    }

    public Boolean usersValidate(userRequest request){
       
        if(!CheckEmptyOrNull(request.getEmail())){
           if(!checkEmail(request.getEmail())){
              return false;
           }
        }
      
        return true;
    }

    public Boolean updateCustomerValidate(updateCustomerRequest request){
       
        if(!CheckEmptyOrNull(request.getEmail())){
            if(!checkEmail(request.getEmail())){
               return false;
            }
         }
         if(!CheckEmptyOrNull(request.getTelephone())){
            if(!checkTelephone(request.getTelephone())){
               return false;
            }
         }
       
        return true;
    }

    public Boolean checkExistingUser(Long id){
        var user = userRepository.findById(id);
        if(user.isEmpty()){
            return false;
        }
        return true;
    }

    public Boolean checkExistingUserByEmail(String email){
        var customer = userRepository.findByEmail(email);
        if(customer == null){
            return false;
        }
        return true;
    }

    public Boolean checkExistingCustomer(String email,String id){
        var customer = customerRepository.findByEmailOrCitizenId(email,id);
        if(customer == null){
            return false;
        }
        return true;
    }

    public Boolean CheckEmptyOrNull(String text){
        if(text == null){
            return true;
        }else if(text.equalsIgnoreCase("")){
            return true;
        }
        else{
            return false;
        }
    }
    private Boolean checkTelephone(String tel){
       if(tel.length()!= 10){
        return false;
       }
        return true;
    }

    private Boolean checkEmail(String email){
    Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    Matcher match = pattern.matcher(email);
    if (match.matches() && email.length() <= 50) {
      return true;
    } else {
      return false;
    }
}

  private Boolean checkCitizen(String id){
if(id.length() == 13)
{
    
    var step1 = (Integer.parseInt(String.valueOf(id.charAt(0)))*13)+(Integer.parseInt(String.valueOf(id.charAt(1)))*12)+(Integer.parseInt(String.valueOf(id.charAt(2)))*11)+(Integer.parseInt(String.valueOf(id.charAt(3)))*10)+(Integer.parseInt(String.valueOf(id.charAt(4)))*9)+(Integer.parseInt(String.valueOf(id.charAt(5)))*8)+(Integer.parseInt(String.valueOf(id.charAt(6)))*7)+(Integer.parseInt(String.valueOf(id.charAt(7)))*6)+(Integer.parseInt(String.valueOf(id.charAt(8)))*5)+(Integer.parseInt(String.valueOf(id.charAt(9)))*4)+(Integer.parseInt(String.valueOf(id.charAt(10)))*3)+(Integer.parseInt(String.valueOf(id.charAt(11)))*2);
    var step2 = step1%11;
    var step3 = 11-step2;
    if(String.valueOf(String.valueOf(step3).charAt(String.valueOf(step3).length()-1)).equalsIgnoreCase(String.valueOf(id.charAt(12)))){
        return true;
    }
}
return false;
}

  private Boolean checkBirthDate(Date date){
    Date dateNow = new Date(System.currentTimeMillis());
     if(date.compareTo(dateNow) >=0){
        return false;
     }
     return true;
  }
}

