package com.cooksys.TwitThis.services;

import java.util.List;

import com.cooksys.TwitThis.DTOs.HashtagDto;
import com.cooksys.TwitThis.DTOs.TweetResponseDto;

public interface HashtagService {

	List<HashtagDto> getAllHashtags();

	List<TweetResponseDto> getAllLabeledTweets(String label);

}
