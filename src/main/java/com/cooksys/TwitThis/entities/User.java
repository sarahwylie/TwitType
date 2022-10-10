package com.cooksys.TwitThis.entities;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.*;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user_table")
public class User {

	  @Id
	  @GeneratedValue
	  private long id;
	
	  @Embedded
	  private Credentials credentials;
	  
	  @Embedded
	  private Profile profile;

	  @Column(nullable = false)
	  private boolean deleted = false;

	  @CreationTimestamp
	  @Column(nullable = false)
	  private Timestamp joined;
	   
	  @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
	  @JsonIgnoreProperties("author")
	  private List<Tweet> tweets;
	 
	  @ManyToMany(cascade = CascadeType.REMOVE)
	  private List<Tweet> likedTweets;
	  
	  @ManyToMany(cascade = CascadeType.REMOVE)
	  private List<User> followers;
	  
	  @ManyToMany(mappedBy = "followers", cascade = CascadeType.REMOVE)
	  private List<User> following;
	  
	  @ManyToMany(mappedBy = "userMentioned", cascade = CascadeType.REMOVE)
	  private List<Tweet> mentions;

}
