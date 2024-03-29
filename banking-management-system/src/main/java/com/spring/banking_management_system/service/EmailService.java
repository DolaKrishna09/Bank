package com.spring.banking_management_system.service;

import com.spring.banking_management_system.dto.EmailDetails;

public interface EmailService {

	
	void sendEmailAlert(EmailDetails emailDetails);

	
}
