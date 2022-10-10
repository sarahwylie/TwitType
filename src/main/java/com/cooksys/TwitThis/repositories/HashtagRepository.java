package com.cooksys.TwitThis.repositories;

import com.cooksys.TwitThis.entities.Hashtag;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
	Optional<Hashtag> findByLabel(String label);
}