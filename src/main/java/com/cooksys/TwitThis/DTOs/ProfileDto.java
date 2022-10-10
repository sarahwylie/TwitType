package com.cooksys.TwitThis.DTOs;

//import com.cooksys.TwitThis.entities.Profile;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProfileDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

}
