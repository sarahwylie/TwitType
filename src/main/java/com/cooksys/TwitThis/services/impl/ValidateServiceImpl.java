package com.cooksys.TwitThis.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.TwitThis.entities.Hashtag;
import com.cooksys.TwitThis.entities.User;
import com.cooksys.TwitThis.repositories.HashtagRepository;
import com.cooksys.TwitThis.repositories.UserRepository;
import com.cooksys.TwitThis.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

	private final HashtagRepository hashtagRepository;
	private final UserRepository userRepository;

	@Override
	public boolean checkTagExistence(String label) {
		Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);
		return optionalHashtag.isPresent();
	}

	//Checks whether a given username exists - boolean
	@Override
	public boolean checkUserExistence(String username) {
		Optional<User> optionalUser = userRepository.findByCredentials_Username(username);
		return optionalUser.isPresent();
	}


	//Checks whether a given username is available - boolean
	@Override
	public boolean checkUsernameAvailability(String username) {
		Optional<User> optionalUser = userRepository.findByCredentials_Username(username);
		if (optionalUser.isPresent()) {
			return false;
		}
		return true;
	}

}