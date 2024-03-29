package com.spring.banking_management_system.service;


import com.spring.banking_management_system.dto.BankResponse;
import com.spring.banking_management_system.dto.CreditDebitRequest;
import com.spring.banking_management_system.dto.EnquiryRequest;
import com.spring.banking_management_system.dto.LoginDTO;
import com.spring.banking_management_system.dto.TransferRequest;
import com.spring.banking_management_system.dto.UserRequest;


public interface UserService {

	
	 BankResponse createAccount(UserRequest userRequest);

	 BankResponse balanceEnquiry(EnquiryRequest request);
	 
	 String nameEnquiry(EnquiryRequest request);

     BankResponse creditAccount(CreditDebitRequest request);
     
     BankResponse debitAccount(CreditDebitRequest request);
     
     BankResponse transfer(TransferRequest request);
     
     BankResponse login(LoginDTO loginDto);

}
