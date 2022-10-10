package com.cooksys.TwitThis.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotAuthorizedException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7908005250267931909L;

	private String message;

}
