package com.cooksys.TwitThis.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.TwitThis.DTOs.CredentialsDto;
import com.cooksys.TwitThis.DTOs.TweetResponseDto;
import com.cooksys.TwitThis.DTOs.UserRequestDto;
import com.cooksys.TwitThis.DTOs.UserResponseDto;
import com.cooksys.TwitThis.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;
	
	//GET users
	@GetMapping
	public List<UserResponseDto> getAllUsers() {
		return userService.getAllUsers();
	}

	//POST users
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponseDto createNewUser(@RequestBody UserRequestDto userRequestDto) {
		return userService.createNewUser(userRequestDto);
	}
	
	//GET users/@{username}
	@GetMapping("/@{username}")
	public UserResponseDto getUser(@PathVariable String username) {
		return userService.getUser(username);
	}
	
	//PATCH users/@{username}
	@PatchMapping("/@{username}")
	public UserResponseDto updateUser(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
		return userService.updateUser(username, userRequestDto);
	}
	
	//DELETE users/@{username}
	@DeleteMapping("/@{username}")
	public UserResponseDto deleteUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		return userService.deleteUser(username, credentialsDto);
	}
	
	//POST users/@{username}/follow
	@PostMapping("/@{username}/follow")
	@ResponseStatus(HttpStatus.CREATED)
	public String followUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		return userService.followUser(username, credentialsDto);
	}
	
	//POST users/@{username}/unfollow
	@PostMapping("/@{username}/unfollow")
	@ResponseStatus(HttpStatus.CREATED)
	public String unfollowUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
		return userService.unfollowUser(username, credentialsDto);
	}
	
	//GET users/@{username}/feed
	@GetMapping("/@{username}/feed")
	public List<TweetResponseDto> getUserFeed(@PathVariable String username){
		return userService.getUserFeed(username);
	}
	
	//GET users/@{username}/tweets
	@GetMapping("/@{username}/tweets")
	public List<TweetResponseDto> getUserTweets(@PathVariable String username){
		return userService.getUserTweets(username);
	}
	
	//GET users/@{username}/mentions
	@GetMapping("/@{username}/mentions")
	public List<TweetResponseDto> getUserMentions(@PathVariable String username){
		return userService.getUserMentions(username);
	}
	
	//GET users/@{username}/followers
	@GetMapping("/@{username}/followers")
	public List<UserResponseDto> getUserFollowers(@PathVariable String username){
		return userService.getUserFollowers(username);
	}
	
		//GET users/@{username}/following
	@GetMapping("/@{username}/following")
	public List<UserResponseDto> getUserFollowing(@PathVariable String username){
		return userService.getUserFollowing(username);
	}
	
}
