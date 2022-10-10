package com.cooksys.TwitThis.mappers;

import com.cooksys.TwitThis.DTOs.CredentialsDto;
import com.cooksys.TwitThis.entities.Credentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

    CredentialsDto entityToDto(Credentials entity);

    Credentials requestDtoToEntity(CredentialsDto credentialsDto);
}
