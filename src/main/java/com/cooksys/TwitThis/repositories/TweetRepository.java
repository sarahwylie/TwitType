package com.cooksys.TwitThis.repositories;

import com.cooksys.TwitThis.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
//    @Query("SELECT posted FROM Tweet ORDER BY posted DESC")
//    public List<Tweet> getByReverseChronology(Timestamp posted);
	
	Optional<Tweet> findById(Long id);
}