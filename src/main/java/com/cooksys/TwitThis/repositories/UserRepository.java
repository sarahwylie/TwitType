package com.cooksys.TwitThis.repositories;

import java.util.Optional;

import com.cooksys.TwitThis.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
//    @Query("SELECT user FROM User username")
//    public Optional<User> findByUsername(String username);

//    @Query("SELECT user FROM User password")
//    Optional<User> findByPassword(String password);

    public Optional<User> findByCredentials_Username(String username);
    
    public User findByCredentials_UsernameAndCredentials_Password(String username, String password);    

}