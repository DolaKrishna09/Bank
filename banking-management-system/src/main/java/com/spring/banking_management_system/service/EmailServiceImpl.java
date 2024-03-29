package com.spring.banking_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.spring.banking_management_system.dto.EmailDetails;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javamailSender;

	@Value("${spring.mail.username}")
	private String senderEmail;

	public void sendEmailAlert(EmailDetails emailDetails) {

		try {

			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom(senderEmail);
			mailMessage.setTo(emailDetails.getRecipients());
			mailMessage.setText(emailDetails.getMessageBody());
			mailMessage.setSubject(emailDetails.getSubject());

			javamailSender.send(mailMessage);
			
			System.out.println("Mail sent Successfully");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
