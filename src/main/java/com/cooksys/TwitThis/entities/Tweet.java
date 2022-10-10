package com.cooksys.TwitThis.entities;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Tweet {

	  @Id
	  @GeneratedValue
	  private Long id;

	  @ManyToOne
	  @JsonIgnoreProperties("tweets")
	  @JoinColumn(name = "tweets_author")
	  private User author;

	  @CreationTimestamp
	  private Timestamp posted;
	  
	  @Column(nullable = false)
	  private boolean deleted = false;
	  
	  @Column(nullable = false)
	  private String content;
	  
	  @ManyToMany(mappedBy = "likedTweets", cascade = CascadeType.REMOVE)
	  private List<User> likes;

	  @ManyToMany( cascade = CascadeType.REMOVE)
	  private List<Hashtag> hashtags;
	  
	  @ManyToMany(cascade = CascadeType.REMOVE)
	  private List<User> userMentioned;
	  
	  @ManyToOne(cascade = CascadeType.REMOVE)
	  @JsonIgnoreProperties("replies")
	  @JoinColumn(name = "tweets_replies")
	  private Tweet inReplyTo;
	  
	  @OneToMany(mappedBy = "inReplyTo", cascade = CascadeType.REMOVE)
	  @JsonIgnoreProperties("inReplyTo")
	  private List<Tweet> replies;
	  
	  @ManyToOne( cascade = CascadeType.REMOVE)
	  @JsonIgnoreProperties("reposts")
	  @JoinColumn(name = "tweets_reposts")
	  private Tweet repostOf;
	  
	  @OneToMany(mappedBy = "repostOf", cascade = CascadeType.REMOVE)
	  @JsonIgnoreProperties("repostOf")
	  private List<Tweet> reposts;
}
