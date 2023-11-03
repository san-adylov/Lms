package com.example.lms.repository;

import com.example.lms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {

  Optional<User> getUserByEmail(String email);

}
