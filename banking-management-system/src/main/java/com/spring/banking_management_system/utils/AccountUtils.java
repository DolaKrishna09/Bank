package com.spring.banking_management_system.utils;

import java.time.Year;

public class AccountUtils {
	
	
	public static final String ACCOUNT_EXISTS_CODE="001";
	public static final String ACCOUNT_EXISTS_MESSAGE="THE ACCOUNT ASSOCIATED WITH THIS USER HAS ALREADY BEEN CREATED!";
	
	public static final String ACCOUNT_CREATION_SUCCESS ="002";
	public static final String ACCOUNT_CREATION_MESSAGE ="ACCOUNT SUCCESSFULLY CREATED";
	
    public static final String ACCOUNT_NOT_EXIST_CODE="003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE="USER WITH THE PROVIDED ACCOUNT NUMBER DOES'NT EXISTS!";
    
    public static final String ACCOUNT_FOUND_CODE="004";
    public static final String ACCOUNT_FOUND_MESSAGE="USER FOUND";
    
    public static final String ACCOUNT_CREDITED_SUCCESS="005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE="CREDITED SUCCESSFULLY";
    
    
    public static final String INSUFFICIENT_BALANCE_CODE="006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE="INSUFFICIENT FUNDS";
    
    public static final String ACCOUNT_DEBITED_SUCCESS="007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE="DEBITED SUCCESFULLY";
    
    public static final String TRANSFER_SUCCESSFULL_CODE="008";
    public static final String TRANSFER_SUCCESSFULL_MESSAGE="TRANSFER SUCCESSFULL";

    public static final String LOGIN_SUCCESSFULL_CODE="009";
    public static final String LOGIN_SUCCESSFULL_MESSAGE="LOGIN SUCESSFULL";
    
	public static String generateAccountNumber() {

		// 2024 + random Six Digits

		Year currentYear = Year.now();

		int min = 100000;
		int max = 999999;

		
		// Generate Number between min and max
		
		int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

		String year = String.valueOf(currentYear);

		String randomNumber = String.valueOf(randNumber);

		StringBuilder accountNumber = new StringBuilder();
		
		
		return accountNumber.append(year).append(randomNumber).toString();

	}
}
