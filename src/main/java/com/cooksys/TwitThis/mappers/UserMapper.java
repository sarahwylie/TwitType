package com.cooksys.TwitThis.mappers;

import com.cooksys.TwitThis.DTOs.UserRequestDto;
import com.cooksys.TwitThis.DTOs.UserResponseDto;
import com.cooksys.TwitThis.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CredentialsMapper.class, ProfileMapper.class, TweetMapper.class} )
public interface UserMapper {

    @Mapping(source="credentials.username", target="username")
    UserResponseDto entityToDto(User entity);
	
	User requestDtoToEntity(UserRequestDto userRequestDto);
    
	List<UserResponseDto> entitiesToDtos(List<User> entities);

}
