package com.cooksys.TwitThis.DTOs;

import com.cooksys.TwitThis.entities.Profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {
	
	private String username;
	
	private Profile profile;
	
	private Timestamp joined;

}
