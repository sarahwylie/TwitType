package com.cooksys.TwitThis.entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {

	@Id
	@GeneratedValue
	private long id;
	
	@Column(unique=true, nullable = false)
	private String label;

	@CreationTimestamp
	private Timestamp firstUsed;

	@CreationTimestamp
	private Timestamp lastUsed;

	@ManyToMany(mappedBy = "hashtags", cascade = CascadeType.REMOVE)
	private List<Tweet> tweets;

}
