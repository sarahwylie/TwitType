package com.cooksys.TwitThis.entities;

import javax.persistence.Embeddable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Embeddable
public class Profile {
	
//	@Column(nullable = false)
	private String firstName;
	
//	@Column(nullable = false)
	private String lastName;
	  
	private String email;
	  
	private String phone;

}
