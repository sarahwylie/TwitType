package com.cooksys.TwitThis.mappers;

import com.cooksys.TwitThis.DTOs.HashtagDto;
import com.cooksys.TwitThis.entities.Hashtag;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TweetMapper.class} )
public interface HashtagMapper {

    Hashtag entityToDto(Hashtag entity);

//  Made a list object for multiple Hashtags in a single tweet.
  List<HashtagDto> entitiesToDtos(List<Hashtag> entities);
}
