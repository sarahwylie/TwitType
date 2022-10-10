package com.cooksys.TwitThis.DTOs;

import com.cooksys.TwitThis.entities.Credentials;
import com.cooksys.TwitThis.entities.Profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserRequestDto {

	private Credentials credentials;
	
	private Profile profile;

}
