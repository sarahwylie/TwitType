package com.cooksys.TwitThis.services;

public interface ValidateService {

	boolean checkTagExistence(String label);

	boolean checkUserExistence(String username);

	boolean checkUsernameAvailability(String username);

}
