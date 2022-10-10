package com.cooksys.TwitThis.DTOs;

import com.cooksys.TwitThis.entities.Credentials;
import com.cooksys.TwitThis.entities.Tweet;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TweetRequestDto {

    private String content;

//    private CredentialsDto credentialsDto;
    
    private Credentials credentials;

    private Tweet repostOf;

}
