package com.example.customer_api.service.search;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.example.customer_api.dto.request.searchCustomerRequest;
import com.example.customer_api.model.Customer;

public class CustomerSpecification implements Specification<Customer>{

    private searchCustomerRequest filter;

    public CustomerSpecification(searchCustomerRequest filter) {
        super();
        this.filter = filter;
    }
    
    public boolean checkNullAndEmpty(String text)
    {
        return text != null && !text.isEmpty();
    }
    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate p = criteriaBuilder.conjunction();

        
        if (checkNullAndEmpty(filter.getCitizenId())) {
         p.getExpressions().add(criteriaBuilder.and(criteriaBuilder.like(root.get("citizenId"), "%"+filter.getCitizenId()+"%")));
        }
        if (checkNullAndEmpty(filter.getTelephone())) {
            p.getExpressions().add(criteriaBuilder.and(criteriaBuilder.like(root.get("telephone"), "%"+filter.getTelephone()+"%")));
        }
        if (checkNullAndEmpty(filter.getEmail())) {
            p.getExpressions().add(criteriaBuilder.and(criteriaBuilder.like(root.get("email"), "%"+filter.getEmail()+"%")));
        }
        if (checkNullAndEmpty(filter.getName())) {
            var custSplit = filter.getName().split(" ");
            var custName = "";
            var custLastname = "";
            if(custSplit.length > 0){
                custName = custSplit[0];
            }
            if(custSplit.length > 1){
                custLastname = custSplit[1];
            }
           
            p.getExpressions().add(criteriaBuilder.and(criteriaBuilder.like(root.get("customerLastname"), "%"+custLastname+"%")));
            p.getExpressions().add(criteriaBuilder.and(criteriaBuilder.like(root.get("customerName"), "%"+custName+"%")));
        }
       
        return p;
    }
}
