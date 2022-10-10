package com.cooksys.TwitThis.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.cooksys.TwitThis.DTOs.HashtagDto;
import com.cooksys.TwitThis.DTOs.TweetResponseDto;
import com.cooksys.TwitThis.services.HashtagService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {
	private final HashtagService hashtagService;
	
	//GET tags - get all hashtags
	@GetMapping
	@ResponseBody
	public List<HashtagDto> getAllHashtags() {
		return hashtagService.getAllHashtags();
	}
	
	//GET tags/{label}
	@GetMapping("/{label}")
	@ResponseBody
	public List<TweetResponseDto> getAllLabeledTweets(@PathVariable String label) {
		return hashtagService.getAllLabeledTweets(label);
	}
}
