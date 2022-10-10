package com.cooksys.TwitThis.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cooksys.TwitThis.entities.Credentials;
import com.cooksys.TwitThis.entities.Profile;
import com.cooksys.TwitThis.entities.Tweet;
import com.cooksys.TwitThis.entities.User;
import com.cooksys.TwitThis.exceptions.BadRequestException;
import com.cooksys.TwitThis.exceptions.NotAuthorizedException;
import com.cooksys.TwitThis.exceptions.NotFoundException;
//import com.cooksys.TwitThis.mappers.CredentialsMapper;
//import com.cooksys.TwitThis.mappers.ProfileMapper;
import com.cooksys.TwitThis.mappers.TweetMapper;
import com.cooksys.TwitThis.mappers.UserMapper;
import com.cooksys.TwitThis.repositories.UserRepository;
import org.springframework.stereotype.Service;

import com.cooksys.TwitThis.DTOs.CredentialsDto;
import com.cooksys.TwitThis.DTOs.TweetResponseDto;
import com.cooksys.TwitThis.DTOs.UserRequestDto;
import com.cooksys.TwitThis.DTOs.UserResponseDto;
import com.cooksys.TwitThis.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
//	private final CredentialsMapper credentialsMapper;
//	private final ProfileMapper profileMapper;
	private final TweetMapper tweetMapper;
	
	//Method to get user by username - built in Error Handling
	private User checkUsername(String username) {
		Optional<User> optionalUser = userRepository.findByCredentials_Username(username);
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("No user found with username " + username);
		}
		return optionalUser.get();
	}

	//Method to check if user was deleted - built in Error Handling
	private void isUserDeleted(String username) {
		User checkUser = checkUsername(username);
		if (checkUser.isDeleted()) {
			throw new BadRequestException("User " + username + " has been deleted!");
		}
	}

	//Method to check if the credentials are correct - built in Error Handling
	private void authenticate(String username, String password) {
		User userToCheck = userRepository.findByCredentials_UsernameAndCredentials_Password(username, password);	
		if (userToCheck == null) {
			throw new NotAuthorizedException("username or password is incorrect");
		}
	}
	
	private void checkReqBodyCredentials(CredentialsDto credentialsDto) {
		if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
			throw new BadRequestException("Please enter your username and password!");
		}
	}

