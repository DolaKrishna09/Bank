package com.spring.banking_management_system.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {
	
	private String accountName;
	
	private String accountNumber;
	
	private BigDecimal accountBalance;
	

	

}
