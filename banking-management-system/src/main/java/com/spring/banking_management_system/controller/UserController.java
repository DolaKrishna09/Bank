package com.spring.banking_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.banking_management_system.dto.BankResponse;
import com.spring.banking_management_system.dto.CreditDebitRequest;
import com.spring.banking_management_system.dto.EnquiryRequest;
import com.spring.banking_management_system.dto.LoginDTO;
import com.spring.banking_management_system.dto.TransferRequest;
import com.spring.banking_management_system.dto.UserRequest;
import com.spring.banking_management_system.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000/")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/createAccount")
	public BankResponse createAccount(@RequestBody UserRequest userRequest) {
		return userService.createAccount(userRequest);
	}
	
	@PostMapping("/login")
	public BankResponse login(@RequestBody LoginDTO loginDto ) {
		return userService.login(loginDto);
		
	}

	 @PostMapping("/balanceEnquiry")
	    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
	      
	        return userService.balanceEnquiry(enquiryRequest);
	    }

	@GetMapping("/nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request) {
		return userService.nameEnquiry(request);
	}

	@PostMapping("/credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
		return userService.creditAccount(request);
	}

	@PostMapping("/debit")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
		return userService.debitAccount(request);
	}
	
	
	@PostMapping("/transfer")
	public BankResponse transfer(@RequestBody TransferRequest request) {
		return userService.transfer(request);	
	}

	
}