// END POINTS ************************************************************************************************

	//DONE - WORKS
	@Override
	public List<UserResponseDto> getAllUsers() {
		return userMapper.entitiesToDtos(userRepository.findAll());
	}
	
	//DONE - WORKS
	@Override
	public UserResponseDto createNewUser(UserRequestDto userRequestDto) {
		if(userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
			throw new BadRequestException("Please enter new user information");
		}			
		Credentials newCredentials = userRequestDto.getCredentials();
		Profile newProfile = userRequestDto.getProfile();
				
		Optional<User> optionalUser = userRepository.findByCredentials_Username(newCredentials.getUsername());
		//check if user already exist & if user was deleted then reactivate user
		if (optionalUser.isPresent()) {
			User existedUser = optionalUser.get();

			if(!existedUser.isDeleted()) {
				throw new BadRequestException("User " + newCredentials.getUsername() + " already exist!");
			}
			existedUser.setDeleted(false);
			userRepository.saveAndFlush(existedUser);
			return userMapper.entityToDto(existedUser);
		}
//		//check if all necessary fields are entered
		if (newCredentials.getUsername() == null || newCredentials.getPassword() == null || newProfile.getEmail() == null) {
			throw new BadRequestException("Please enter your username, password, and email!");
		}
		//adding info to create a new user
		User userToSave = userRepository.saveAndFlush(userMapper.requestDtoToEntity(userRequestDto));
		return userMapper.entityToDto(userToSave);
	}

	//DONE - WORKS
	@Override
	public UserResponseDto getUser(String username) {
		User singleUser = checkUsername(username);
		return userMapper.entityToDto(singleUser);
	}

	//DONE - WORKS
	@Override
	public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
		if(userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
			throw new BadRequestException("Please enter new user information");
		}
				
		Credentials newCredentials = userRequestDto.getCredentials();
		Profile newProfile = userRequestDto.getProfile();
		
		//check if user exists, is deleted and authenticate
		User userToUpdate = checkUsername(newCredentials.getUsername());		
		authenticate(newCredentials.getUsername(), newCredentials.getPassword());
		isUserDeleted(newCredentials.getUsername());
		
		if (newProfile != null) {
			Profile profileToUpdate = userToUpdate.getProfile();
			if(newProfile.getFirstName() != null) {
				profileToUpdate.setFirstName(newProfile.getFirstName());
			}
			if(newProfile.getLastName() != null) {
				profileToUpdate.setLastName(newProfile.getLastName());
			}
			if(newProfile.getEmail() != null) {
				profileToUpdate.setEmail(newProfile.getEmail());
			}
			if(newProfile.getPhone() != null) {
				profileToUpdate.setPhone(newProfile.getPhone());
			}
			userToUpdate.setProfile(profileToUpdate);
		}		
		userRepository.saveAndFlush(userToUpdate);
		return userMapper.entityToDto(userToUpdate);
	}

	//DONE - WORKS
	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
		checkReqBodyCredentials(credentialsDto);
		User userToDelete = checkUsername(username);
		authenticate(credentialsDto.getUsername(), credentialsDto.getPassword());
		isUserDeleted(username);

		userToDelete.setDeleted(true);
		userRepository.save(userToDelete);
		return userMapper.entityToDto(userToDelete);
	}

	
	//DONE - WORKS
	@Override
	public String followUser(String username, CredentialsDto credentialsDto) {
		checkReqBodyCredentials(credentialsDto);

		//check if userToFollow exist & authenticate userFollowing
		User userToFollow = checkUsername(username);
		isUserDeleted(username);
		User userFollowing = checkUsername(credentialsDto.getUsername());
		authenticate(credentialsDto.getUsername(), credentialsDto.getPassword());

		//check if user is already following the other user
		List<User> peopleUserFollows = userFollowing.getFollowing();
		for (User peopleUserFollow : peopleUserFollows) {
			if (peopleUserFollow == userToFollow) {
				throw new BadRequestException("You are already following " + username);
			}
		}
		
		//add userToFollow to the other user following list and save into the database
		peopleUserFollows.add(userToFollow);
		userFollowing.setFollowing(peopleUserFollows);
		userRepository.save(userFollowing);
		
		//add userFollowing to userToFollow follower list
		List<User> userFollowings = userToFollow.getFollowers();
		userFollowings.add(userFollowing);
		userToFollow.setFollowers(userFollowings);
		userRepository.save(userToFollow);
		
		return "You are now following " + username;
	}

	//DONE - WORKS
	@Override
	public String unfollowUser(String username, CredentialsDto credentialsDto) {
		checkReqBodyCredentials(credentialsDto);
		
		//check if userToUnFollow exist & authenticate userFollowing
		User userToUnFollow = checkUsername(username);
		isUserDeleted(username);
		User userFollowing = checkUsername(credentialsDto.getUsername());
		authenticate(credentialsDto.getUsername(), credentialsDto.getPassword());

		//check if user-user following relationship
		List<User> peopleUserFollows = userFollowing.getFollowing();
		List<User> userFollowings = userToUnFollow.getFollowers();
		for(int i = 0; i < peopleUserFollows.size(); i++){
			User currUser = peopleUserFollows.get(i);
			if ( currUser == userToUnFollow ){
				peopleUserFollows.remove(currUser);
				userFollowing.setFollowing(peopleUserFollows);
				userRepository.save(userFollowing);
				
				userFollowings.remove(userFollowing);
				userToUnFollow.setFollowers(userFollowings);
				userRepository.save(userToUnFollow);

				return "You unfollowed " + username;
			}
		}
		throw new BadRequestException("You are NOT already following " + username);
	}

	//The tweets should appear in reverse-chronological order. 
	//DONE - WORKS
	@Override
	public List<TweetResponseDto> getUserFeed(String username) {
		//Check user exists and is active
		User userToCheck = checkUsername(username);
		isUserDeleted(username);
		
		//get all nonDeleted users that user is following
		List<User> userFollowing = userToCheck.getFollowing();
		List<User> activeFollowing = new ArrayList<>();
		for (User currUser : userFollowing) {
			if (!currUser.isDeleted()) {
				activeFollowing.add(currUser);
			}
		}
		
		//get all everybody's tweets
		List<Tweet> allTweets = userToCheck.getTweets();
		for (User follower : activeFollowing) {
			allTweets.addAll(follower.getTweets());
		}
		
		//get all nonDeleted tweets
		List<Tweet> nonDeletedTweets = new ArrayList<>();
		for (Tweet currTweet : allTweets) {
			if (!currTweet.isDeleted()) {
				nonDeletedTweets.add(currTweet);
			}
		}
		
//		//get all replies and reposts
//		for(Tweet currTweet : nonDeletedTweets) {
//			nonDeletedTweets.addAll(currTweet.getReplies());
//			nonDeletedTweets.addAll(currTweet.getReposts());
//		}
	
		return tweetMapper.entitiesToDtos(nonDeletedTweets);
	}

	//The tweets should appear in reverse-chronological order.
	//DONE - WORKS
	@Override
	public List<TweetResponseDto> getUserTweets(String username) {
		User userToCheck = checkUsername(username);
		isUserDeleted(username);
		
		List<Tweet> userTweets = userToCheck.getTweets();
		List<Tweet> nonDeletedTweets = new ArrayList<>();

		for (Tweet currTweet : userTweets) {
			if (!currTweet.isDeleted()) {
				nonDeletedTweets.add(currTweet);
			}
		}
		return tweetMapper.entitiesToDtos(nonDeletedTweets);
	}

	//The tweets should appear in reverse-chronological order.
	//DONE - WORKS
	@Override
	public List<TweetResponseDto> getUserMentions(String username) {
		User userToCheck = checkUsername(username);
		isUserDeleted(username);
		
		List<Tweet> userMentionedTweets = userToCheck.getMentions();
		List<Tweet> nonDeletedTweets = new ArrayList<>();

		for (Tweet currTweet : userMentionedTweets) {
			if (!currTweet.isDeleted()) {
				nonDeletedTweets.add(currTweet);
			}
		}
	
		return tweetMapper.entitiesToDtos(nonDeletedTweets);
	}

	//DONE - WORKS
	@Override
	public List<UserResponseDto> getUserFollowers(String username) {
		User userToCheck = checkUsername(username);
		isUserDeleted(username);
		
		List<User> userFollowers = userToCheck.getFollowers();
		List<User> activeFollowers = new ArrayList<>();

		for (User currUser : userFollowers) {
			if (!currUser.isDeleted()) {
				activeFollowers.add(currUser);
			}
		}
		userMapper.entitiesToDtos(activeFollowers);

//		for(int i=0; i<userFollowers.size(); i++) {
//			if(userFollowers.get(i).isDeleted() == false) {
//				activeFollowers.add(userFollowers.get(i));
//			}
//		}
		return userMapper.entitiesToDtos(userFollowers);
	}

	//DONE - WORKS
	@Override
	public List<UserResponseDto> getUserFollowing(String username) {
		User userToCheck = checkUsername(username);
		isUserDeleted(username);
		
		List<User> userFollowing = userToCheck.getFollowing();
		List<User> activeFollowing = new ArrayList<>();

		for (User currUser : userFollowing) {
			if (!currUser.isDeleted()) {
				activeFollowing.add(currUser);
			}
		}
		userMapper.entitiesToDtos(activeFollowing);

//		for(int i=0; i<userFollowing.size(); i++) {
//			User currUser = userFollowing.get(i);
//			if(currUser.isDeleted() == false) {
//				activeFollowing.add(currUser);
//			}
//		}
		return userMapper.entitiesToDtos(userFollowing);
	}
}
