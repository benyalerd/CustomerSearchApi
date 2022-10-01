package com.example.customer_api.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.customer_api.model.Users;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<Users,Long>  {
    public Users findByEmail(String email);
}
