package com.cooksys.TwitThis.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
//import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Embeddable
public class Credentials {

	@Column(unique=true, nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String password;

}
