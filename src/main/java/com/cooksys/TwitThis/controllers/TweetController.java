package com.cooksys.TwitThis.controllers;

import java.util.List;

import com.cooksys.TwitThis.entities.Tweet;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.cooksys.TwitThis.DTOs.ContextDto;
import com.cooksys.TwitThis.DTOs.CredentialsDto;
import com.cooksys.TwitThis.DTOs.HashtagDto;
import com.cooksys.TwitThis.DTOs.TweetRequestDto;
import com.cooksys.TwitThis.DTOs.TweetResponseDto;
import com.cooksys.TwitThis.DTOs.UserResponseDto;
import com.cooksys.TwitThis.services.TweetService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
	
	private final TweetService tweetService;
	
	//GET tweets
	@GetMapping
	public List<TweetResponseDto> getAllTweets(){
		return tweetService.getAllTweets();
	}

	//POST tweets
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createTweet(tweetRequestDto);
	}
	
	//GET tweets/{id}
	@GetMapping("/{id}")
	public TweetResponseDto getTweetById(@PathVariable Long id) {
		return tweetService.getTweetById(id);
	}
	
	//DELETE tweets/{id}
	@DeleteMapping("/{id}")
	public TweetResponseDto deleteTweetById(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.deleteTweetById(id, credentialsDto);
	}
	
	//POST tweets/{id}/like
	@PostMapping("/{id}/like")
	@ResponseStatus(HttpStatus.CREATED)
	public String likeTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.likeTweet(id, credentialsDto);
	}
	
	//POST tweets/{id}/reply
	@PostMapping("/{id}/reply")
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto replyToTweetById(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.replyToTweetById(id, tweetRequestDto);
	}
	
	//POST tweets/{id}/repost
	@PostMapping("/{id}/repost")
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto repostToTweetById(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto, TweetRequestDto repostedTweet) {
		return tweetService.repostToTweetById(id, credentialsDto,repostedTweet);
	}
	
	//GET tweets/{id}/tags
	@GetMapping("/{id}/tags")
	public List<HashtagDto> getAllHashtagsFromTweet(@PathVariable Long id){
		return tweetService.getAllHashtagsFromTweet(id);
	}
	
	//GET tweets/{id}/likes
	@GetMapping("/{id}/likes")
	public List<UserResponseDto> getAllUserLikesFromTweet(@PathVariable Long id){
		return tweetService.getAllUserLikesFromTweet(id);
	}
	
	//GET tweets/{id}/context
	@GetMapping("/{id}/context")
	public ContextDto getContextOfTweet(@PathVariable Long id){
		return tweetService.getContextOfTweet(id);
	}
	
	//GET tweets/{id}/replies
	@GetMapping("/{id}/replies")
	public List<TweetResponseDto> getAllRepliesToTweet(@PathVariable Long id){
		return tweetService.getAllRepliesToTweet(id);
	}
	
	//GET tweets/{id}/reposts
	@GetMapping("/{id}/reposts")
	public List<TweetResponseDto> getAllRepostsToTweet(@PathVariable Long id){
		return tweetService.getAllRepostsToTweet(id);
	}
	
	//GET tweets/{id}/mentions
	@GetMapping("/{id}/mentions")
	public List<UserResponseDto> getAllUsersMentionedInTweet(@PathVariable Long id){
		return tweetService.getAllUsersMentionedInTweet(id);
	}

}
