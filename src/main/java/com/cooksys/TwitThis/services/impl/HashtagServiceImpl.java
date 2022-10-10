package com.cooksys.TwitThis.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cooksys.TwitThis.entities.Hashtag;
import com.cooksys.TwitThis.entities.User;
import com.cooksys.TwitThis.exceptions.NotFoundException;
import com.cooksys.TwitThis.mappers.HashtagMapper;
import com.cooksys.TwitThis.mappers.TweetMapper;
import com.cooksys.TwitThis.repositories.HashtagRepository;
import org.springframework.stereotype.Service;

import com.cooksys.TwitThis.entities.Tweet;
import com.cooksys.TwitThis.DTOs.HashtagDto;
import com.cooksys.TwitThis.DTOs.TweetResponseDto;
import com.cooksys.TwitThis.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
	private final HashtagRepository hashtagRepository;
	private final HashtagMapper hashtagMapper;
	private final TweetMapper tweetMapper;

	//WORKS - DONE
	@Override
	public List<HashtagDto> getAllHashtags() {
		return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
	}

	//WORKS - FIXED some logic - still need below (there's probably an easier way to do this)
	//The tweets should appear in reverse-chronological order.
	@Override
	public List<TweetResponseDto> getAllLabeledTweets(String label) {
		Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);
		if (optionalHashtag.isEmpty()) {
			throw new NotFoundException("No tweet found with hashtag label " + label);
		}
		Hashtag tag = optionalHashtag.get();
		
		List<Tweet> allTweetFromTags = tag.getTweets();
		List<Tweet> nonDeletedTweet = new ArrayList<>();
        for (Tweet currTweet : allTweetFromTags) {
            if (!currTweet.isDeleted()) {
                nonDeletedTweet.add(currTweet);
            }
        }

		return tweetMapper.entitiesToDtos(nonDeletedTweet);
	}
}
