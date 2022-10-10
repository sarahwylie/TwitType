package com.cooksys.TwitThis.DTOs;

import com.cooksys.TwitThis.entities.Tweet;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class TweetResponseDto {

    private Long id;

    private UserResponseDto author;

    private Timestamp posted;

    private String content;

//    private Tweet inReplyTo;
//
//    private Tweet repostOf;

}
