package com.spring.banking_management_system.service;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.banking_management_system.config.JwtTokenProvider;
import com.spring.banking_management_system.dto.AccountInfo;
import com.spring.banking_management_system.dto.BankResponse;
import com.spring.banking_management_system.dto.CreditDebitRequest;
import com.spring.banking_management_system.dto.EmailDetails;
import com.spring.banking_management_system.dto.EnquiryRequest;
import com.spring.banking_management_system.dto.LoginDTO;
import com.spring.banking_management_system.dto.TransferRequest;
import com.spring.banking_management_system.dto.UserRequest;
import com.spring.banking_management_system.entity.User;
import com.spring.banking_management_system.repository.UserRepository;
import com.spring.banking_management_system.utils.AccountUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	@Autowired
	AuthenticationManager authenticationManger;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	
	
	public BankResponse createAccount(UserRequest userRequest) {
		// Creating an account - saving the new user data in db
		// checking if user already has an account

		if (userRepository.existsByEmail(userRequest.getEmail())) {

			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE).accountInfo(null).build();

		} else {
			User newUser = User.builder().firstName(userRequest.getFirstName()).lastName(userRequest.getLastName())
					.otherName(userRequest.getOtherName()).gender(userRequest.getGender())
					.address(userRequest.getAddress()).state(userRequest.getState())
					.accountNumber(AccountUtils.generateAccountNumber()).accountBalance(BigDecimal.ZERO)
					.email(userRequest.getEmail())
					.password(passwordEncoder.encode(userRequest.getPassword()))
					.phoneNumber(userRequest.getPhoneNumber())
					.alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
					.role(userRequest.getRole())
					.status("ACTIVE").build();

			User savedUser = userRepository.save(newUser);

			// For Email
			EmailDetails emailDetails = EmailDetails.builder().recipients(savedUser.getEmail())
					.subject("ACCOUNT CREATION")
					.messageBody("Congratulations🎉🎉🎉, Your Account Has been Created Successfully \n"
							+ "Your Account Details:\n" + "Account Holder Name: " + savedUser.getFirstName() + " "
							+ savedUser.getLastName() + " " + savedUser.getOtherName() + "\n" + "Account Number: "
							+ savedUser.getAccountNumber())

					.build();

			emailService.sendEmailAlert(emailDetails);

			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
					.responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
					.accountInfo(
							AccountInfo.builder().accountBalance(savedUser.getAccountBalance())
									.accountNumber(savedUser.getAccountNumber()).accountName(savedUser.getFirstName()
											+ " " + savedUser.getLastName() + " " + savedUser.getOtherName())
									.build())
					.build();

		}
	}

	
	public BankResponse login(LoginDTO loginDTO) {
		
		Authentication authentication= null;
		
		authentication = authenticationManger.authenticate(
				
				new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
		
		
		EmailDetails loginAlert = EmailDetails.builder()
				.subject("You are Logged in")
				.recipients(loginDTO.getEmail())
				.messageBody("You are logged into your account. If not you, Please contact your bank")
				.build();
		
		emailService.sendEmailAlert(loginAlert); 
		return BankResponse.builder()
				.responseCode(AccountUtils.LOGIN_SUCCESSFULL_MESSAGE)
				.responseMessage(jwtTokenProvider.generateToken(authentication))
				.build();
		
	}
	
	

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest request) {
		boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExists) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();

		}
		User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());

		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
				.responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
				.accountInfo(AccountInfo.builder().accountBalance(foundUser.getAccountBalance())
						.accountNumber(foundUser.getAccountNumber()).accountName(foundUser.getFirstName() + " "
								+ foundUser.getLastName() + " " + foundUser.getOtherName())
						.build())
				.build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest request) {
		boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExists) {
			return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
		} else {
			User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
			return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
		}
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest request) {
		boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExists) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();

		}

		else {
			User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());

			userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));

			userRepository.save(userToCredit);

			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
					.responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
					.accountInfo(AccountInfo.builder()
							.accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + " "
									+ userToCredit.getOtherName())
							.accountNumber(userToCredit.getAccountNumber())
							.accountBalance(userToCredit.getAccountBalance()).build())
					.build();

		}

	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest request) {
		boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExists) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();

		}
		User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
		BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();

		BigInteger debitAmount = request.getAmount().toBigInteger();

		if (availableBalance.intValue() < debitAmount.intValue()) {

			return BankResponse.builder().responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE).accountInfo(null).build();
		} else {
			userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
			userRepository.save(userToDebit);

			return BankResponse.builder()

					.responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
					.responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
					.accountInfo(AccountInfo.builder().accountNumber(request.getAccountNumber())
							.accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " "
									+ userToDebit.getOtherName())
							.accountBalance(userToDebit.getAccountBalance()).build())
					.build();

		}

	}

	@Override
	public BankResponse transfer(TransferRequest request) {
	    // Check if destination account exists
	    boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
	    if (!isDestinationAccountExist) {
	        return BankResponse.builder()
	            .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
	            .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
	            .accountInfo(null)
	            .build();
	    }

	    // Find the source account
	    User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());

	    // Check if the source account has sufficient balance
	    if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
	        return BankResponse.builder()
	            .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
	            .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
	            .accountInfo(null)
	            .build();
	    }

	    // Debit the source account
	    sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
	    userRepository.save(sourceAccountUser);

	    // Send debit alert email
	    String sourceUserName = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName() + " " + sourceAccountUser.getOtherName();
	    EmailDetails debitAlert = EmailDetails.builder()
	            .subject("DEBIT ALERT")
	            .recipients(sourceAccountUser.getEmail())
	            .messageBody("Rupees " + request.getAmount() + " has been debited from your account\n" +
	                         "Your available balance is " + sourceAccountUser.getAccountBalance())
	            .build();
	    emailService.sendEmailAlert(debitAlert);
	    

	    // Credit the destination account
	    User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
	    destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
	    userRepository.save(destinationAccountUser);
	    
	    

	    // Send credit alert email
	    EmailDetails creditAlert = EmailDetails.builder()
	            .subject("CREDIT ALERT")
	            .recipients(destinationAccountUser.getEmail())
	            .messageBody("Rupees " + request.getAmount() + " has been sent to your account from " + sourceUserName +
	                         "\nYour Current balance is " + destinationAccountUser.getAccountBalance())
	            .build();
	    emailService.sendEmailAlert(creditAlert);
	    
	    

	    return BankResponse.builder()
	            .responseCode(AccountUtils.TRANSFER_SUCCESSFULL_CODE)
	            .responseMessage(AccountUtils.TRANSFER_SUCCESSFULL_MESSAGE)
	            .accountInfo(null)
	            .build();
	}
	
	
	

}
