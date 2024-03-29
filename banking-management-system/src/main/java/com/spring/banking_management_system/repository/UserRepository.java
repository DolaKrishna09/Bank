package com.spring.banking_management_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.banking_management_system.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
     
	 boolean existsByEmail(String email);
    
	 Optional<User> findByEmail(String email);
   
	 boolean existsByAccountNumber(String accountNumber);
     
     User findByAccountNumber(String accountNumber);
    
     
     
}
