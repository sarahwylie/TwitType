package com.cooksys.TwitThis.services;

import java.util.List;

import com.cooksys.TwitThis.DTOs.CredentialsDto;
import com.cooksys.TwitThis.DTOs.TweetResponseDto;
import com.cooksys.TwitThis.DTOs.UserRequestDto;
import com.cooksys.TwitThis.DTOs.UserResponseDto;

public interface UserService {

	List<UserResponseDto> getAllUsers();

	UserResponseDto createNewUser(UserRequestDto userRequestDto);

	UserResponseDto getUser(String username);

	UserResponseDto updateUser(String username, UserRequestDto userRequestDto);

	UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

	String followUser(String username, CredentialsDto credentialsDto);
	
	String unfollowUser(String username, CredentialsDto credentialsDto);

	List<TweetResponseDto> getUserFeed(String username);

	List<TweetResponseDto> getUserTweets(String username);

	List<TweetResponseDto> getUserMentions(String username);

	List<UserResponseDto> getUserFollowers(String username);

	List<UserResponseDto> getUserFollowing(String username);

}
