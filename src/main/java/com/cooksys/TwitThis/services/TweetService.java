package com.cooksys.TwitThis.services;

import java.util.List;

import com.cooksys.TwitThis.DTOs.ContextDto;
import com.cooksys.TwitThis.DTOs.CredentialsDto;
import com.cooksys.TwitThis.DTOs.HashtagDto;
import com.cooksys.TwitThis.DTOs.TweetRequestDto;
import com.cooksys.TwitThis.DTOs.TweetResponseDto;
import com.cooksys.TwitThis.DTOs.UserResponseDto;
import com.cooksys.TwitThis.entities.Tweet;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

	TweetResponseDto getTweetById(Long id);

	TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto);
	
	String likeTweet(Long id, CredentialsDto credentialsDto);

	TweetResponseDto replyToTweetById(Long id, TweetRequestDto tweetRequestDto);

	TweetResponseDto repostToTweetById(Long id, CredentialsDto credentialsDto, TweetRequestDto repostedTweet);

	List<HashtagDto> getAllHashtagsFromTweet(Long id);

	List<UserResponseDto> getAllUserLikesFromTweet(Long id);

	ContextDto getContextOfTweet(Long id);

	List<TweetResponseDto> getAllRepliesToTweet(Long id);

	List<TweetResponseDto> getAllRepostsToTweet(Long id);

	List<UserResponseDto> getAllUsersMentionedInTweet(Long id);
	
}
