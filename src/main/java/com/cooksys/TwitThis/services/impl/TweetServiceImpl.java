package com.cooksys.TwitThis.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.cooksys.TwitThis.DTOs.*;
import com.cooksys.TwitThis.entities.Credentials;
import com.cooksys.TwitThis.entities.Tweet;
import com.cooksys.TwitThis.entities.User;
import com.cooksys.TwitThis.mappers.CredentialsMapper;
import com.cooksys.TwitThis.mappers.TweetMapper;
import com.cooksys.TwitThis.mappers.HashtagMapper;
import com.cooksys.TwitThis.mappers.UserMapper;
import com.cooksys.TwitThis.repositories.HashtagRepository;
import com.cooksys.TwitThis.repositories.TweetRepository;
import com.cooksys.TwitThis.repositories.UserRepository;
import com.cooksys.TwitThis.services.TweetService;

import org.springframework.stereotype.Service;

import com.cooksys.TwitThis.exceptions.BadRequestException;
import com.cooksys.TwitThis.entities.Hashtag;
import com.cooksys.TwitThis.exceptions.NotAuthorizedException;
import com.cooksys.TwitThis.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final TweetRepository tweetRepository;
	private final UserRepository userRepository;
	private final HashtagRepository hashtagRepository;
	private final TweetMapper tweetMapper;
	private final HashtagMapper hashtagMapper;
	private final UserMapper userMapper;
	private final CredentialsMapper credentialsMapper;

	//getTweet by ID method with built-in Error Handling
	private Tweet getTweet(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findById(id);
		if(optionalTweet.isEmpty()) {
			throw new NotFoundException("Tweet with ID " + id + " does not exist");
		}
		return optionalTweet.get();
	}

	//Method to check if tweet was deleted with built-in Error Handling
	private boolean isTweetDeleted(Long id) {
		Tweet tweetToCheck = getTweet(id);
		if (tweetToCheck.isDeleted()) {
			throw new BadRequestException("Tweet ID " + id + " has been deleted!");
		}
		return false;
	}

	//Method to get user by username - built in Error Handling
	private User checkUsername(String username) {
		Optional<User> optionalUser = userRepository.findByCredentials_Username(username);
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("No user found with username" + username);
		}
		return optionalUser.get();
	}

	//Method to check if Hashtag exist
	private Hashtag checkHashtag(String tag) {
		Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(tag);
		if (optionalHashtag.isEmpty()) {
			Hashtag newHashtag = new Hashtag();
			newHashtag.setLabel(tag);
			hashtagRepository.save(newHashtag);
			return newHashtag;
		}
		return optionalHashtag.get();
	}

	//Method to check if the credentials are correct - built in Error Handling
	private void authenticate(String username, String password) {
		User userToCheck = userRepository.findByCredentials_UsernameAndCredentials_Password(username, password);
		if (userToCheck == null) {
			throw new NotAuthorizedException("username or password is incorrect");
		}
	}

