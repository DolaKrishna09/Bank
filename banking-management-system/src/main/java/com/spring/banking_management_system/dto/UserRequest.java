package com.spring.banking_management_system.dto;


import com.spring.banking_management_system.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

	private String firstName;

	private String lastName;

	private String otherName;

	private String gender;

	private String address;

	private String state;

	private String email;
	
	private String password;

	private String phoneNumber;

	private String alternativePhoneNumber;
	
	private Role role;

	

}
