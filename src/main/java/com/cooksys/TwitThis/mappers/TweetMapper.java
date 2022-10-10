package com.cooksys.TwitThis.mappers;

import java.util.List;
import com.cooksys.TwitThis.DTOs.TweetRequestDto;
import com.cooksys.TwitThis.DTOs.TweetResponseDto;
import com.cooksys.TwitThis.entities.Tweet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, HashtagMapper.class} )
public interface TweetMapper {

//    TweetRequestDto entityToDto(Tweet entity);

    TweetResponseDto entityToDto(Tweet entity);

    Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);
    //THEORY: Grabs List of replies, other tweets, ect...
    List<TweetResponseDto> entitiesToDtos(List<Tweet> entities);

    List<Tweet> dtoToEntity(List<TweetResponseDto> entities);

    Tweet tweetResponseDtoToTweet(TweetResponseDto tweetResponseDto);

}