//End points ****************************************************************************************

	@Override
	public List<TweetResponseDto> getAllTweets() {
		List<Tweet> allTweets = tweetRepository.findAll();
		allTweets.removeIf(Tweet :: isDeleted);
		allTweets.sort(Comparator.comparing(Tweet :: getPosted).reversed());
		return tweetMapper.entitiesToDtos(allTweets);
	}

	@Override
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
		//check for content
		if (tweetRequestDto == null || tweetRequestDto.getContent() == null || tweetRequestDto.getCredentials() == null) {
			throw new BadRequestException("You must have content and credentials in your request");
		}
		Tweet newTweet = tweetMapper.requestDtoToEntity(tweetRequestDto);

		//set up new author and check credentials
		Credentials poster = tweetRequestDto.getCredentials();
		if (poster.getPassword() == null|| poster.getUsername() == null) {
			throw new BadRequestException("You must enter your username and password.");
		}
		User writer = checkUsername(poster.getUsername());
		authenticate(poster.getUsername(), poster.getPassword());

		//Extract #hashtag and @username from content
		List<Hashtag> newHashtags = new ArrayList<>();
		List<User> newUsersMentioned = new ArrayList<>();
		String[] splitContent = newTweet.getContent().split(" ");
		for (String s : splitContent) {
			if (s.startsWith("#")) {
				String hashtag = s.substring(1);
				Hashtag hashtagToAdd = checkHashtag(hashtag);
				newHashtags.add(hashtagToAdd);
			}
			if (s.startsWith("@")) {
				String username = s.substring(1);
				User userToAdd = checkUsername(username);
				newUsersMentioned.add(userToAdd);
			}
		}

		//set info in new tweet
		Tweet newPost = new Tweet();
		newPost.setContent(newTweet.getContent());
		newPost.setAuthor(writer);
		newPost.setHashtags(newHashtags);
		newPost.setUserMentioned(newUsersMentioned);

		//save and return tweet
		Tweet publishedTweet = tweetRepository.save(newPost);

		//Established relationship between userMentioned and Tweet
		for (User currUser: newUsersMentioned) {
			List<Tweet> currTweets = currUser.getMentions();
			currTweets.add(publishedTweet);
			currUser.setMentions(currTweets);
			userRepository.save(currUser);
		}
		return tweetMapper.entityToDto(publishedTweet);
	}

	@Override
	public TweetResponseDto getTweetById(Long id) {
		Tweet tweetToGet = getTweet(id);
		return tweetMapper.entityToDto(tweetToGet);
	}

	@Override
	public TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto) {
		Tweet tweetToDelete = getTweet(id);
		isTweetDeleted(id);
		authenticate(credentialsDto.getUsername(), credentialsDto.getPassword());
		tweetToDelete.setDeleted(true);
		tweetRepository.saveAndFlush(tweetToDelete);
		return tweetMapper.entityToDto(tweetToDelete);
	}

	//DONE - WORKS
	@Override
	public String likeTweet(Long id, CredentialsDto credentialsDto) {
		//authenticate
		authenticate(credentialsDto.getUsername(), credentialsDto.getPassword());
		//get tweet and user
		Tweet tweetToLike = getTweet(id);
		isTweetDeleted(id);
		User userLikingTweet = checkUsername(credentialsDto.getUsername());
		//establishing relationships - setting likes on tweet
		List<User> tweetLikes = tweetToLike.getLikes();
		//check if user already liked a tweet
		for (User currUser : tweetLikes) {
			if (currUser.getCredentials() == userLikingTweet.getCredentials()) {
				return "You already liked this tweet!";
			}
		}
		tweetLikes.add(userLikingTweet);
		tweetToLike.setLikes(tweetLikes);
		tweetRepository.saveAndFlush(tweetToLike);

		//establishing relationships - setting tweets liked on user
		List<Tweet>  tweetsLiked = userLikingTweet.getLikedTweets();
		tweetsLiked.add(tweetToLike);
		userLikingTweet.setLikedTweets(tweetsLiked);
		userRepository.saveAndFlush(userLikingTweet);

		return "You liked a tweet!";
	}

	//DONE - WORKS
	@Override
	public TweetResponseDto replyToTweetById(Long id, TweetRequestDto tweetRequestDto) {
		//check if request body has all necessary info
		if (tweetRequestDto == null || tweetRequestDto.getContent() == null) {
			throw new BadRequestException("Content cannot be empty");
		}
		String content = tweetRequestDto.getContent();
		Credentials credentials = tweetRequestDto.getCredentials();
		if (credentials == null || credentials.getUsername() == null || credentials.getPassword() == null) {
			throw new BadRequestException("Please enter your username and password!");
		}
		//authenticate and check if original tweet & reply author exist
		authenticate(credentials.getUsername(), credentials.getPassword());
		User replyAuthor = checkUsername(credentials.getUsername());
		Tweet originalTweet = getTweet(id);
		isTweetDeleted(id);

		//Extract #hashtag and @username from content
		List<Hashtag> newHashtags = new ArrayList<>();
		List<User> newUsersMentioned = new ArrayList<>();
		String[] splitContent = content.split(" ");
		for (String s : splitContent) {
			if (s.startsWith("#")) {
				String hashtag = s.substring(1);
				Hashtag hashtagToAdd = checkHashtag(hashtag);
				newHashtags.add(hashtagToAdd);
			}
			if (s.startsWith("@")) {
				String username = s.substring(1);
				User userToAdd = checkUsername(username);
				newUsersMentioned.add(userToAdd);
			}
		}

		//create tweet(reply)
		Tweet newReply = new Tweet();
		//create inReplyTo relationship
		newReply.setInReplyTo(originalTweet);
		newReply.setContent(content);
		newReply.setAuthor(replyAuthor);
		newReply.setHashtags(newHashtags);
		newReply.setUserMentioned(newUsersMentioned);

		//return newly created tweet
		Tweet publishedReply = tweetRepository.save(newReply);

		//Established relationship between userMentioned and Tweet
		for (User currUser: newUsersMentioned) {
			List<Tweet> currTweets = currUser.getMentions();
			currTweets.add(publishedReply);
			currUser.setMentions(currTweets);
			userRepository.save(currUser);
		}

		//Established relationship between TweetReplyTo and TweetGettingReplied
		List<Tweet> originalTweetReplies = originalTweet.getReplies();
		originalTweetReplies.add(publishedReply);
		originalTweet.setReplies(originalTweetReplies);
		tweetRepository.save(originalTweet);

		return tweetMapper.entityToDto(publishedReply);
	}

	@Override
	public TweetResponseDto repostToTweetById(Long id, CredentialsDto credentialsDto, TweetRequestDto repostedTweet) {
		//get original tweet and check if it's deleted
		Tweet originalTweet = getTweet(id);
		isTweetDeleted(id);
		originalTweet.getReposts().add(tweetMapper.requestDtoToEntity(repostedTweet));
		tweetRepository.saveAndFlush(originalTweet);
		//get credentials and authenticate, verify that no information is blank
		authenticate(credentialsDto.getUsername(), credentialsDto.getPassword());
		if (credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
			throw new BadRequestException("Please enter your username and password!");
		}
		User reposter = checkUsername(credentialsDto.getUsername());

		repostedTweet.setCredentials(credentialsMapper.requestDtoToEntity(credentialsDto));
		//create tweet(repost)
		repostedTweet.setRepostOf(originalTweet);
		tweetRepository.saveAndFlush(tweetMapper.requestDtoToEntity(repostedTweet));
		reposter.getTweets().add(tweetRepository.saveAndFlush(tweetMapper.requestDtoToEntity(repostedTweet)));

//		newRepost.setHashtags(newHashtags);
		//return newly created tweet
		Tweet publishedRepost = tweetRepository.saveAndFlush(tweetMapper.requestDtoToEntity(repostedTweet));
		return tweetMapper.entityToDto(publishedRepost);
	}

	@Override
	public List<HashtagDto> getAllHashtagsFromTweet(Long id) {
		Tweet tweetToGet = getTweet(id);
		isTweetDeleted(id);
		List<Hashtag> tweetTags = tweetToGet.getHashtags();
		return hashtagMapper.entitiesToDtos(tweetTags);
	}

	@Override
	public List<UserResponseDto> getAllUserLikesFromTweet(Long id) {
		//get tweet by id
		Tweet activeTweet = getTweet(id);
		//send error if tweet is deleted or non-existent
		isTweetDeleted(id);
		List<User> tweetLikes = activeTweet.getLikes();
		return userMapper.entitiesToDtos(tweetLikes);
	}

	@Override
	public ContextDto getContextOfTweet(Long id) {
		//check if tweet is deleted
		isTweetDeleted(id);
		// pull out tweet
		Tweet targetedTweet = getTweet(id);
		//set up a new context for return values
		ContextDto targetedContext = new ContextDto();

		//get before tweets
		Tweet currentTweet = targetedTweet.getInReplyTo();
		List<Tweet> beforeTarget = new ArrayList<>();
		while (currentTweet != null) {
			if (isTweetDeleted(currentTweet.getId())) {
				currentTweet = currentTweet.getInReplyTo();
			}
			beforeTarget.add(currentTweet);
			currentTweet = currentTweet.getInReplyTo();
		}
		//get after tweets
		List<Tweet> afterTarget = targetedTweet.getReplies();

		//add the targetedTweet to the targetedContext
		targetedContext.setTarget(tweetMapper.entityToDto(targetedTweet));
		targetedContext.setAfter(tweetMapper.entitiesToDtos(afterTarget));
		targetedContext.setBefore(tweetMapper.entitiesToDtos(beforeTarget));
		return targetedContext;
	}

	@Override
	public List<TweetResponseDto> getAllRepliesToTweet(Long id) {
		Tweet targetTweet = getTweet(id);
		isTweetDeleted(targetTweet.getId());

		List<Tweet> replyList = targetTweet.getReplies();

		//Checks for deleted replies.
		List<Tweet> nonDeletedTweets = new ArrayList<>();
		for (Tweet currTweet : replyList) {
			if (!currTweet.isDeleted()) {
				nonDeletedTweets.add(currTweet);
			}
		}
		//Convert list to responseDTO
		return tweetMapper.entitiesToDtos(nonDeletedTweets);
	}

	@Override
	public List<TweetResponseDto> getAllRepostsToTweet(Long id) {
		//check if tweet exists
		Tweet targetTweet = getTweet(id);
		isTweetDeleted(targetTweet.getId());

		//List of all the reposts to the tweet
		List<Tweet> tweetRepostsList = targetTweet.getReposts();

		//Checks for deleted replies.
		List<Tweet> nonDeletedTweets = new ArrayList<>();
		for (Tweet currTweet : tweetRepostsList) {
			if (!currTweet.isDeleted()) {
				nonDeletedTweets.add(currTweet);
			}
		}
		//Convert list to responseDTO
		return tweetMapper.entitiesToDtos(nonDeletedTweets);
	}

	@Override
	public List<UserResponseDto> getAllUsersMentionedInTweet(Long id) {
		//get tweet by id & check is it is deleted
		Tweet activeTweet = getTweet(id);
		isTweetDeleted(id);

		//get all users mentioned & if anyone is deleted
		List<User> usersMentioned = activeTweet.getUserMentioned();
		List<User> activeUsersMentioned = new ArrayList<>();

		for (User currUser : usersMentioned) {
			if (!currUser.isDeleted()) {
				activeUsersMentioned.add(currUser);
			}
		}
		return userMapper.entitiesToDtos(activeUsersMentioned);
	}
}
