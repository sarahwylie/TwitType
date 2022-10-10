package com.cooksys.TwitThis.mappers;

import com.cooksys.TwitThis.DTOs.ProfileDto;
import com.cooksys.TwitThis.entities.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDto entityToDto(Profile entity);

    Profile requestDtoToEntity(ProfileDto profileDto);

}
